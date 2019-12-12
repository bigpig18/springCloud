package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 规则类型枚举定义
 *
 * @author li
 * @date 2019/12/11
 */
@Getter
@AllArgsConstructor
public enum RuleFlag {

    /**
     * 单类别优惠券
     */
    MONEY_OFF("满减券计算规则"),
    DISCOUNT("折扣券计算规则"),
    DECREASE("立减券计算规则"),
    /**
     * 多类别优惠券定义
     */
    MONEY_OFF_DISCOUNT("满减+折扣券计算规则");
    
    /**
     * 规则描述
     */
     private String description;
     
}
