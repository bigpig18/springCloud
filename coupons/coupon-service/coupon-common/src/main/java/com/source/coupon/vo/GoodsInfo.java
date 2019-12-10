package com.source.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 商品信息对象
 *
 * @author li
 * @date 2019/12/3
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsInfo {

    /**
     * 商品类型
     */
    private Integer type;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品价格
     */
    private Double price;
    /**
     * 商品数量
     */
    private Integer count;
}
