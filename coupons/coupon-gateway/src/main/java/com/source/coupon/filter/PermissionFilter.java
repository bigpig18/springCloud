package com.source.coupon.filter;

import com.alibaba.fastjson.JSON;
import com.source.coupon.permission.PermissionClient;
import com.source.coupon.vo.CheckPermissionRequest;
import com.source.coupon.vo.CommonResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 描述: 权限过滤器实现
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
@Component
public class PermissionFilter extends AbstractSecurityFilter {

    private final PermissionClient permissionClient;

    @Autowired
    public PermissionFilter(PermissionClient permissionClient) {
        this.permissionClient = permissionClient;
    }

    @Override
    protected Boolean interceptCheck(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 执行权限校验
        // 从Header中获取userId
        Long userId = Long.valueOf(request.getHeader("userId"));
        String uri = request.getRequestURI().substring("/coupon".length());
        String httpMethod = request.getMethod();
        return permissionClient.checkPermission(new CheckPermissionRequest(userId,uri,httpMethod));
    }

    @Override
    protected int getHttpStatus() {
        return HttpStatus.OK.value();
    }

    @Override
    protected String getErrorMsg() {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setCode(-1);
        response.setMessage("No Authority");
        return JSON.toJSONString(response);
    }
}
