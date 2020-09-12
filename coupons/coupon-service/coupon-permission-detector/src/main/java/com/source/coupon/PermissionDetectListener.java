package com.source.coupon;

import com.source.coupon.permission.PermissionClient;
import com.source.coupon.vo.PermissionInfo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 描述: 权限探测监听器,spring容器启动后自动监听
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
@Component
public class PermissionDetectListener implements ApplicationListener<ApplicationReadyEvent> {

    private static final String KEY_SERVER_CTX = "server.servlet.context-path";

    private static final String KEY_SERVER_NAME = "spring.application.name";

    @Override
    @SuppressWarnings("all")
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        // 为了不影响微服务的主线程，开启一个子线程实现注解的扫描以及权限注册
        new Thread(() -> {
            // 扫描权限
            List<PermissionInfo> infoList = scanPermission(ctx);
            // 注册权限
            registryPermission(infoList,ctx);
        }).start();
    }

    /**
     * 注册接口权限
     * @param infoList 权限信息
     * @param ctx 应用程序上下文
     */
    @SuppressWarnings("all")
    private void registryPermission(List<PermissionInfo> infoList, ApplicationContext ctx){
        log.info("************* registry permission *************");
        PermissionClient permissionClient = ctx.getBean(PermissionClient.class);
        if (null == permissionClient){
            log.error("no permissionClient bean find");
            return;
        }
        // 取出service name
        String serviceName = ctx.getEnvironment().getProperty(KEY_SERVER_NAME);
        log.info("Service Name: {}", serviceName);
        boolean result = new PermissionRegistry(permissionClient, serviceName).registry(infoList);
        if (result){
            log.info("************* registry permission over *************");
        }
    }

    /**
     * 扫描微服务中的 Controller 接口权限信息
     * @param ctx spring容器中的信息
     * @return 权限信息
     */
    private List<PermissionInfo> scanPermission(ApplicationContext ctx){
        // 取出 context 前缀
        String pathPrefix = ctx.getEnvironment().getProperty(KEY_SERVER_CTX);
        // 取出spring的映射bean
        RequestMappingHandlerMapping mappingBean = (RequestMappingHandlerMapping) ctx.getBean("requestMappingHandlerMapping");
        // 扫描权限
        List<PermissionInfo> permissionInfoList = new AnnotationScanner(pathPrefix).scanPermission(mappingBean.getHandlerMethods());
        permissionInfoList.forEach(p -> log.info("{}", p));
        log.info("{} permission found", permissionInfoList.size());
        log.info("************* done scanning *************");
        return permissionInfoList;
    }

}
