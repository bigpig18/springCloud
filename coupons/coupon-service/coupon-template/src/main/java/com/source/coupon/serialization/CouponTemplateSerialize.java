package com.source.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.source.coupon.entity.CouponTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 描述: 优惠券模板实体类自定义序列化器
 *
 * @author li
 * @date 2019/11/11
 */
public class CouponTemplateSerialize extends JsonSerializer<CouponTemplate> {

    /**
     * 序列化实体类
     * @param template 要序列化的对象
     * @param gen 生成json的生成器
     * @param serializers 序列化的工具
     * @throws IOException Io异常
     */
    @Override
    public void serialize(CouponTemplate template, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //开始序列号对象
        gen.writeStartObject();
        //将字段序列化
        gen.writeStringField("id",template.getId().toString());
        gen.writeStringField("name",template.getName());
        gen.writeStringField("logo",template.getLogo());
        gen.writeStringField("desc",template.getDesc());
        gen.writeStringField("category",template.getCategory().getDescription());
        gen.writeStringField("productLint",template.getProductLine().getDescription());
        gen.writeStringField("count",template.getCount().toString());
        gen.writeStringField("createTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(template.getCreateTime()));
        gen.writeStringField("userId",template.getUserId().toString());
        gen.writeStringField("key",template.getKey() + String.format("%04d",template.getId()));
        gen.writeStringField("target",template.getTarget().getDescription());
        gen.writeStringField("rule", JSON.toJSONString(template.getRule()));
        //结束序列号对象
        gen.writeEndObject();
    }
}
