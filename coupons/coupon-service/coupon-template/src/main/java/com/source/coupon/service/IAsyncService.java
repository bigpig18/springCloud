package com.source.coupon.service;

import com.source.coupon.entity.CouponTemplate;

/**
 * 描述: 异步服务接口定义
 *
 * @author li
 * @date 2019/11/13
 */
public interface IAsyncService {

    /**
     * 根据模板异步创建优惠券码
     * @param template {@link CouponTemplate} 优惠券模板实体
     */
    void asyncConstructCouponTemplate(CouponTemplate template);
}
