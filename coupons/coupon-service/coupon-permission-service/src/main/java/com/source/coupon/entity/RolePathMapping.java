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
 * 描述: 角色与路径的映射关系的实体类
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_role_path_mapping")
public class RolePathMapping {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;
    /**
     * 角色id
     */
    @Basic
    @Column(name = "role_id",nullable = false)
    private Integer roleId;
    /**
     * 路径id
     */
    @Basic
    @Column(name = "path_id",nullable = false)
    private Integer pathId;
}
