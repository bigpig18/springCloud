package com.source.coupon.vo;

import com.source.coupon.constant.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * 描述: 优惠券规则对象定义
 *
 * @author li
 * @date 2019/11/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRule {

    /**
     * 优惠券过期规则
     */
    private Expiration expiration;
    /**
     * 折扣规则
     */
    private Discount discount;
    /**
     * 每个人针对某个优惠券可以领几张的限制
     */
    private Integer limitation;
    /**
     * 使用范围: 地域 + 商品类型
     */
    private Usage usage;
    /**
     * 权重(可以和哪些优惠券叠加使用,同一类的优惠券一定不能叠加: list[code])
     */
    private String weight;

    public boolean validate(){
        return expiration.validate() && discount.validate()
                && usage.validate() && limitation > 0
                && StringUtils.isNotEmpty(weight);
    }

    /**
     * 有限期限规则
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Expiration{
        /**
         * 有限期规则 对应 periodType 的 code 字段
         */
        private Integer period;
        /**
         * 有效间隔; 只对变动性有效期有效
         */
        private Integer gap;
        /**
         * 优惠券模板的失效日期,两类规则都有效(时间戳)
         */
        private Long deadline;

        boolean validate(){
            //最简化校验
            return null != PeriodType.of(period) && gap > 0 && deadline >0;
        }
    }

    /**
     * 折扣，需要与类型配合决定
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount{

        /**
         * 额度: 满减(20),折扣(85),立减(10)
         */
        private Integer quota;
        /**
         * 对满减有效,基准(需要满多少才可用)
         */
        private Integer base;

        boolean validate(){
            return quota > 0 && base > 0;
        }
    }

    /**
     * 使用范围
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage{

        /**
         * 可以使用的省份
         */
        private String province;
        /**
         * 可以使用的城市
         */
        private String city;
        /**
         * 商品类型: list[文娱,生鲜, ... ]
         */
        private String goodsType;

        boolean validate(){
            return StringUtils.isNotEmpty(province)
                    && StringUtils.isNotEmpty(city)
                    && StringUtils.isNotEmpty(goodsType);
        }
    }
}
