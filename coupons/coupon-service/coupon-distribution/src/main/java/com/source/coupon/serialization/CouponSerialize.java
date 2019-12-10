package com.source.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.source.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 描述: 优惠券实体类自动义序列化器
 *
 * @author li
 * @date 2019/12/2
 */
public class CouponSerialize extends JsonSerializer<Coupon> {

    @Override
    public void serialize(Coupon coupon, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        //开始序列号
        gen.writeStartObject();
        gen.writeStringField("id",coupon.getId().toString());
        gen.writeStringField("templateId",coupon.getTemplateId().toString());
        gen.writeStringField("userId", coupon.getUserId().toString());
        gen.writeStringField("couponCode",coupon.getCouponCode());
        gen.writeStringField("assignTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(coupon.getAssignTime()));
        //给前端传送优惠券信息(名称，logo，优惠力度等)
        gen.writeStringField("name",coupon.getTemplateSdk().getName());
        gen.writeStringField("logo",coupon.getTemplateSdk().getLogo());
        gen.writeStringField("desc",coupon.getTemplateSdk().getDesc());
        gen.writeStringField("expiration", JSON.toJSONString(coupon.getTemplateSdk().getRule().getExpiration()));
        gen.writeStringField("discount",JSON.toJSONString(coupon.getTemplateSdk().getRule().getDiscount()));
        gen.writeStringField("usage",JSON.toJSONString(coupon.getTemplateSdk().getRule().getUsage()));
        //结束序列化
        gen.writeEndObject();
    }
}
