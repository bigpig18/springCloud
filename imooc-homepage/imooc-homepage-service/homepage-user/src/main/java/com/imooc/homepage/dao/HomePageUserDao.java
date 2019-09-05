package com.imooc.homepage.dao;

import com.imooc.homepage.entity.HomePageUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * homePageUser 数据表访问接口定义
 * @author li
 * @date 2019/9/5
 */
public interface HomePageUserDao extends JpaRepository<HomePageUser,Long> {

    /**
     * 根据用户名寻找数据记录
     * @param userName 用户名
     * @return UserInfo
     */
    HomePageUser findByUserName(String userName);
}
