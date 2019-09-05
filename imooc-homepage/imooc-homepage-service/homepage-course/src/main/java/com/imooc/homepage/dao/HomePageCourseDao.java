package com.imooc.homepage.dao;

import com.imooc.homepage.entity.HomePageCourse;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author li
 * @date 2019/9/5
 */

public interface HomePageCourseDao extends JpaRepository<HomePageCourse,Long> {

}
