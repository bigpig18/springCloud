package com.source.coupon.service;

import com.alibaba.fastjson.JSON;
import com.source.coupon.exception.CouponException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * 描述: 优惠券模板基础服务测试
 *
 * @author li
 * @date 2019/11/28
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TemplateBaseTest {

    @Autowired
    private ITemplateBaseService baseService;

    @Test
    public void testBuildTemplateInfo() throws CouponException {
        System.out.println(JSON.toJSONString(baseService.buildTemplateInfo(2),true));
        System.out.println(JSON.toJSONString(baseService.buildTemplateInfo(7),true));
    }

    @Test
    public void testFindAllUsableTemplate(){
        System.out.println(JSON.toJSONString(baseService.findAllUsableTemplate(),true));
    }

    @Test
    public void testFindIds2TemplateSdk(){
        System.out.println(JSON.toJSONString(baseService.findIds2TemplateSdk(Arrays.asList(1,2,3,4,5,6,7)),true));
    }
}
