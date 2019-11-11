package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 分发目标
 *
 * @author li
 * @date 2019/11/11
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {

    /**
     * 单用户 - 多用户
     */
    SINGLE("单用户",1),
    MULTI("多用户",2);

    /**
     * 分发目标描述
     */
    private String description;
    /**
     * 分发目标编码
     */
    private Integer code;

    public static DistributeTarget of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
