package com.source.coupon.executor.impl;

import com.source.coupon.constant.RuleFlag;
import com.source.coupon.executor.AbstractExecutor;
import com.source.coupon.executor.RuleExecutor;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 描述: 满减优惠券结算规则执行器
 *
 * @author li
 * @date 2019/12/11
 */
@Component
@Slf4j
public class MoneyOffExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MONEY_OFF;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo info) {
        //计算总价
        double goodsSum = retain3Decimals(goodsCostSum(info.getGoodsInfos()));
        SettlementInfo possibility = processGoodsTypeNotSatisfy(info,goodsSum);
        if (possibility != null){
            log.debug("Money_Off Coupon is Not Match To GoodsType");
            return possibility;
        }
        //判断满减是否符合折扣标准
        CouponTemplateSdk templateSdk = info.getCouponAndTemplateInfos().get(0).getTemplate();
        //获取基准值
        double base = (double) templateSdk.getRule().getDiscount().getBase();
        //获取额度
        double quota = (double) templateSdk.getRule().getDiscount().getQuota();
        //如果不符合标准
        if (goodsSum < base){
            log.debug("Current Goods Total Price < MoneyOff Coupon Base.");
            info.setCost(goodsSum);
            info.setCouponAndTemplateInfos(Collections.emptyList());
            return info;
        }
        //计算使用优惠券之后的价格
        double afterDiscount = retain3Decimals((goodsSum - quota) > minCost() ? (goodsSum - quota) : minCost());
        info.setCost(afterDiscount);
        log.debug("Use MoneyOff Coupon Cost: {} To {}",goodsSum,afterDiscount);
        return info;
    }
}
