package com.source.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 获取优惠券请求对象定义
 *
 * @author li
 * @date 2019/12/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcquireTemplateRequest {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 优惠券模板信息
     */
    private CouponTemplateSdk templateSdk;
}
