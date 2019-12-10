package com.source.coupon.feign;

import com.source.coupon.exception.CouponException;
import com.source.coupon.feign.hystrix.SettlementClientHystrix;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述: 优惠券结算微服务feign接口定义
 *
 * @author li
 * @date 2019/12/6
 */
@FeignClient(value = "eureka-client-coupon-settlement",fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * 优惠券规则计算
     * @param info 结算信息
     * @return {@link SettlementInfo}
     * @throws CouponException 自定义异常
     */
    @RequestMapping(value = "/coupon-settlement/settlement/compute",method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(@RequestBody SettlementInfo info) throws CouponException;
}
