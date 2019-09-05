package com.imooc.homepage.controller;

import com.alibaba.fastjson.JSON;
import com.imooc.homepage.UserInfo;
import com.imooc.homepage.service.ImoocUserService;
import com.imooc.homepage.vo.CreateUserRequest;
import com.imooc.homepage.vo.UserCourseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户服务对外接口
 * @author li
 * @date 2019/9/5
 */
@Slf4j
@RestController
public class HomePageUserController {

    private final ImoocUserService userService;

    @Autowired
    public HomePageUserController(ImoocUserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/create/user")
    public UserInfo createUser(@RequestBody CreateUserRequest request){
        log.info("<homepage-user> : create user --> {}", JSON.toJSONString(request));
        return userService.createUser(request);
    }

    @GetMapping(value = "/get/user")
    public UserInfo getUserInfo(Long id){
        log.info("<homepage-user> : get user --> {}",id);
        return userService.getUserInfo(id);
    }

    @GetMapping(value = "/get/user/course")
    public UserCourseInfo getUserCourseInfo(Long id){
        log.info("<homepage-user> : get user course --> {}",id);
        return userService.getUserCourseInfo(id);
    }
}
