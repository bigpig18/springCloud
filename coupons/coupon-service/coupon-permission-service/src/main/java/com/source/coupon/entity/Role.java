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
 * 描述: 用户角色实体类
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_role")
public class Role {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;
    /**
     * 角色名称
     */
    @Basic
    @Column(name = "role_name",nullable = false)
    private String roleName;
    /**
     * 角色标识
     */
    @Basic
    @Column(name = "role_tag",nullable = false)
    private String roleTag;
}
