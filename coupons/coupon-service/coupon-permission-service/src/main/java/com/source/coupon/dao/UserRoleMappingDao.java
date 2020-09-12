package com.source.coupon.dao;

import com.source.coupon.entity.UserRoleMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述: User-Role 表对应dao接口
 *
 * @author li
 * @date 2020/9/11
 */
public interface UserRoleMappingDao extends JpaRepository<UserRoleMapping,Integer> {

    /**
     * 查询用户角色
     * @param userId 用户id
     * @return User-Role
     */
    UserRoleMapping findByUserId(Long userId);
}
