package com.source.coupon.feign.hystrix;

import com.source.coupon.exception.CouponException;
import com.source.coupon.feign.SettlementClient;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 描述: 结算微服务feign接口熔断降级策略
 *
 * @author li
 * @date 2019/12/6
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {

    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo info) throws CouponException {
        log.error("[eureka-client-coupon-settlement] computeRule Request Error");
        info.setEmploy(false);
        info.setCost(-1.0);
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-settlement] computeRule Request Error",
                info);
    }
}
