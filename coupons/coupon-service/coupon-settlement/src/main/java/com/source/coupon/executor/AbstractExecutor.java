package com.source.coupon.executor;

import com.alibaba.fastjson.JSON;
import com.source.coupon.vo.GoodsInfo;
import com.source.coupon.vo.SettlementInfo;
import java.math.RoundingMode;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: 规则执行器抽象类
 *
 * @author li
 * @date 2019/12/11
 */
public abstract class AbstractExecutor {

    /**
     * 校验商品类型与优惠券是否匹配
     * 1.实现单品类优惠券的校验，多品类优惠券重载此方法
     * 2.商品只需要有一个优惠券要求的商品类型匹配
     * @param info {@link SettlementInfo}
     * @return boolean
     */
    protected boolean isGoodsTypeSatisfy(SettlementInfo info){
        //获取商品信息中商品种类合集
        List<Integer> goodsType = info.getGoodsInfos().stream().map(GoodsInfo::getType).collect(Collectors.toList());
        //得到优惠券模板中定义的规则中的商品类型 - 因为现在是实现单类优惠券，CouponAndTemplateInfo 只会有一条信息
        String templateOne = info.getCouponAndTemplateInfos().get(0).getTemplate().getRule().getUsage().getGoodsType();
        List<Integer> templateType = JSON.parseObject(templateOne,List.class);
        //存在交集即可
        return CollectionUtils.isNotEmpty(CollectionUtils.intersection(goodsType,templateType));
    }

    /**
     * 处理商品类型与优惠券限制不匹配的情况
     * @param info {@link SettlementInfo} 用户传递的结算信息
     * @param goodsSum 商品总价
     * @return 已修改过的结算信息
     */
    protected SettlementInfo processGoodsTypeNotSatisfy(SettlementInfo info, double goodsSum){
        boolean isGoodsTypeSatisfy = isGoodsTypeSatisfy(info);
        //当商品类型不满足时，直接返回总价并清空优惠券
        if (!isGoodsTypeSatisfy){
            info.setCost(goodsSum);
            info.setCouponAndTemplateInfos(Collections.emptyList());
            return info;
        }
        return null;
    }

    /**
     * 商品总价
     * @param infos {@link GoodsInfo} 商品信息
     * @return 商品总价
     */
    protected double goodsCostSum(List<GoodsInfo> infos){
        return infos.stream().mapToDouble(g -> g.getCount() * g.getPrice()).sum();
    }

    /**
     * 保留3位小数
     * @param value 值
     * @return 3位小数值
     */
    protected double retain3Decimals(double value){
        return new BigDecimal(value).setScale(3, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * 最小支付费用
     * @return 最小支付费用
     */
    protected double minCost(){
        //优惠券可能会使价格变成负数
        return 0.1;
    }
}
