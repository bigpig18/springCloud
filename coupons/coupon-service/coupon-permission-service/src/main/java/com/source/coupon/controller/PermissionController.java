package com.source.coupon.controller;

import com.source.coupon.annotation.IgnoreResponseAdvice;
import com.source.coupon.service.PathService;
import com.source.coupon.service.PermissionService;
import com.source.coupon.vo.CheckPermissionRequest;
import com.source.coupon.vo.CreatePathRequest;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 描述: 路径创建与权限校验对外接口
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
@RestController
public class PermissionController {

    private final PathService pathService;

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PathService pathService, PermissionService permissionService) {
        this.pathService = pathService;
        this.permissionService = permissionService;
    }

    /**
     * 路径创建接口
     * @param request {@link CreatePathRequest} 路径创建参数
     * @return 存入参数
     */
    @PostMapping("/create/path")
    public List<Integer> createPath(@RequestBody CreatePathRequest request){
        log.info("createPath: {}",request.getPathInfos().size());
        return pathService.createPath(request);
    }

    /**
     * 权限校验接口
     * @param request {@link CheckPermissionRequest}
     * @return 是否有权限
     */
    @IgnoreResponseAdvice
    @PostMapping("/check/permission")
    public Boolean checkPermission(@RequestBody CheckPermissionRequest request){
        log.info("checkPermission for args: {}, {}, {}",request.getUserId(),request.getUri(),request.getHttpMethod());
        return permissionService.checkPermission(request.getUserId(),request.getUri(),request.getHttpMethod());
    }

}
