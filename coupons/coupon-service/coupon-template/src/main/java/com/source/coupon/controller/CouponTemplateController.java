package com.source.coupon.controller;

import com.alibaba.fastjson.JSON;
import com.source.coupon.annotation.CouponPermission;
import com.source.coupon.annotation.IgnorePermission;
import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.exception.CouponException;
import com.source.coupon.service.IBuildTemplateService;
import com.source.coupon.service.ITemplateBaseService;
import com.source.coupon.vo.CouponTemplateSdk;
import com.source.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 描述: 优惠券模板相关的功能控制器
 *
 * @author li
 * @date 2019/11/15
 */
@Slf4j
@RestController
public class CouponTemplateController {

    /**
     * 构建优惠券模板服务
     */
    private final IBuildTemplateService templateService;
    /**
     * 优惠券模板基础服务
     */
    private final ITemplateBaseService baseService;

    @Autowired
    public CouponTemplateController(IBuildTemplateService templateService, ITemplateBaseService baseService) {
        this.templateService = templateService;
        this.baseService = baseService;
    }

    /**
     * 创建优惠券模板
     * 127.0.0.1:7001/coupon-template/template/build
     * 127.0.0.1:9000/coupon/coupon-template/template/build
     * @param request {@link TemplateRequest} 优惠券请求模板
     * @return {@link CouponTemplate}优惠券模板信息
     * @throws CouponException {@link CouponException} 自定义异常
     */
    @CouponPermission(description = "buildTemplate",readOnly = false)
    @PostMapping(value = "/template/build")
    public CouponTemplate buildTemplate(@RequestBody TemplateRequest request)throws CouponException{
        log.info("Build Template: {}", JSON.toJSONString(request));
        return templateService.buildTemplate(request);
    }

    /**
     * 构造优惠券模板详情
     * 127.0.0.1:7001/coupon-template/template/info
     * 127.0.0.1:9000/coupon/coupon-template/template/info
     * @param id id
     * @return {@link CouponTemplate}优惠券模板信息
     * @throws CouponException {@link CouponException} 自定义异常
     */
    @CouponPermission(description = "buildTemplateInfo")
    @GetMapping(value = "/template/info")
    public CouponTemplate buildTemplateInfo(@RequestParam("id") Integer id)throws CouponException{
        log.info("Build Template Info For: {}",id);
        return baseService.buildTemplateInfo(id);
    }

    /**
     * 查询所有可用的优惠券模板
     * 127.0.0.1:7001/coupon-template/template/sdk/all
     * 127.0.0.1:9000/coupon/coupon-template/template/sdk/all
     * @return list
     */
    @IgnorePermission
    @GetMapping(value = "/template/sdk/all")
    public List<CouponTemplateSdk> findAllUsableTemplate(){
        log.info("Find All Usable Template");
        return baseService.findAllUsableTemplate();
    }

    /**
     * 获取模板ids到CouponTemplateSdk的映射
     * 127.0.0.1:7001/coupon-template/template/sdk/infos
     * 127.0.0.1:9000/coupon/coupon-template/template/sdk/infos
     * @param ids id
     * @return map
     */
    @GetMapping(value = "template/sdk/infos")
    public Map<Integer,CouponTemplateSdk> findIds2TemplateSdk(@RequestParam("ids") Collection<Integer> ids){
        log.info("Find Ids To TemplateSdk: {}",JSON.toJSONString(ids));
        return baseService.findIds2TemplateSdk(ids);
    }
}
