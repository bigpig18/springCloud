package com.source.coupon.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 描述: token校验过滤器
 *
 * @author li
 * @date 2019/10/31
 */
@Slf4j
@Component
public class TokenFilter extends AbstractPreZuulFilter{

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",request.getMethod(), request.getRequestURI()));
        Object token = request.getParameter("token");
        if (null == token){
            log.error("error: token is empty");
            return fail(401,"error: token is empty");
        }
        return success();
    }

    @Override
    public int filterOrder() {
        return 1;
    }
}
