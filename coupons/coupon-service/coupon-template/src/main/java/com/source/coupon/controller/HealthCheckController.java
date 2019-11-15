package com.source.coupon.controller;

import com.source.coupon.exception.CouponException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述: 健康检查接口
 *
 * @author li
 * @date 2019/11/15
 */
@Slf4j
@RestController
public class HealthCheckController {

    /**
     * 服务发现客户端
     */
    private final DiscoveryClient discoveryClient;
    /**
     * 服务注册接口，提供获取服务id的方法
     */
    @Resource
    private Registration registration;

    @Autowired
    public HealthCheckController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * 健康检查接口
     * @return String
     */
    @GetMapping("/health")
    public String health(){
        log.debug("View Health Api");
        return "CouponTemplate is ok...";
    }

    /**
     * 异常测试接口(测试统一异常处理是否好用)
     * @return String
     */
    @GetMapping("/exception")
    public String exception() throws CouponException {
        log.debug("View Exception Api");
        throw new CouponException("CouponTemplate Has Some Problem");
    }

    /**
     * 获取eurekaServer 上的微服务元信息
     * @return list
     */
    @GetMapping("/info")
    public List<Map<String,Object>> info(){
        // 大约需要等待两分钟左右 才能获取到注册信息
        List<ServiceInstance> instances = discoveryClient.getInstances(registration.getServiceId());
        List<Map<String,Object>> result = new ArrayList<>(instances.size());
        instances.forEach(i -> {
            Map<String,Object> info = new HashMap<>(16);
            info.put("serviceId",i.getServiceId());
            info.put("instanceId",i.getInstanceId());
            info.put("port",i.getPort());
            info.put("hostname",i.getHost());
            result.add(info);
        });
        return result;
    }
}
