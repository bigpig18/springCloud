package com.source.coupon.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: 限流过滤器
 *
 * @author li
 * @date 2019/10/31
 */
@Slf4j
@Component
public class RateLimiterFilter extends AbstractPreZuulFilter{

    /**
     * 每秒可以获取到两个令牌
     * 每秒只允许两个请求过来
     */
    private RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        //尝试获取令牌
        if (rateLimiter.tryAcquire()){
            log.info("get rate token success");
            return success();
        }else {
            log.error("rate limit:{}",request.getRequestURI());
            return fail(402,"error: rate limit");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
