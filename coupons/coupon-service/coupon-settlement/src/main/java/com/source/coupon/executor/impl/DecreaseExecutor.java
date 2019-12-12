package com.source.coupon.executor.impl;

import com.source.coupon.constant.RuleFlag;
import com.source.coupon.executor.AbstractExecutor;
import com.source.coupon.executor.RuleExecutor;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述: 立减券结算规则执行器
 *
 * @author li
 * @date 2019/12/11
 */
@Component
@Slf4j
public class DecreaseExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.DECREASE;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo info) {
        //得到商品总价
        double goodsSum = retain3Decimals(goodsCostSum(info.getGoodsInfos()));
        SettlementInfo possibility = processGoodsTypeNotSatisfy(info,goodsSum);
        if (possibility != null){
            log.debug("Decrease Coupon Is Not Match To GoodsType.");
            return possibility;
        }
        CouponTemplateSdk templateSdk = info.getCouponAndTemplateInfos().get(0).getTemplate();
        //立减无门槛,直接使用,获取额度
        double quota = templateSdk.getRule().getDiscount().getQuota();
        //计算使用优惠券之后的价格
        double afterDiscount = retain3Decimals((goodsSum - quota) > minCost() ? (goodsSum - quota) : minCost());
        info.setCost(afterDiscount);
        log.debug("Use Decrease Coupon Cost: {} To {}",goodsSum,afterDiscount);
        return info;
    }
}
