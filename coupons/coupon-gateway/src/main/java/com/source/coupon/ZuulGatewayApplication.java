package com.source.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 描述: 网关应用启动入口
 * 1. @EnableZuulProxy 标识当前应用是 Zuul Server
 * 2. @SpringCloudApplication 组合注解，标识是一个springBoot应用，同时开启服务发现和熔断注解
 * @author li
 * @date 2019/10/30
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class,args);
    }
}
