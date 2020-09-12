package com.source.coupon.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述: 权限描述注解(定义controller接口权限)
 *
 * @author li
 * @date 2020/9/12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CouponPermission {

    /**
     * 接口描述信息
     * @return 接口描述
     */
    String description() default "";

    /**
     * 接口是否为只读
     * @return 默认true
     */
    boolean readOnly() default true;

    /**
     * 扩展属性
     * @return 最好以json格式存储
     */
    String extra() default "";
}
