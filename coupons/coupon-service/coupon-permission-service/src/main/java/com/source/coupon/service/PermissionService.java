package com.source.coupon.service;

import com.source.coupon.constant.RoleEnum;
import com.source.coupon.dao.PathDao;
import com.source.coupon.dao.RoleDao;
import com.source.coupon.dao.RolePathMappingDao;
import com.source.coupon.dao.UserRoleMappingDao;
import com.source.coupon.entity.Path;
import com.source.coupon.entity.Role;
import com.source.coupon.entity.RolePathMapping;
import com.source.coupon.entity.UserRoleMapping;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 权限校验功能实现
 *
 * @author li
 * @date 2020/9/11
 */
@Slf4j
@Service
public class PermissionService {

    private final PathDao pathDao;

    private final RoleDao roleDao;

    private final UserRoleMappingDao userRoleMappingDao;

    private final RolePathMappingDao rolePathMappingDao;

    @Autowired
    public PermissionService(PathDao pathDao, RoleDao roleDao,
            UserRoleMappingDao userRoleMappingDao,
            RolePathMappingDao rolePathMappingDao) {
        this.pathDao = pathDao;
        this.roleDao = roleDao;
        this.userRoleMappingDao = userRoleMappingDao;
        this.rolePathMappingDao = rolePathMappingDao;
    }

    /**
     * 用户访问接口权限校验
     * @param userId 用户id
     * @param uri 访问的uri
     * @param httpMethod 方法的请求类型
     * @return 是否有权限
     */
    public Boolean checkPermission(Long userId, String uri, String httpMethod){
        UserRoleMapping userRoleMapping = userRoleMappingDao.findByUserId(userId);
        // 若用户角色映射表找不到记录，直接返回false
        if (null == userRoleMapping){
            log.error("userId not exist in UserRoleMapping: {}",userId);
            return false;
        }
        // 若找不到对应的角色信息，直接返回false
        Optional<Role> role = roleDao.findById(userRoleMapping.getRoleId());
        if (!role.isPresent()) {
            log.error("roleId not exist in UserRoleMapping: {}",userRoleMapping.getRoleId());
            return false;
        }
        // 若用户角色是超级管理员，直接返回true
        if (role.get().getRoleTag().equals(RoleEnum.SUPERADMIN.name())){
            return true;
        }
        // 确定唯一的一条path记录
        Path path = pathDao.findByPathPatternAndHttpMethod(uri,httpMethod);
        // 若路径未注册(忽略了),直接返回true
        if (null == path) {
            return true;
        }
        RolePathMapping rolePathMapping = rolePathMappingDao.findByRoleIdAndPathId(role.get().getId(),path.getId());
        return rolePathMapping != null;
    }
}
