package com.source.coupon.executor;

import com.source.coupon.constant.RuleFlag;
import com.source.coupon.vo.SettlementInfo;

/**
 * 描述: 优惠券模板规则处理器接口定义
 *
 * @author li
 * @date 2019/12/11
 */
public interface RuleExecutor {

    /**
     * 规则类型标记
     * @return {@link RuleFlag}
     */
    RuleFlag ruleConfig();

    /**
     * 优惠券规则计算
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo} 包含修正后的结算信息
     */
    SettlementInfo computeRule(SettlementInfo info);
}
