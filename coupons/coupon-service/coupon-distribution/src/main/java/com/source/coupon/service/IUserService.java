package com.source.coupon.service;

import com.source.coupon.entity.Coupon;
import com.source.coupon.exception.CouponException;
import com.source.coupon.vo.AcquireTemplateRequest;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * 描述: 用户服务相关接口定义
 *
 * @author li
 * @date 2019/12/3
 */
public interface IUserService {

    /**
     * 根据userId + 状态 查询优惠券信息
     * @param userId 用户id
     * @param status 优惠券状态
     * @return {@link Coupon}list
     * @throws CouponException 自定义异常
     */
    List<Coupon> findCouponsByStatus(Long userId,Integer status) throws CouponException;

    /**
     * 根据userId 查询当前可领取的优惠券模板
     * @param userId 用户id
     * @return {@link CouponTemplateSdk} list
     * @throws CouponException 自定义异常
     */
    List<CouponTemplateSdk> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * 用户领取优惠券
     * @param request {@link AcquireTemplateRequest}获取优惠券请求对象定义
     * @return {@link Coupon}
     * @throws CouponException 自定义异常
     */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;

    /**
     * 结算优惠券
     * @param settlementInfo {@link SettlementInfo} 并无结算金额cost
     * @return {@link SettlementInfo} 计算出结算金额返回
     * @throws CouponException 自定义异常
     */
    SettlementInfo settlement(SettlementInfo settlementInfo) throws CouponException;
}
