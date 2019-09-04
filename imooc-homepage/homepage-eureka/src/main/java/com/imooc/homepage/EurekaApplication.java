package com.imooc.homepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 只需要使用@EnableEurekaServer注解让应用变为Eureka Server
 * pom.xml文件中对应到 spring-cloud-starter-netflix-eureka-server
 * @author li
 * @date 2019/9/4
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class,args);
    }
}
