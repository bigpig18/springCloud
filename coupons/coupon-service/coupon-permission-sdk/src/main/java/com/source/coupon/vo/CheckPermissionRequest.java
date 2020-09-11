package com.source.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 权限校验请求对象定义
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckPermissionRequest {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 请求路径uri
     */
    private String uri;
    /**
     * http请求类型
     */
    private String httpMethod;

}
