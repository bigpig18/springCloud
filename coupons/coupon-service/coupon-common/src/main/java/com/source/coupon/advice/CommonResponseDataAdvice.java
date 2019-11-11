package com.source.coupon.advice;

import com.source.coupon.annotation.IgnoreResponseAdvice;
import com.source.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 描述: 统一响应
 *
 * @author li
 * @date 2019/10/31
 */
@RestControllerAdvice
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 判断是否需要对响应进行处理
     * @param methodParameter 当前controller的定义
     * @param aClass 消息转换器
     * @return boolean
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        //如果当前方法所在的类标识了 @IgnoreResponseAdvice 则不需要处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        //如果当前方法标识了 @IgnoreResponseAdvice 则不需要处理
        if (methodParameter.getMethod().isAnnotationPresent(IgnoreResponseAdvice.class)){
            return false;
        }
        //对响应进行处理，执行 beforeBodyWrite()方法
        return true;
    }

    /**
     * 在响应返回之前的处理
     */
    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        //定义最终的方位对象
        CommonResponse<Object> response = new CommonResponse<>(0,"");
        //如果 o 是 null ，response不需要设置 data
        if (null == o){
            return response;
        }else if (o instanceof CommonResponse){
            //如果当前的o 已经是CommonResponse类型，就不需要再次处理
            response = (CommonResponse<Object>) o;
        }else{
            //否则就把响应对象作为CommonResponse的data部分
            response.setData(o);
        }
        return response;
    }
}
