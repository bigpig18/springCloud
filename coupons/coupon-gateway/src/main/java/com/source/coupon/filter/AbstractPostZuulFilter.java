package com.source.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * 描述: 通用 PostZuulFilter
 *
 * @author li
 * @date 2019/10/31
 */
public abstract class AbstractPostZuulFilter extends AbstractZuulFilter{

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
