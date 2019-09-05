package com.imooc.homepage.client;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 通过 Feign 访问course 微服务
 * @author li
 * @date 2019/9/5
 */
@FeignClient(value = "eureka-client-homepage-course",fallback = CourseClientHystrix.class)
public interface CourseClient {

    /**
     * 通过id获取课程信息
     * @param id 课程id
     * @return courseInfo
     */
    @RequestMapping(value = "/homepage-course/get/course",method = RequestMethod.GET)
    CourseInfo getCourseInfo(Long id);

    /**
     * 获取课程信息
     * @param request ids
     * @return list
     */
    @RequestMapping(value = "/homepage-course/get/courses",method = RequestMethod.POST)
    List<CourseInfo> getCourseInfos(@RequestBody CourseInfosRequest request);
}
