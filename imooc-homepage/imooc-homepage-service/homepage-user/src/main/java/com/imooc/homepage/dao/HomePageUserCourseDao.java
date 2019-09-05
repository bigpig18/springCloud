package com.imooc.homepage.dao;

import com.imooc.homepage.entity.HomePageUserCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * HomePageUserCourse数据表访问接口定义
 * @author li
 * @date 2019/9/5
 */
public interface HomePageUserCourseDao extends JpaRepository<HomePageUserCourse,Long> {

    /**
     * 通过用户id寻找数据记录
     * @param userId 用户id
     * @return list
     */
    List<HomePageUserCourse> findAllByUserId(Long userId);
}
