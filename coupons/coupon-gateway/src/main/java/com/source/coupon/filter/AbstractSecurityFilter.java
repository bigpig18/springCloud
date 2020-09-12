package com.source.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;

/**
 * 描述: 抽象权限过滤器
 *
 * @author li
 * @date 2020/9/12
 */
@Slf4j
public abstract class AbstractSecurityFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletResponse response = context.getResponse();
        // 如果前一个filter执行失败，不会去调用后面的filter
        return response.getStatus() == 0 || response.getStatus() == HttpStatus.OK.value();
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        HttpServletResponse response = context.getResponse();
        log.info("filter {} begin check request {}",this.getClass().getSimpleName(),request.getRequestURI());
        Boolean result = null;
        try {
            result = interceptCheck(request,response);
        } catch (Exception e){
            log.error("filter {} check request {}, throw Exception: {}",
                    this.getClass().getSimpleName(),request.getRequestURI(),e.getMessage());
        }
        log.info("Filter {} finish check, result: {}",this.getClass().getSimpleName(),result);
        if (result == null){
            log.debug("Filter {} finish check,result is null",this.getClass().getSimpleName());
            // 对当前的请求不进行路由
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(getHttpStatus());
            return null;
        }
        if (!result){
            try {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(getHttpStatus());
                response.setHeader("Content-Type","application/json,charset=UTF-8");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(getErrorMsg());
                context.setResponse(response);
            } catch (IOException e){
                log.error("filter {} check request {},result is false,set response throws Exception: {}",
                        this.getClass().getSimpleName(),
                        request.getRequestURI(),e.getMessage());
            }
        }
        return null;
    }

    /**
     * 子filter实现改方法，填充校验逻辑
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return 校验是否通过
     * @throws Exception 捕获异常
     */
    protected abstract Boolean interceptCheck(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 返回http状态码
     * @return httpStatus
     */
    protected abstract int getHttpStatus();

    /**
     * 返回错误信息
     * @return errorMsg
     */
    protected abstract String getErrorMsg();
}
