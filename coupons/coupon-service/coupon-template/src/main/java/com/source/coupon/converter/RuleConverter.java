package com.source.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.source.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * 描述: 规则转换器
 *
 * @author li
 * @date 2019/11/11
 */
@Converter
public class RuleConverter implements AttributeConverter<TemplateRule,String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        return JSON.parseObject(s,TemplateRule.class);
    }
}
