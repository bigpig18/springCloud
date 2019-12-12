package com.source.coupon.executor.impl;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.CouponCategory;
import com.source.coupon.constant.RuleFlag;
import com.source.coupon.executor.AbstractExecutor;
import com.source.coupon.executor.RuleExecutor;
import com.source.coupon.vo.GoodsInfo;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: 满减 + 折扣优惠券结算规则执行器
 *
 * @author li
 * @date 2019/12/12
 */
@Component
@Slf4j
public class MoneyOffDiscountExecutor extends AbstractExecutor implements RuleExecutor {

    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MONEY_OFF_DISCOUNT;
    }

    @Override
    public SettlementInfo computeRule(SettlementInfo info) {
        //得到商品总价
        double goodsSum = retain3Decimals(goodsCostSum(info.getGoodsInfos()));
        //商品类型是否符合校验
        SettlementInfo possibility = processGoodsTypeNotSatisfy(info,goodsSum);
        if (possibility != null){
            log.debug("MoneyOff And Discount Coupon Is Not Match To GoodsType.");
            return possibility;
        }
        //识别满减 折扣优惠券并填充到下面两个对象中
        SettlementInfo.CouponAndTemplateInfo moneyOff = null;
        SettlementInfo.CouponAndTemplateInfo discount = null;
        for (SettlementInfo.CouponAndTemplateInfo ct : info.getCouponAndTemplateInfos()) {
            if (CouponCategory.of(ct.getTemplate().getCategory()) == CouponCategory.MONEY_OFF_COUPON){
                moneyOff = ct;
            }else {
                discount = ct;
            }
        }
        assert moneyOff != null;
        assert discount != null;
        //若当前优惠券和满减券不能公用，需要清空优惠券，返回商品原价
        if (!isCouponCanShared(moneyOff,discount)){
            log.debug("Current MoneyOff And Discount Can't Shared");
            info.setCost(goodsSum);
            info.setCouponAndTemplateInfos(Collections.emptyList());
            return info;
        }
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = new ArrayList<>();
        //计算满减
        double moneyOffBase = (double) moneyOff.getTemplate().getRule().getDiscount().getBase();
        double monetOffQuota = (double) moneyOff.getTemplate().getRule().getDiscount().getQuota();
        //最终价格
        double targetSum = goodsSum;
        //看商品总价是否满足基准值
        if (targetSum >= moneyOffBase){
            targetSum -= monetOffQuota;
            ctInfos.add(moneyOff);
        }
        //计算折扣
        double discountQuota = (discount.getTemplate().getRule().getDiscount().getQuota()) * 1.0 / 100;
        targetSum *= discountQuota;
        ctInfos.add(discount);
        //实际上做计算的优惠券
        info.setCouponAndTemplateInfos(ctInfos);
        //用完优惠券之后的价格
        double afterDiscount = retain3Decimals(targetSum > minCost() ? targetSum : minCost());
        info.setCost(afterDiscount);
        log.debug("Use MoneyOff And Discount Coupon Cost: {} To {}",goodsSum,afterDiscount);
        return info;
    }

    /**
     * 判断两种优惠券是否可以一起使用
     * 即校验TemplateRule 中 weight 是否满足条件
     * @param moneyOff 满减券
     * @param discount 折扣券
     * @return 判断结果
     */
    private boolean isCouponCanShared(SettlementInfo.CouponAndTemplateInfo moneyOff,
                                      SettlementInfo.CouponAndTemplateInfo discount){
        //获取满减 折扣券的优惠券模板编码
        String moneyOffKey = moneyOff.getTemplate().getKey() + String.format("%04d",moneyOff.getTemplate().getId());
        String discountKey = discount.getTemplate().getKey() + String.format("%04d",moneyOff.getTemplate().getId());
        //获取其所有权重
        List<String> allSharedKeysForMoneyOff = new ArrayList<>();
        List<String> allSharedKeysForDiscount = new ArrayList<>();
        allSharedKeysForMoneyOff.add(moneyOffKey);
        allSharedKeysForMoneyOff.addAll(JSON.parseObject(moneyOff.getTemplate().getRule().getWeight(),List.class));
        allSharedKeysForDiscount.add(discountKey);
        allSharedKeysForDiscount.addAll(JSON.parseObject(discount.getTemplate().getRule().getWeight(),List.class));
        //判断 满减 折扣券是否是weight子集
        return CollectionUtils.isSubCollection(Arrays.asList(moneyOffKey,discountKey),allSharedKeysForMoneyOff)
                || CollectionUtils.isSubCollection(Arrays.asList(moneyOffKey,discountKey),allSharedKeysForDiscount);
    }

    /**
     * 实现满减 + 折扣优惠券校验
     * @param info {@link SettlementInfo}
     * @return 是否符合优惠券规则
     */
    @Override
    public boolean isGoodsTypeSatisfy(SettlementInfo info) {
        log.debug("Check MoneyOff And Discount Is Match Or Not.");
        //得到用户传入的商品类型
        List<Integer> goodsType = info.getGoodsInfos().stream().map(GoodsInfo::getType).collect(Collectors.toList());
        //优惠券模板所支持的商品类型
        List<Integer> templateGoodsType = new ArrayList<>();
        info.getCouponAndTemplateInfos().forEach(ct -> templateGoodsType.addAll(JSON.parseObject(ct.getTemplate().getRule().getUsage().getGoodsType(),List.class)));
        //若想使用多类优惠券，用户传入的商品类型必须都包含在优惠券模板中，差集为空
        return CollectionUtils.isEmpty(CollectionUtils.subtract(goodsType,templateGoodsType));
    }
}
