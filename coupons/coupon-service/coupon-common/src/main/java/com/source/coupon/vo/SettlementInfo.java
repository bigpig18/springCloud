package com.source.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 描述: 结算信息对象定义
 *
 * @author li
 * @date 2019/12/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfo {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 优惠券列表
     */
    private List<CouponAndTemplateInfo> couponAndTemplateInfos;
    /**
     * 商品信息
     */
    private List<GoodsInfo> goodsInfos;
    /**
     * 是否使结算生效(核销true)
     */
    private Boolean employ;
    /**
     * 结果结算金额
     */
    private Double cost;

    /**
     * 优惠券和模板信息
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponAndTemplateInfo{

        /**
         * coupon id
         */
        private Integer id;
        /**
         * 优惠券对应的模板对象
         */
        private CouponTemplateSdk template;
    }
}
