package com.source.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.Constant;
import com.source.coupon.constant.CouponStatus;
import com.source.coupon.entity.Coupon;
import com.source.coupon.exception.CouponException;
import com.source.coupon.service.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述: redis相关操作服务接口实现
 *
 * @author li
 * @date 2019/12/3
 */
@Slf4j
@Service
public class RedisServiceImpl implements IRedisService {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Coupon> getCachedCoupons(Long userId, Integer status) {
        log.info("get Coupons From Cache, userId:{},Status:{}",userId,status);
        String redisKey = statusToRedisKey(status,userId);
        List<String> couponInfo = redisTemplate.opsForHash().values(redisKey)
                .stream()
                .map(o -> Objects.toString(o,null))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponInfo)){
            saveEmptyCouponListToCache(userId, Collections.singletonList(status));
            return Collections.emptyList();
        }
        return couponInfo.stream()
                .map(cs -> JSON.parseObject(cs,Coupon.class))
                .collect(Collectors.toList());
    }

    @Override
    public void saveEmptyCouponListToCache(Long userId, List<Integer> status) {
        log.info("Save Empty CouponList To Cache For User:{},Status{}",userId, JSON.toJSONString(status));
        //key: coupon_id,value: 序列化的coupon
        Map<String,String> invalidCouponMap = new HashMap<>(16);
        //塞入无效的优惠券信息
        invalidCouponMap.put("-1",JSON.toJSONString(Coupon.invalidCoupon()));
        //使用 SessionCallBack 把数据命令放入 Redis 的 pipeline
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                status.forEach(s -> {
                    String redisKey = statusToRedisKey(s,userId);
                    redisOperations.opsForHash().putAll(redisKey,invalidCouponMap);
                });
                return null;
            }
        };
        log.info("Pipeline Exe Result: {}",JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
    }

    @Override
    public String tryToAcquireCouponCodeFromCache(Integer templateId) {
        String redisKey = String.format("%s%s",Constant.RedisPrefix.COUPON_TEMPLATE,templateId);
        // 优惠券码不存在顺序关系
        String couponCode = redisTemplate.opsForList().leftPop(redisKey);
        log.info("Acquire Coupon Code: {},{},{}",templateId,redisKey,couponCode);
        return couponCode;
    }

    @Override
    public Integer addCouponToCache(Long userId, List<Coupon> coupons, Integer status) throws CouponException {
        log.info("add Coupon To Cache:{},{},{}",userId,JSON.toJSONString(coupons,status));
        //保存在cache中coupon的个数
        Integer result = -1;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus){
            case USABLE: result = addCouponToCacheForUsable(userId, coupons);break;
            case USED: result = addCouponToCacheForUsed(userId, coupons);break;
            case EXPIRED: result = addCouponToCacheForExpired(userId,coupons);break;
            default:
        }
        return result;
    }

    /**
     * 新增优惠券到cache中
     * @param userId 用户id
     * @param coupons 优惠券
     * @return 新增数量
     */
    private Integer addCouponToCacheForUsable(Long userId,List<Coupon> coupons){
        // 如果status是usable，代表是新增加的优惠券，只会影响一个cache
        log.debug("Add Coupon To Cache For Usable.");
        //value
        Map<String,String> cacheObject = new HashMap<>(coupons.size());
        coupons.forEach(c -> cacheObject.put(c.getId().toString(),JSON.toJSONString(c)));
        String redisKey = statusToRedisKey(CouponStatus.USABLE.getCode(),userId);
        //插入redis
        redisTemplate.opsForHash().putAll(redisKey,cacheObject);
        log.info("Add Coupons To Cache:{},{},{}",cacheObject.size(),userId,redisKey);
        //设置过期时间
        redisTemplate.expire(redisKey,getRandomExpirationTime(1,2), TimeUnit.SECONDS);
        return cacheObject.size();
    }

    /**
     * 将已使用的优惠券加入到cache中
     * @param userId 用户id
     * @param coupons 优惠券信息
     * @return 数量
     * @throws CouponException 自定义异常
     */
    private Integer addCouponToCacheForUsed(Long userId,List<Coupon> coupons) throws CouponException{
        //若status 为 used ，代表用户操作是使用当前的优惠券，会影响到两个cache (usable,used)
        log.debug("Add Coupon To Cache For Used.");
        Map<String,String> needCachedForUsed = new HashMap<>(coupons.size());
        String redisKeyForUsable = statusToRedisKey(CouponStatus.USABLE.getCode(),userId);
        String redisKeyForUsed = statusToRedisKey(CouponStatus.USED.getCode(),userId);
        //获取当前用户可用的优惠券
        List<Coupon> usableCoupons = getCachedCoupons(userId,CouponStatus.USABLE.getCode());
        //当前可用的优惠券个数一定大于1
        assert usableCoupons.size() > coupons.size();
        coupons.forEach(c -> needCachedForUsed.put(c.getId().toString(),JSON.toJSONString(c)));
        //校验当前优惠券参数是否与cache中匹配(用id做匹配)
        List<Integer> usableIds = usableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        //如果 A(paramIds) 是 B(usableIds) 的子集，返回true
        if (!CollectionUtils.isSubCollection(paramIds,usableIds)){
            log.error("Coupons Is Not Equal To Cache: {},{},{}",userId,JSON.toJSONString(paramIds),JSON.toJSONString(usableIds));
            throw new CouponException("Coupons Is Not Equal To Cache");
        }
        Object[] needCleanKeys = paramIds.stream().map(Object::toString).toArray();
        SessionCallback<Object> sessionCallback = new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                //1.添加已使用的优惠券cache
                ops.opsForHash().putAll(redisKeyForUsed,needCachedForUsed);
                //2.清理可用的优惠券cache
                ops.opsForHash().delete(redisKeyForUsable,needCleanKeys);
                //3.重置过期时间
                ops.expire(redisKeyForUsable,getRandomExpirationTime(2,3),TimeUnit.SECONDS);
                ops.expire(redisKeyForUsed,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result:{}",JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    /**
     * 将过期优惠券加入到cache中
     * @param userId 用户id
     * @param coupons 优惠券信息
     * @return 数量
     * @throws CouponException 自定义异常
     */
    private Integer addCouponToCacheForExpired(Long userId,List<Coupon> coupons) throws CouponException{
        //status 为expired ，代表已过期的优惠券，影响到两个cache (expired,usable)
        log.debug("Add Coupon To Cache For Expired.");
        Map<String,String> needCacheForExpired = new HashMap<>(coupons.size());
        //获取到 usable + expired 的 redisKey (status + userId)
        String redisKeyForUsable = statusToRedisKey(CouponStatus.USABLE.getCode(),userId);
        String redisKeyForExpired = statusToRedisKey(CouponStatus.EXPIRED.getCode(),userId);
        //获取到当前用户可用优惠券
        List<Coupon> usableCoupons = getCachedCoupons(userId,CouponStatus.USABLE.getCode());
        //当前可用的优惠券数量一定大于1
        assert usableCoupons.size() > coupons.size();
        coupons.forEach(c -> needCacheForExpired.put(c.getId().toString(),JSON.toJSONString(c)));
        //校验当前优惠券参数是否与cache中匹配
        List<Integer> usableIds = usableCoupons.stream().map(Coupon::getId).collect(Collectors.toList());
        List<Integer> paramIds = coupons.stream().map(Coupon::getId).collect(Collectors.toList());
        if (!CollectionUtils.isSubCollection(paramIds,usableIds)){
            log.error("Coupons Is Not Equal To Cache: {},{},{}",userId,JSON.toJSONString(paramIds),JSON.toJSONString(usableIds));
            throw new CouponException("Coupons Is Not Equal To Cache");
        }
        Object[] needCleanKeys = paramIds.stream().map(Object::toString).toArray();
        SessionCallback sessionCallback = new SessionCallback() {
            @Override
            public Object execute(RedisOperations ops) throws DataAccessException {
                //保存已过期的优惠券
                ops.opsForHash().putAll(redisKeyForExpired,needCacheForExpired);
                //删除可用的优惠券
                ops.opsForHash().delete(redisKeyForUsable,needCleanKeys);
                //设置过期时间
                ops.expire(redisKeyForExpired,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
                ops.expire(redisKeyForUsable,getRandomExpirationTime(1,2),TimeUnit.SECONDS);
                return null;
            }
        };
        log.info("Pipeline Exe Result:{}",JSON.toJSONString(redisTemplate.executePipelined(sessionCallback)));
        return coupons.size();
    }

    /**
     * 根据status获取到对应的redis key
     * @param status 状态
     * @param userId 用户id
     * @return string
     */
    private String statusToRedisKey(Integer status,Long userId){
        String redisKey = null;
        CouponStatus couponStatus = CouponStatus.of(status);
        switch (couponStatus){
            case USABLE: redisKey = String.format("%s%s", Constant.RedisPrefix.USER_COUPON_USABLE,userId);break;
            case USED: redisKey = String.format("%s%s",Constant.RedisPrefix.USER_COUPON_USED,userId);break;
            case EXPIRED: redisKey = String.format("%s%s",Constant.RedisPrefix.USER_COUPON_EXPIRED,userId);break;
            default:
        }
        return redisKey;
    }

    /**
     * 获取一个随机的过期时间
     * @param min 最小的小时数
     * @param max 最大的小时数
     * @return 返回[min,max]
     */
    private Long getRandomExpirationTime(Integer min,Integer max){
        return RandomUtils.nextLong(min*60*60,max*60*60);
    }
}
