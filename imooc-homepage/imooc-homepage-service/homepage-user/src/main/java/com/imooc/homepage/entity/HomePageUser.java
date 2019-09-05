package com.imooc.homepage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * homepage_user 表对应的实体类定义
 * @author li
 * @date 2019/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "homepage_user")
public class HomePageUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Basic
    @Column(name = "username",nullable = false)
    private String userName;

    @Basic
    @Column(name = "email",nullable = false)
    private String email;

    @Basic
    @CreatedDate
    @Column(name = "createTime",nullable = false)
    private Date createTime;

    @Basic
    @LastModifiedDate
    @Column(name = "updateTime",nullable = false)
    private Date updateTime;

    public HomePageUser(String userName,String email){
        this.userName = userName;
        this.email = email;
    }
}
