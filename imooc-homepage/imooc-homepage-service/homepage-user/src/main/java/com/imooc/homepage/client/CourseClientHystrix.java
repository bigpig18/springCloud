package com.imooc.homepage.client;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 熔断降级策略
 * @author li
 * @date 2019/9/5
 */
@Component
public class CourseClientHystrix implements CourseClient {

    /**
     * 当调用接口出错，返回一个无效的课程信息
     * @param id 课程id
     * @return courseInfo.invalid
     */
    @Override
    public CourseInfo getCourseInfo(Long id) {
        return CourseInfo.invalid();
    }

    /**
     * 当接口失效，返回一个空列表
     * @param request ids
     * @return emptyList
     */
    @Override
    public List<CourseInfo> getCourseInfos(CourseInfosRequest request) {
        return Collections.emptyList();
    }
}
