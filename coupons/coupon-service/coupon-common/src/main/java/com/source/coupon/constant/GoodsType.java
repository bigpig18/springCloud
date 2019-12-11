package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * 描述: 商品类型枚举
 *
 * @author li
 * @date 2019/12/3
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    /**
     * 文娱 生鲜 家居 其他 全品类
     */
    AMUSEMENT("文娱",1),
    FRESH("生鲜",2),
    HOME("家居",3),
    OTHERS("其他",4),
    ALL("全品类",5);

    /**
     * 商品类型描述
     */
    private String description;
    /**
     * 商品类型编码
     */
    private Integer code;

    public static GoodsType of(Integer code){
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + "not exists"));
    }

}
