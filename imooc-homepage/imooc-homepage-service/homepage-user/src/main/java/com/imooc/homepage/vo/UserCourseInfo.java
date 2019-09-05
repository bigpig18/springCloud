package com.imooc.homepage.vo;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 用户课程形象对象定义
 * @author li
 * @date 2019/9/5
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseInfo {

    private UserInfo userInfo;
    private List<CourseInfo> courseInfos;

    public static UserCourseInfo invalid(){
        return new UserCourseInfo(UserInfo.invalid(), Collections.emptyList());
    }
}
