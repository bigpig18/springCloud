package com.source.coupon.converter;

import com.source.coupon.constant.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 描述: 优惠券分类枚举属性转换器
 * AttributeConverter<X,Y>
 * X是实体属性的类型,Y是数据库字段的类型
 * @author li
 * @date 2019/11/11
 */
@Converter
public class CouponCategoryConverter implements AttributeConverter<CouponCategory,String> {

    /**
     * 将实体属性X转换为Y 存到数据库中 (插入与更新时执行的操作)
     * @param couponCategory 实体属性
     * @return String
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    /**
     * 将数据库中字段Y 转化为实体属性X (查询操作时执行的动作)
     * @param s 数据库中字段
     * @return CouponCategory
     */
    @Override
    public CouponCategory convertToEntityAttribute(String s) {
        return CouponCategory.of(s);
    }
}
