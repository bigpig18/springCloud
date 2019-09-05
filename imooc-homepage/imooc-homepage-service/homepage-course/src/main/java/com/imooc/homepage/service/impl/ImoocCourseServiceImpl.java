package com.imooc.homepage.service.impl;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import com.imooc.homepage.dao.HomePageCourseDao;
import com.imooc.homepage.entity.HomePageCourse;
import com.imooc.homepage.service.ImoocCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 课程服务功能实现
 * @author li
 * @date 2019/9/5
 */
@Slf4j
@Service
public class ImoocCourseServiceImpl implements ImoocCourseService {

    private final HomePageCourseDao homePageCourseDao;

    @Autowired
    public ImoocCourseServiceImpl(HomePageCourseDao homePageCourseDao) {
        this.homePageCourseDao = homePageCourseDao;
    }

    @Override
    public CourseInfo getCourseInfo(Long id) {
        Optional<HomePageCourse> course = homePageCourseDao.findById(id);
        return buildCourseInfo(course.orElse(HomePageCourse.invalid()));
    }

    @Override
    public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
        if (CollectionUtils.isEmpty(request.getIds())){
            return Collections.emptyList();
        }
        List<HomePageCourse> list = homePageCourseDao.findAllById(request.getIds());
        return list.stream().map(this::buildCourseInfo).collect(Collectors.toList());
    }

    /**
     * 根据数据记录构造对象信息
     * @param course course
     * @return courseInfo
     */
    private CourseInfo buildCourseInfo(HomePageCourse course){
        return CourseInfo.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseType(course.getCourseType() == 0 ? "免费课":"实战课")
                .courseIcon(course.getCourseIcon())
                .courseIntro(course.getCourseIntro())
                .build();
    }
}
