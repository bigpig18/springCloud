package com.source.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述: 用户角色枚举
 *
 * @author li
 * @date 2020/9/11
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {

    /**
     * 用户角色枚举
     */
    ADMIN("管理员"),
    SUPERADMIN("超级管理员"),
    CUSTOMER("普通用户");

    private String roleName;
}
