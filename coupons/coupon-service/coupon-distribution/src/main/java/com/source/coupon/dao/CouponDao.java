package com.source.coupon.dao;

import com.source.coupon.constant.CouponStatus;
import com.source.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 描述: 优惠券dao接口定义
 *
 * @author li
 * @date 2019/12/2
 */
public interface CouponDao extends JpaRepository<Coupon,Integer> {

    /**
     * 根据userId + 状态 查询优惠券记录
     * @param userId 用户id
     * @param status {@link CouponStatus}优惠券状态
     * @return list
     */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
