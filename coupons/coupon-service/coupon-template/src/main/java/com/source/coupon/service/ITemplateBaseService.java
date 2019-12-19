package com.source.coupon.service;

import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.exception.CouponException;
import com.source.coupon.vo.CouponTemplateSdk;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 描述: 优惠券模板基础服务(select,delete ...)定义
 *
 * @author li
 * @date 2019/11/13
 */
public interface ITemplateBaseService {

    /**
     * 根据优惠券模板id获取信息
     * @param id 模板id
     * @return {@link CouponTemplate} 优惠券模板实体
     * @throws CouponException 自定义异常
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    /**
     * 查询所有可用的优惠券模板
     * @return {@link CouponTemplateSdk}s
     */
    List<CouponTemplateSdk> findAllUsableTemplate();

    /**
     * 获取模板ids 到 CouponTemplateSdk 的映射
     * @param ids 模板的ids
     * @return Map<key: 模板id,value: CouponTemplateSdk>
     */
    Map<Integer,CouponTemplateSdk> findIds2TemplateSdk(Collection<Integer> ids);
}
