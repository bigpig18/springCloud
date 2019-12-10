package com.source.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.Constant;
import com.source.coupon.constant.CouponStatus;
import com.source.coupon.dao.CouponDao;
import com.source.coupon.entity.Coupon;
import com.source.coupon.exception.CouponException;
import com.source.coupon.feign.SettlementClient;
import com.source.coupon.feign.TemplateClient;
import com.source.coupon.service.IRedisService;
import com.source.coupon.service.IUserService;
import com.source.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述: 用户服务相关接口实现
 * 所有操作过程，状态都保存到redis中，并通过kafka将消息传递到mysql中
 * @author li
 * @date 2019/12/6
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    private final CouponDao couponDao;
    private final IRedisService redisService;
    private KafkaTemplate<String,String> kafkaTemplate;

    private final TemplateClient templateClient;
    private final SettlementClient settlementClient;

    @Autowired
    public UserServiceImpl(CouponDao couponDao, IRedisService redisService, KafkaTemplate<String, String> kafkaTemplate, TemplateClient templateClient, SettlementClient settlementClient) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.kafkaTemplate = kafkaTemplate;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
    }

    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {
        // 从redis中查询
        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;
        if (CollectionUtils.isNotEmpty(curCached)){
            log.debug("Coupon Cache Is Not Empty:{},{}",userId,status);
            preTarget = curCached;
        }else {
            log.debug("Coupon Cache Is Empty,Get Coupons From Db:{},{}",userId,status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            //若数据库中无记录，直接返回
            if (CollectionUtils.isEmpty(dbCoupons)){
                log.debug("Current User Do Not Have Coupon: {},{}",userId,status);
                return dbCoupons;
            }
            //填充dbCoupons的TemplateSdk字段
            Map<Integer,CouponTemplateSdk> id2TemplateSdk = templateClient.findIds2TemplateSdk(
                    dbCoupons.stream()
                            .map(Coupon::getTemplateId)
                            .collect(Collectors.toList())).getData();
            dbCoupons.forEach(dc -> dc.setTemplateSdk(id2TemplateSdk.get(dc.getTemplateId())));
            //数据库存在信息
            preTarget = dbCoupons;
            //将记录写入cache
            redisService.addCouponToCache(userId,preTarget,status);
        }
        //将无效优惠券剔除
        preTarget = preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        //如果当前获取的优惠券是可用的，要对已过期优惠券做延迟处理
        if (CouponStatus.of(status) == CouponStatus.USABLE){
            CouponClassify classify = CouponClassify.classify(preTarget);
            //如果已过期状态不为空，需要做延迟处理
            if (CollectionUtils.isNotEmpty(classify.getExpired())){
                log.info("Add Expired Coupon To Cache From 'findCouponsByStatus':{},{}",userId,status);
                redisService.addCouponToCache(userId,classify.getExpired(),CouponStatus.EXPIRED.getCode());
                //发送到kafka中做异步处理
                kafkaTemplate.send(Constant.TOPIC, JSON.toJSONString(new CouponKafkaMessage(
                        CouponStatus.EXPIRED.getCode(),
                        classify.getExpired().stream().map(Coupon::getId).collect(Collectors.toList())
                )));
            }
            return classify.getUsable();
        }
        return preTarget;
    }

    @Override
    public List<CouponTemplateSdk> findAvailableTemplate(Long userId) throws CouponException {
        //得到当前时间戳
        long curTime = System.currentTimeMillis();
        //先查询出所有可用的优惠券模板信息
        List<CouponTemplateSdk> templateSdks = templateClient.findAllUsableTemplate().getData();
        log.debug("Find All Template From TemplateClient,Count: {}",templateSdks.size());
        //过滤过期的优惠券模板
        templateSdks = templateSdks.stream()
                .filter(t -> t.getRule().getExpiration().getDeadline() >= curTime)
                .collect(Collectors.toList());
        log.info("Find Usable Template Count: {}",templateSdks.size());
        //key -> templateId
        //value -> Pair(left -> Template limitation(优惠券领取数量限制),right -> 优惠券模板信息)
        // 实际上Pair保存的应该说是一个信息对，两个信息都是我们需要的，没有key和value之分
        Map<Integer, Pair<Integer,CouponTemplateSdk>> limit2Template = new HashMap<>(templateSdks.size());
        templateSdks.forEach(t -> limit2Template.put(
                t.getId(),
                Pair.of(t.getRule().getLimitation(),t)
        ));
        List<CouponTemplateSdk> result = new ArrayList<>(limit2Template.size());
        //查询可用状态的优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId,CouponStatus.USABLE.getCode());
        log.debug("Current User Has Usable Coupons: userId - {},size - {}",userId,userUsableCoupons.size());
        // key -> templateId ; value -> Coupons
        Map<Integer,List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));
        //根据 template 的 rule 判断是否可以领取优惠券模板
        limit2Template.forEach((k,v) -> {
            int limitation = v.getLeft();
            CouponTemplateSdk sdk = v.getRight();
            if (templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitation){
                return;
            }
            result.add(sdk);
        });
        return result;
    }

    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {
        //获取优惠券模板
        Map<Integer,CouponTemplateSdk> id2Template = templateClient.findIds2TemplateSdk(
                Collections.singleton(request.getTemplateSdk().getId())
        ).getData();
        //优惠券模板是否存在的
        if (id2Template.size() <= 0){
            log.error("Can Not Acquire Template From TemplateClient:{}",request.getTemplateSdk().getId());
            throw new CouponException("Can Not Acquire Template From TemplateClient");
        }
        //判断用户是否可以领取优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(request.getUserId(),CouponStatus.USABLE.getCode());
        Map<Integer,List<Coupon>> templateId2Coupons = userUsableCoupons.stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));
        if (templateId2Coupons.containsKey(request.getTemplateSdk().getId())
                && templateId2Coupons.get(request.getTemplateSdk().getId()).size() >= request.getTemplateSdk().getRule().getLimitation()){
            log.error("Exceed Template Assign Limitation:{}",request.getTemplateSdk().getId());
            throw new CouponException("Exceed Template Assign Limitation");
        }
        //尝试领取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(request.getTemplateSdk().getId());
        if (StringUtils.isEmpty(couponCode)){
            log.error("Can Not Acquire Coupon Code:{}",request.getTemplateSdk().getId());
            throw new CouponException("Can Not Acquire Coupon Code");
        }
        //构建coupon信息
        Coupon coupon = new Coupon(
                request.getTemplateSdk().getId(),
                request.getUserId(),
                couponCode,
                CouponStatus.USABLE
        );
        //保存到db
        coupon = couponDao.save(coupon);
        //填充coupon 中 templateSdk
        coupon.setTemplateSdk(request.getTemplateSdk());
        redisService.addCouponToCache(request.getUserId(),Collections.singletonList(coupon),CouponStatus.USABLE.getCode());
        return coupon;
    }

    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        //当未传递优惠券时，直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = info.getCouponAndTemplateInfos();
        if (CollectionUtils.isEmpty(ctInfos)){
            log.info("Empty Coupon For Settle.");
            double goodsSum = 0.0;
            //得到总价
            for (GoodsInfo goodsInfo : info.getGoodsInfos()){
                goodsSum += goodsInfo.getPrice() * goodsInfo.getCount();
            }
            //无优惠券不存在优惠券核销,只需要修改cost字段
            info.setCost(retain3Decimals(goodsSum));
            return info;
        }
        //校验传递的优惠券是否是用户自己的
        //先得到该用户可用的优惠券
        List<Coupon> coupons = findCouponsByStatus(info.getUserId(),CouponStatus.USABLE.getCode());
        //将用户可用优惠券从 list -> map
        //key - couponId ; value - Coupon
        Map<Integer,Coupon> id2Coupon = coupons.stream().collect(Collectors.toMap(
                Coupon::getId,
                Function.identity()
        ));
        //得到传入的优惠券id
        List<Integer> ids = ctInfos.stream().map(SettlementInfo.CouponAndTemplateInfo::getId).collect(Collectors.toList());
        //当 id2Coupon(用户可用优惠券)为空 或 传递进来的优惠券(ids)不包含在用户可用优惠券(id2Coupon)内，请求非法
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(ids,id2Coupon.keySet())){
            log.error("User Coupon Has Some Problem,It's Not SubCollection Of Coupons - settlement:{},userUsable:{}",ids,id2Coupon.keySet());
            throw new CouponException("User Coupon Has Some Problem,It's Not SubCollection Of Coupons");
        }
        log.debug("Current Settlement Coupon is Users.");
        //传入的优惠券信息
        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));
        //通过结算微服务获取结算信息
        SettlementInfo processInfo = settlementClient.computeRule(info).getData();
        //如果 employ 为 true 即核销 且 优惠券列表不为空(结算微服务报错会将优惠券列表置空),则核销优惠券,即更新cache以及db
        if (processInfo.getEmploy() && CollectionUtils.isNotEmpty(processInfo.getCouponAndTemplateInfos())){
            log.info("Settle User Coupon - userId: {},settleCoupons: {}",info.getUserId(),JSON.toJSONString(settleCoupons,true));
            //核销优惠券
            //更新缓存
            redisService.addCouponToCache(info.getUserId(),settleCoupons,CouponStatus.USED.getCode());
            //更新db
            kafkaTemplate.send(
                    Constant.TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                                    CouponStatus.USED.getCode(),
                                    settleCoupons.stream().map(Coupon::getId).collect(Collectors.toList())))
            );
        }
        return processInfo;
    }

    /**
     * 保留三位小数
     * @param value 值
     * @return 三位小数
     */
    private double retain3Decimals(double value){
        //BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value)
                .setScale(3,BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }
}
