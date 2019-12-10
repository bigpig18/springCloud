package com.source.coupon.vo;

import com.source.coupon.constant.CouponStatus;
import com.source.coupon.constant.PeriodType;
import com.source.coupon.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 根据优惠券状态，分类用户优惠券
 *
 * @author li
 * @date 2019/12/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponClassify {

    /**
     * 可用优惠券
     */
    private List<Coupon> usable;
    /**
     * 已使用的
     */
    private List<Coupon> used;
    /**
     * 已过期的
     */
    private List<Coupon> expired;

    /**
     * 对当前优惠券进行分类
     * @param coupons 传入的优惠券信息
     * @return 分类后的优惠券
     */
    public static CouponClassify classify(List<Coupon> coupons){
        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());
        coupons.forEach(c -> {
            boolean isTimeExpired;
            long curTime = System.currentTimeMillis();
            if (c.getTemplateSdk().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())){
                isTimeExpired = c.getTemplateSdk().getRule().getExpiration().getDeadline() <= curTime;
            }else{
                isTimeExpired = DateUtils.addDays(c.getAssignTime(),c.getTemplateSdk().getRule().getExpiration().getGap()).getTime() <= curTime;
            }
            if (c.getStatus() == CouponStatus.USED){
                used.add(c);
            }else if (c.getStatus() == CouponStatus.EXPIRED || isTimeExpired){
                expired.add(c);
            }else{
                usable.add(c);
            }
        });
        return new CouponClassify(usable,used,expired);
    }
}
