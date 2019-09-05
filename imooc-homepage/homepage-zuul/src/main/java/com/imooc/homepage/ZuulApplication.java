package com.imooc.homepage;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 网关应用程序
 * EnableZuulProxy: 标识当前应用是 Zuul Server
 * SpringCloudApplication: 用于简化配置的组合注解
 * @author li
 * @date 2019/9/5
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class,args);
    }
}
