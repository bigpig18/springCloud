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
 * 描述: 路径信息实体类
 *
 * @author li
 * @date 2020/9/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupon_path")
public class Path {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Integer id;
    /**
     * 路径模式
     */
    @Basic
    @Column(name = "path_pattern",nullable = false)
    private String pathPattern;
    /**
     * http请求类型
     */
    @Basic
    @Column(name = "http_method",nullable = false)
    private String httpMethod;
    /**
     * 路径名称
     */
    @Basic
    @Column(name = "path_name",nullable = false)
    private String pathName;
    /**
     * 服务名称
     */
    @Basic
    @Column(name = "service_name",nullable = false)
    private String serviceName;
    /**
     * 操作类型, READ/WRITE
     */
    @Basic
    @Column(name = "op_mode",nullable = false)
    private String opMode;

    /**
     * 不带主键的构造函数
     */
    public Path(String pathPattern,String httpMethod,String pathName,
            String serviceName,String opMode) {
        this.pathPattern = pathPattern;
        this.httpMethod = httpMethod;
        this.pathName = pathName;
        this.serviceName = serviceName;
        this.opMode = opMode;
    }
}
