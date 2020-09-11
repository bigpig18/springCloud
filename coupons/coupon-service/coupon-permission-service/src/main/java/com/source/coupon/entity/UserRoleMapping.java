package com.source.coupon.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 描述: 用户角色映射关系
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_user_role_mapping")
public class UserRoleMapping {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;
    /**
     * 用户id
     */
    @Basic
    @Column(name = "user_id",nullable = false)
    private Integer userId;
    /**
     * 角色id
     */
    @Basic
    @Column(name = "role_id",nullable = false)
    private Integer roleId;

}
