package com.source.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 操作模式的枚举定义
 *
 * @author li
 * @date 2020/9/11
 */
@Getter
@AllArgsConstructor
public enum OpModeEnum {

    /**
     * 操作模式的定义，读写两种
     */
    READ("读"),
    WRITE("写");

    private String mode;
}
