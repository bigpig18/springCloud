package com.source.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.source.coupon.entity.Coupon;
import com.source.coupon.exception.CouponException;
import com.source.coupon.service.IUserService;
import com.source.coupon.vo.AcquireTemplateRequest;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述: 用户服务对外接口
 *
 * @author li
 * @date 2019/12/10
 */
@RestController
@Slf4j
public class UserServiceController {

    private final IUserService userService;

    @Autowired
    public UserServiceController(IUserService userService) {
        this.userService = userService;
    }

    /**
     * 查询用户不同状态的优惠券
     * @param userId 用户id
     * @param status 优惠券状态
     * @return 对应优惠券
     * @throws CouponException 自定义异常
     */
    @GetMapping("/find/coupons")
    public List<Coupon> findCouponsByStatus(@RequestParam("userId") Long userId,
                                            @RequestParam("status") Integer status) throws CouponException {
        log.info("Find Coupons By Status - userId:{},status:{}",userId,status);
        return userService.findCouponsByStatus(userId, status);
    }

    /**
     * 根据userId 查询当前可领取的优惠券模板
     * @param userId 用户id
     * @return 可领取的优惠券模板
     * @throws CouponException 自定义异常
     */
    @GetMapping(value = "/template")
    public List<CouponTemplateSdk> findAvailableTemplate(Long userId) throws CouponException{
        log.info("Find Available CouponTemplate - userId:{}",userId);
        return userService.findAvailableTemplate(userId);
    }

    /**
     * 用户领取优惠券
     * @param request {@link AcquireTemplateRequest}
     * @return 优惠券信息
     * @throws CouponException 自定义异常
     */
    @PostMapping(value = "/acquire/template")
    public Coupon acquireTemplate(@RequestBody AcquireTemplateRequest request) throws CouponException{
        log.info("Acquire Template:{}", JSON.toJSONString(request,true));
        return userService.acquireTemplate(request);
    }

    /**
     * 结算优惠券
     * @param settlementInfo {@link SettlementInfo} 并无结算金额cost
     * @return {@link SettlementInfo} 计算出结算金额返回
     * @throws CouponException 自定义异常
     */
    @PostMapping(value = "/settlement")
    public SettlementInfo settlement(@RequestBody SettlementInfo settlementInfo) throws CouponException{
        log.info("Settlement Coupon:{}",JSON.toJSONString(settlementInfo,true));
        return userService.settlement(settlementInfo);
    }
}
