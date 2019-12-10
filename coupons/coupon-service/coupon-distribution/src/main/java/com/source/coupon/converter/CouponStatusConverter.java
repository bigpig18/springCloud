package com.source.coupon.converter;

import com.source.coupon.constant.CouponStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 描述: 优惠券状态枚举属性转换器
 *
 * @author li
 * @date 2019/12/2
 */
@Converter
public class CouponStatusConverter implements AttributeConverter<CouponStatus,Integer> {

    @Override
    public Integer convertToDatabaseColumn(CouponStatus couponStatus) {
        return couponStatus.getCode();
    }

    @Override
    public CouponStatus convertToEntityAttribute(Integer code) {
        return CouponStatus.of(code);
    }
}
