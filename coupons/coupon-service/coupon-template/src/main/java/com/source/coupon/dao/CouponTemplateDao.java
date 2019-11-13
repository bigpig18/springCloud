package com.source.coupon.dao;

import com.source.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 描述: CouponTemplate DAO 接口定义
 *
 * @author li
 * @date 2019/11/12
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate,Integer> {

    /**
     * 通过模板名称查询模板
     * @param name 模板名称
     * @return CouponTemplate
     */
    CouponTemplate findByName(String name);

    /**
     * 查询未过期且可用的模板
     * @param available 是否可用
     * @param expired 是否过期
     * @return list
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available,Boolean expired);

    /**
     * 根据expired标记查找模板记录
     * @param expired 是否过期
     * @return list
     */
    List<CouponTemplate> findAllByExpired(Boolean expired);
}
