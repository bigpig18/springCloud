package com.source.coupon.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * 描述: 定制 http 消息转换器
 *
 * @author li
 * @date 2019/10/31
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        //先清空
        converters.clear();
        //将java实体对象转换成http数据输出流
        //这里可以将Java对象转换成Application/json
        converters.add(new MappingJackson2HttpMessageConverter());
    }
}
