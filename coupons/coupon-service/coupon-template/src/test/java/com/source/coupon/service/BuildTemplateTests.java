package com.source.coupon.service;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.CouponCategory;
import com.source.coupon.constant.DistributeTarget;
import com.source.coupon.constant.PeriodType;
import com.source.coupon.constant.ProductLine;
import com.source.coupon.vo.TemplateRequest;
import com.source.coupon.vo.TemplateRule;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

/**
 * 描述: 构造优惠券模板测试
 *
 * @author li
 * @date 2019/11/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildTemplateTests {

    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws Exception{
        System.out.println(JSON.toJSONString(buildTemplateService.buildTemplate(fakeTemplateRequest())));
        Thread.sleep(10000);
    }

    /**
     * 伪造TemplateRequest
     * @return TemplateRequest
     */
    private TemplateRequest fakeTemplateRequest(){
        TemplateRequest request = new TemplateRequest();
        request.setName("优惠券模板-" + new Date().getTime());
        request.setLogo("pig");
        request.setDesc("优惠券模板假数据");
        request.setCategory(CouponCategory.MONEY_OFF_COUPON.getCode());
        request.setProductLine(ProductLine.HAMMER_LINE.getCode());
        request.setCount(10000);
        request.setUserId(1001L);
        request.setTarget(DistributeTarget.SINGLE.getCode());
        TemplateRule rule = new TemplateRule();
        rule.setExpiration(new TemplateRule.Expiration(PeriodType.SHIFT.getCode(),1, DateUtils.addDays(new Date(),60).getTime()));
        rule.setDiscount(new TemplateRule.Discount(5,1));
        rule.setLimitation(1);
        rule.setUsage(new TemplateRule.Usage("江西省","南昌", JSON.toJSONString(Arrays.asList("文娱","运动"))));
        rule.setWeight(JSON.toJSONString(Collections.EMPTY_LIST));
        request.setRule(rule);
        return request;
    }
}
