package com.source.coupon.dao;

import com.source.coupon.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 描述: path表对应的dao接口
 *
 * @author li
 * @date 2020/9/11
 */
public interface RoleDao extends JpaRepository<Role,Integer> {
}
