package com.source.coupon.vo;

import lombok.Data;

/**
 * 描述: 接口权限信息组装类定义
 *
 * @author li
 * @date 2020/9/12
 */
@Data
public class PermissionInfo {

    /**
     * controller接口的url
     */
    private String url;
    /**
     * 方法类型
     */
    private String method;
    /**
     * 是否只读
     */
    private Boolean isRead;
    /**
     * 方法描述信息
     */
    private String description;
    /**
     * 扩展信息
     */
    private String extra;

    @Override
    public String toString() {
        return  "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", isRead=" + isRead +
                ", description='" + description + '\'';
    }
}
