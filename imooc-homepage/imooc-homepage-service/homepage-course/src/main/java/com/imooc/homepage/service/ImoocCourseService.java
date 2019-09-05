package com.imooc.homepage.service;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;

import java.util.List;

/**
 * 课程相关服务接口定义
 * @author li
 * @date 2019/9/5
 */
public interface ImoocCourseService {

    /**
     * 通过id获取课程信息
     * @param id 课程id
     * @return 课程信息
     */
    CourseInfo getCourseInfo(Long id);

    /**
     * 通过ids 获取课程信息
     * @param request ids
     * @return list
     */
    List<CourseInfo> getCourseInfos(CourseInfosRequest request);
}
