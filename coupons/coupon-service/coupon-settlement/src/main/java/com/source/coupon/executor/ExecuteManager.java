package com.source.coupon.executor;

import com.source.coupon.constant.CouponCategory;
import com.source.coupon.constant.RuleFlag;
import com.source.coupon.exception.CouponException;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 优惠券结算规则执行管理器
 * 根据用户的请求，找到对应的executor做结算
 * BeanPostProcessor: bean 后置处理器
 * 即当所有的bean都被创建处理且放入spring容器中，两个接口才会被执行到
 * @author li
 * @date 2019/12/12
 */
@Component
@Slf4j
public class ExecuteManager implements BeanPostProcessor {

    /**
     * 规则执行器映射
     */
    private static Map<RuleFlag,RuleExecutor> executorIndex = new HashMap<>(RuleFlag.values().length);

    /**
     * 优惠券结算规则 计算入口
     * @param info {@link SettlementInfo}
     * @return 结算后
     */
    public SettlementInfo computeRule(SettlementInfo info) throws CouponException {
        SettlementInfo result = null;
        //单类优惠券
        if (info.getCouponAndTemplateInfos().size() == 1){
            //获取优惠券类别
            CouponCategory category = CouponCategory.of(info.getCouponAndTemplateInfos().get(0).getTemplate().getCategory());
            switch (category){
                case MONEY_OFF_COUPON: result = executorIndex.get(RuleFlag.MONEY_OFF).computeRule(info);break;
                case DISCOUNT_COUPON: result = executorIndex.get(RuleFlag.DISCOUNT).computeRule(info);break;
                case DECREASE_COUPON: result = executorIndex.get(RuleFlag.DECREASE).computeRule(info);break;
                default:
            }
        }else {
            //多类优惠券
            List<CouponCategory> categories = new ArrayList<>();
            info.getCouponAndTemplateInfos().forEach(ct -> categories.add(CouponCategory.of(ct.getTemplate().getCategory())));
            if (categories.size() != 2){
                log.error("Not Support For More Template Category.");
                throw new CouponException("Not Support For More Template Category.");
            }else {
                if (categories.contains(CouponCategory.MONEY_OFF_COUPON ) && categories.contains(CouponCategory.DISCOUNT_COUPON)){
                    result = executorIndex.get(RuleFlag.MONEY_OFF_DISCOUNT).computeRule(info);
                }else{
                    log.error("Not Support For Other Template Category.");
                    throw new CouponException("Not Support For Other Template Category.");
                }
            }
        }
        return result;
    }

    /**
     * 在bean初始化之前去执行
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (!(bean instanceof RuleExecutor)){
            return bean;
        }
        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag flag = executor.ruleConfig();
        if (executorIndex.containsKey(flag)){
            log.error("There is already an executor for ruleFlag: {}",flag);
            throw new IllegalStateException("There is already an executor for ruleFlag: " + flag);
        }
        log.info("Load Executor {} For Rule Flag {}",executor.getClass(),flag);
        executorIndex.put(flag,executor);
        return null;
    }

    /**
     * 在bean初始化之后执行
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
