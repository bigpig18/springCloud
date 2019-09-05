package com.imooc.homepage.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import com.imooc.homepage.service.ImoocCourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程对外服务接口
 * @author li
 * @date 2019/9/5
 */
@Slf4j
@RestController
public class HomePageCourseController {

    private final ImoocCourseService imoocCourseService;

    @Autowired
    public HomePageCourseController(ImoocCourseService imoocCourseService) {
        this.imoocCourseService = imoocCourseService;
    }

    @GetMapping(value = "/get/course")
    public CourseInfo getCourseInfo(Long id){
        log.info("<homepage-course> : get course --> {}",id);
        return imoocCourseService.getCourseInfo(id);
    }

    @PostMapping(value = "/get/courses")
    public List<CourseInfo> getCourseInfos(@RequestBody CourseInfosRequest request){
        log.info("<homepage-course> : get courses --> {}", JSON.toJSONString(request));
        return imoocCourseService.getCourseInfos(request);
    }
}
