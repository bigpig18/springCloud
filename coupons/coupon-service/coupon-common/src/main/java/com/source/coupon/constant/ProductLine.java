package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 产品线枚举
 *
 * @author li
 * @date 2019/11/11
 */
@Getter
@AllArgsConstructor
public enum ProductLine {

    /**
     * 产品线
     */
    HAMMER_LINE("铁锤",1),
    PROP_LINE("铁柱",2);

    /**
     * 产品线描述
     */
    private String description;
    /**
     * 优惠券分类编码
     */
    private Integer code;

    public static ProductLine of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
