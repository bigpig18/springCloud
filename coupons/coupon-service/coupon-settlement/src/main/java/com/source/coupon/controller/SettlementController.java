package com.source.coupon.controller;

import com.source.coupon.exception.CouponException;
import com.source.coupon.executor.ExecuteManager;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 描述: 结算服务controller
 *
 * @author li
 * @date 2019/12/12
 */
@RestController
@Slf4j
public class SettlementController {

    private final ExecuteManager executeManager;

    @Autowired
    public SettlementController(ExecuteManager executeManager) {
        this.executeManager = executeManager;
    }

    /**
     * 优惠券规则计算
     * @param info 结算信息
     * @return {@link SettlementInfo}
     * @throws CouponException 自定义异常
     */
    @PostMapping(value = "/settlement/compute")
    SettlementInfo computeRule(@RequestBody SettlementInfo info) throws CouponException{
        log.info("Settlement : {}",info);
        return executeManager.computeRule(info);
    }
}
