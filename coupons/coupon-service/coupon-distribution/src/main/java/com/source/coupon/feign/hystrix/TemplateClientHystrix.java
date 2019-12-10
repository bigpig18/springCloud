package com.source.coupon.feign.hystrix;

import com.source.coupon.feign.TemplateClient;
import com.source.coupon.vo.CommonResponse;
import com.source.coupon.vo.CouponTemplateSdk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 描述: 优惠券模板feign接口熔断降级策略
 *
 * @author li
 * @date 2019/12/6
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {

    @Override
    public CommonResponse<List<CouponTemplateSdk>> findAllUsableTemplate() {
        log.error("[eureka-client-coupon-template] findAllUsableTemplate Request Error");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findAllUsableTemplate Request Error",
                Collections.emptyList());
    }

    @Override
    public CommonResponse<Map<Integer, CouponTemplateSdk>> findIds2TemplateSdk(Collection<Integer> ids) {
        log.error("[eureka-client-coupon-template] findIds2TemplateSdk Request Error");
        return new CommonResponse<>(-1,
                "[eureka-client-coupon-template] findIds2TemplateSdk Request Error",
                Collections.emptyMap());
    }
}
