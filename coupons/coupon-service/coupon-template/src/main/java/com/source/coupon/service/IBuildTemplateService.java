package com.source.coupon.service;

import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.exception.CouponException;
import com.source.coupon.vo.TemplateRequest;

/**
 * 描述: 构建优惠券模板接口定义
 *
 * @author li
 * @date 2019/11/13
 */
public interface IBuildTemplateService {

    /**
     * 创建优惠券模板
     * @param request {@link TemplateRequest} 优惠券模板信息请求对象
     * @return {@link CouponTemplate}CouponTemplate
     * @throws CouponException 自定义异常
     */
    CouponTemplate buildTemplate (TemplateRequest request) throws CouponException;
}
