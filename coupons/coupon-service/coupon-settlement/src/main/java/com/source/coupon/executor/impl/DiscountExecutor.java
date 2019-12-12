package com.source.coupon.executor.impl;

import com.source.coupon.constant.RuleFlag;
import com.source.coupon.executor.AbstractExecutor;
import com.source.coupon.executor.RuleExecutor;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述: 折扣优惠券结算规则执行器
 *
 * @author li
 * @date 2019/12/11
 */
@Component
@Slf4j
public class DiscountExecutor extends AbstractExecutor implements RuleExecutor {
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.DISCOUNT;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo info) {
        double goodsSum = retain3Decimals(goodsCostSum(info.getGoodsInfos()));
        SettlementInfo possibility = processGoodsTypeNotSatisfy(info,goodsSum);
        if (possibility != null){
            log.debug("Discount Coupon Is Not Match To GoodType");
            return possibility;
        }
        //折扣优惠券无门槛，可直接使用
        CouponTemplateSdk templateSdk = info.getCouponAndTemplateInfos().get(0).getTemplate();
        //获取折扣额度
        double quota = (templateSdk.getRule().getDiscount().getQuota()) * 1.0 / 100;
        //计算使用了优惠券之后的价钱
        double afterDiscount = retain3Decimals((goodsSum * quota) > minCost() ? (goodsSum * quota) : minCost());
        info.setCost(afterDiscount);
        log.debug("Use Discount Coupon Cost: {} To {}",goodsSum,afterDiscount);
        return info;
    }
}
