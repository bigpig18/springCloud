package com.source.coupon.service;

import com.source.coupon.entity.Coupon;
import com.source.coupon.exception.CouponException;

import java.util.List;

/**
 * 描述: redis相关服务接口定义
 *  1.用户的三个状态优惠券 Cache 相关操作
 *  2.优惠券模板生成的优惠券码 Cache 操作
 * @author li
 * @date 2019/12/2
 */
public interface IRedisService {

    /**
     * 根据userId 和 状态 找到缓存的优惠券列表数据
     * 可能会返回null，代表从无记录
     * @param userId 用户id
     * @param status 优惠券状态
     * @return {@link Coupon}list
     */
    List<Coupon> getCachedCoupons(Long userId,Integer status);

    /**
     * 保存空的优惠券列表到缓存中(防止缓存穿透)
     * @param userId 用户id
     * @param status 优惠券状态列表
     */
    void saveEmptyCouponListToCache(Long userId,List<Integer> status);

    /**
     * 尝试从cache中获取一个优惠券码
     * @param templateId 优惠券模板id
     * @return 优惠券码
     */
    String tryToAcquireCouponCodeFromCache(Integer templateId);

    /**
     * 将优惠券保存到cache中
     * @param userId 用户id
     * @param coupons {@link Coupon}
     * @param status 优惠券状态
     * @return 保存成功的个数
     * @throws CouponException 自定义异常
     */
    Integer addCouponToCache(Long userId,List<Coupon> coupons,Integer status) throws CouponException;
}

