package com.imooc.homepage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Springboot 程序启动入口
 * @author li
 * @date 2019/9/5
 */
@EnableJpaAuditing
@EnableEurekaClient
@SpringBootApplication
public class HomePageCourseApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomePageCourseApplication.class,args);
    }
}
