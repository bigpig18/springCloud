package com.imooc.homepage.service.impl;

import com.imooc.homepage.CourseInfo;
import com.imooc.homepage.CourseInfosRequest;
import com.imooc.homepage.UserInfo;
import com.imooc.homepage.client.CourseClient;
import com.imooc.homepage.dao.HomePageUserCourseDao;
import com.imooc.homepage.dao.HomePageUserDao;
import com.imooc.homepage.entity.HomePageUser;
import com.imooc.homepage.entity.HomePageUserCourse;
import com.imooc.homepage.service.ImoocUserService;
import com.imooc.homepage.vo.CreateUserRequest;
import com.imooc.homepage.vo.UserCourseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户相关服务实现
 * @author li
 * @date 2019/9/5
 */
@Slf4j
@Service
public class ImoocUserServiceImpl implements ImoocUserService {

    private final HomePageUserDao userDao;

    private final HomePageUserCourseDao userCourseDao;

    private final CourseClient courseClient;

    @Autowired
    public ImoocUserServiceImpl(HomePageUserDao userDao, HomePageUserCourseDao userCourseDao, CourseClient courseClient) {
        this.userDao = userDao;
        this.userCourseDao = userCourseDao;
        this.courseClient = courseClient;
    }

    @Override
    public UserInfo createUser(CreateUserRequest request) {
        if (!request.validate()){
            return UserInfo.invalid();
        }
        HomePageUser oldUser = userDao.findByUserName(request.getUserName());
        if (null != oldUser){
            return UserInfo.invalid();
        }
        HomePageUser newUser = userDao.save(new HomePageUser(request.getUserName(),request.getEmail()));
        return new UserInfo(newUser.getId(),newUser.getUserName(),newUser.getEmail());
    }

    @Override
    public UserInfo getUserInfo(Long id) {
        Optional<HomePageUser> user = userDao.findById(id);
        if (!user.isPresent()){
            return UserInfo.invalid();
        }
        HomePageUser homePageUser = user.get();
        return new UserInfo(homePageUser.getId(),homePageUser.getUserName(),homePageUser.getEmail());
    }

    @Override
    public UserCourseInfo getUserCourseInfo(Long id) {
        Optional<HomePageUser> user = userDao.findById(id);
        if (!user.isPresent()){
            return UserCourseInfo.invalid();
        }
        HomePageUser homePageUser = user.get();
        UserInfo userInfo = new UserInfo(homePageUser.getId(),homePageUser.getUserName(),homePageUser.getEmail());
        List<HomePageUserCourse> userCourses = userCourseDao.findAllByUserId(id);
        if (CollectionUtils.isEmpty(userCourses)){
            return new UserCourseInfo(userInfo, Collections.emptyList());
        }
        List<CourseInfo> courseInfos = courseClient.getCourseInfos(new CourseInfosRequest(userCourses.stream().map(HomePageUserCourse::getCourseId).collect(Collectors.toList())));
        return new UserCourseInfo(userInfo,courseInfos);
    }
}
