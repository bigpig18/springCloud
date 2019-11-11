package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 优惠券分类
 *
 * @author li
 * @date 2019/11/11
 */
@AllArgsConstructor
@Getter
public enum CouponCategory {

    /**
     * 满减 - 折扣 - 立减
     */
    MONEY_OFF_COUPON("满减券","001"),
    DISCOUNT_COUPON("折扣券","002"),
    STAND_BY_COUPON("立减券","003");

    /**
     * 优惠券描述(分类)
     */
    private String description;
    /**
     * 优惠券分类编码
     */
    private String code;

    public static CouponCategory of(String code){
        //code 不能为空
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists"));
    }
}
