package com.source.coupon.service;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.CouponStatus;
import com.source.coupon.exception.CouponException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * 描述: 用户服务功能测试用例
 *
 * @author li
 * @date 2019/12/10
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    /**
     * userId 假数据
     */
    private Long userId = 2001L;

    @Autowired
    private IUserService userService;

    @Test
    public void testFindCouponByStatus() throws CouponException {
        System.out.println(JSON.toJSONString(userService.findCouponsByStatus(userId, CouponStatus.USABLE.getCode()),true));
    }

    @Test
    public void testFindAvailableTemplate() throws CouponException {
        System.out.println(JSON.toJSONString(userService.findAvailableTemplate(userId),true));
    }
}
