package com.source.coupon.feign;

import com.source.coupon.feign.hystrix.TemplateClientHystrix;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.CouponTemplateSdk;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 描述: 优惠券模板微服务feign接口定义
 *
 * @author li
 * @date 2019/12/6
 */
@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    /**
     * 查找所有可用的优惠券模板
     * @return {@link CouponTemplateSdk}s
     */
    @RequestMapping(value = "/coupon-template/template/sdk/all",method = RequestMethod.GET)
    CommonResponse<List<CouponTemplateSdk>> findAllUsableTemplate();

    /**
     * 获取模板ids到CouponTemplateSdk的映射
     * @param ids ids
     * @return map
     */
    @RequestMapping(value = "/coupon-template/template/sdk/infos",method = RequestMethod.GET)
    CommonResponse<Map<Integer,CouponTemplateSdk>> findIds2TemplateSdk(@RequestParam("ids") Collection<Integer> ids);
}
