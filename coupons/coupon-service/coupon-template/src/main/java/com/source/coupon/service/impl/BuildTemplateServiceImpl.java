package com.source.coupon.service.impl;

import com.source.coupon.dao.CouponTemplateDao;
import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.exception.CouponException;
import com.source.coupon.service.IAsyncService;
import com.source.coupon.service.IBuildTemplateService;
import com.source.coupon.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述: 构建优惠券模板接口实现
 *
 * @author li
 * @date 2019/11/14
 */
@Slf4j
@Service
public class BuildTemplateServiceImpl implements IBuildTemplateService {

    private final IAsyncService asyncService;

    private final CouponTemplateDao templateDao;

    @Autowired
    public BuildTemplateServiceImpl(IAsyncService asyncService, CouponTemplateDao templateDao) {
        this.asyncService = asyncService;
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplate(TemplateRequest request) throws CouponException {
        //参数合法校验
        if (!request.validate()){
            throw new CouponException("BuildTemplate Param Is Not Validate!");
        }
        //判断同名的优惠券模板是否存在
        if (null != templateDao.findByName(request.getName())){
            throw new CouponException("Exist Same Name Template");
        }
        //构造Template并保存到数据库中
        CouponTemplate couponTemplate = requestToTemplate(request);
        couponTemplate = templateDao.save(couponTemplate);

        //根据优惠券模板异步生成优惠券码
        asyncService.asyncConstructCouponTemplate(couponTemplate);

        return couponTemplate;
    }

    /**
     * 将 TemplateRequest 转换成 CouponTemplate
     * @param request {@link TemplateRequest} 请求对象
     * @return CouponTemplate {@link CouponTemplate}
     */
    private CouponTemplate requestToTemplate(TemplateRequest request){
        return new CouponTemplate(request.getName(),request.getLogo(),request.getDesc()
                                 ,request.getCategory(),request.getProductLine()
                                 ,request.getCount(),request.getUserId()
                                 ,request.getTarget(),request.getRule());
    }
}
