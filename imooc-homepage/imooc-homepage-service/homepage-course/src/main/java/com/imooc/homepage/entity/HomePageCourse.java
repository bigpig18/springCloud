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
 * 映射实体表定义
 * @author li
 * @date 2019/9/5
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "homepage_course")
public class HomePageCourse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Basic
    @Column(name = "course_name",nullable = false)
    private String courseName;

    @Basic
    @Column(name = "course_type",nullable = false)
    private Integer courseType;

    @Basic
    @Column(name = "course_icon",nullable = false)
    private String courseIcon;

    @Basic
    @Column(name = "course_intro",nullable = false)
    private String courseIntro;

    @Basic
    @Column(name = "createTime",nullable = false)
    @CreatedDate
    private Date createTime;

    @Basic
    @Column(name = "updateTime",nullable = false)
    @LastModifiedDate
    private Date updateTime;

    public HomePageCourse(String courseName,Integer courseType,String courseIcon,String courseIntro){
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseIcon = courseIcon;
        this.courseIntro = courseIntro;
    }

    public static HomePageCourse invalid(){
        HomePageCourse course = new HomePageCourse("",0,"","");
        course.setId(-1L);
        return course;
    }
}