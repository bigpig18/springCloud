package com.source.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.source.coupon.constant.Constant;
import com.source.coupon.dao.CouponTemplateDao;
import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 描述: 异步服务接口实现
 *
 * @author li
 * @date 2019/11/14
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    private final CouponTemplateDao templateDao;

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao, StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    @Async("getAsyncExecutor")
    @Override
    public void asyncConstructCouponTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> couponCodes = buildCouponCode(template);
        //优惠券在redis中的key
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE,template.getId().toString());
        log.info("Put Coupon Code To Redis: {}",redisTemplate.opsForList().rightPushAll(redisKey,couponCodes));
        //可用状态
        template.setAvailable(true);
        templateDao.save(template);
        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms",watch.elapsed(TimeUnit.MILLISECONDS));
        //TODO 发送邮件给某某,优惠券模板可用
        log.info("CouponTemplate({}) Is Available",template.getId());
    }

    /**
     * 构造优惠券码 --> 优惠券码(对应每一张优惠券,18位)
     * 前四位：产品线 + 类型
     * 中间六位：日期随机(19 04 01)
     * 后八位：0~9 随机数构成
     * @param template {@link CouponTemplate} 优惠券模板实体对象
     * @return Set<String> 返回的数量是与template.count 相同个数的优惠券码
     */
    private Set<String> buildCouponCode(CouponTemplate template){
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> result = new HashSet<>(template.getCount());
        //前四位的生成 (产品线 + 类型)
        String prefix4 = template.getProductLine().getCode().toString() + template.getCategory().getCode();
        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());
        //for循环的效率会比while高很多，所有先用for循环，如果数量达不到，再用while
        for (int i = 0; i != template.getCount() ; i++) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        while (result.size() < template.getCount()){
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        assert result.size() == template.getCount();
        watch.stop();
        //构建这么多优惠券码一共用了多少时间
        log.info("Build Coupon Code Cost: {}ms",watch.elapsed(TimeUnit.MILLISECONDS));
        return result;
    }

    /**
     * 构造优惠券码的后14位
     * @param date 创建优惠券的日期
     * @return 14位优惠券码
     */
    private String buildCouponCodeSuffix14(String date){
        char[] bases = new char[]{'1','2','3','4','5','6','7','8','9'};
        //中间六位
        List<Character> chars = date.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String mid6 = chars.stream().map(Object::toString).collect(Collectors.joining());
        //后八位
        String suffix8 = RandomStringUtils.random(1,bases) + RandomStringUtils.randomNumeric(7);
        return mid6 + suffix8;
    }
}
