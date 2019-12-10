package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 用户优惠券状态
 *
 * @author li
 * @date 2019/12/2
 */
@AllArgsConstructor
@Getter
public enum CouponStatus {

    /**
     * 可用，未使用，已过期
     */
    USABLE("可用的",1),
    USED("已使用的",2),
    EXPIRED("过期的(未被使用的)",3);

    /**
     * 优惠券状态描述信息
     */
    private String description;
    /**
     * 优惠券状态编码
     */
    private Integer code;

    /**
     * 根据code获取CouponStatus
     * @param code 编码
     * @return CouponStatus
     */
    public static CouponStatus of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists"));
    }
}
