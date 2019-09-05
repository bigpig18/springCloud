package com.imooc.homepage.service;

import com.imooc.homepage.UserInfo;
import com.imooc.homepage.vo.CreateUserRequest;
import com.imooc.homepage.vo.UserCourseInfo;

/**
 * 用户相关服务接口定义
 * @author li
 * @date 2019/9/5
 */
public interface ImoocUserService {

    /**
     * 创建用户
     * @param request 用户相关信息
     * @return userInfo
     */
   UserInfo createUser(CreateUserRequest request);

    /**
     * 根据用户id获取用户信息
     * @param id 用户id
     * @return userInfo
     */
   UserInfo getUserInfo(Long id);

    /**
     * 获取用户和课程信息
     * @param id 用户id
     * @return UserCourseInfo
     */
   UserCourseInfo getUserCourseInfo(Long id);
}
