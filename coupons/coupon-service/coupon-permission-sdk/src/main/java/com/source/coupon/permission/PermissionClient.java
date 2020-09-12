package com.source.coupon.permission;

import com.source.coupon.vo.CheckPermissionRequest;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.CreatePathRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 路径创建与权限校验功能feign接口实现
 *
 * @author li
 * @date 2020/9/12
 */
@FeignClient(value = "eureka-client-coupon-permission")
public interface PermissionClient {

    /**
     * 路径创建接口
     * @param request {@link CreatePathRequest}
     * @return 返回的路径id
     */
    @RequestMapping(value = "/coupon-permission/create/path",method = RequestMethod.POST)
    CommonResponse<List<Integer>> createPath(@RequestBody CreatePathRequest request);

    /**
     * 权限校验接口
     * @param request {@link CheckPermissionRequest}
     * @return 是否有权限
     */
    @RequestMapping(value = "/coupon-permission/check/permission")
    Boolean checkPermission(@RequestBody CheckPermissionRequest request);
}
