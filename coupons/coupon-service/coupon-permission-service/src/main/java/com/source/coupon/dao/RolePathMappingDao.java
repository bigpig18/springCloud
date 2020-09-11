package com.source.coupon.dao;

import com.source.coupon.entity.RolePathMapping;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述: Role-Path 表对应的dao接口
 *
 * @author li
 * @date 2020/9/11
 */
public interface RolePathMappingDao extends JpaRepository<RolePathMapping,Integer> {

    /**
     * 根据 角色id + 路径id 查询数据记录
     * @param roleId 角色id
     * @param pathId 路径id
     * @return Role-Path
     */
    RolePathMapping findByRoleIdAndPathId(Integer roleId,Integer pathId);
}
