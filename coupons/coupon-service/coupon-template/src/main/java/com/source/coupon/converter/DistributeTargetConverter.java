package com.source.coupon.converter;

import com.source.coupon.constant.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 描述: 分发目标枚举属性转换器
 *
 * @author li
 * @date 2019/11/11
 */
@Converter
public class DistributeTargetConverter implements AttributeConverter<DistributeTarget,Integer> {


    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer code) {
        return DistributeTarget.of(code);
    }
}
