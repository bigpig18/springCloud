package com.source.coupon.converter;

import com.source.coupon.constant.ProductLine;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 描述: 产品线枚举属性转换器
 *
 * @author li
 * @date 2019/11/11
 */
@Converter
public class ProductLineConverter implements AttributeConverter<ProductLine,Integer> {

    @Override
    public Integer convertToDatabaseColumn(ProductLine productLine) {
        return productLine.getCode();
    }

    @Override
    public ProductLine convertToEntityAttribute(Integer code) {
        return ProductLine.of(code);
    }
}
