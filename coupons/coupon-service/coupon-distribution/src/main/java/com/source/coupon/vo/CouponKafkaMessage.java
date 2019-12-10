package com.source.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述: 优惠券kafka消息对象定义
 *
 * @author li
 * @date 2019/12/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponKafkaMessage {

    /**
     * 优惠券状态
     */
    private Integer status;
    /**
     * coupon主键
     */
    private List<Integer> ids;

}
