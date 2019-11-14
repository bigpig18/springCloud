package com.source.coupon.service.impl;

import com.source.coupon.dao.CouponTemplateDao;
import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.exception.CouponException;
import com.source.coupon.service.ITemplateBaseService;
import com.source.coupon.vo.CouponTemplateSdk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述: 优惠券模板基础服务实现
 *
 * @author li
 * @date 2019/11/14
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    private final CouponTemplateDao templateDao;

    @Autowired
    public TemplateBaseServiceImpl(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    @Override
    public CouponTemplate buildTemplateInfo(Integer id) throws CouponException {
        Optional<CouponTemplate> template = templateDao.findById(id);
        if (!template.isPresent()){
            throw new CouponException("Template is not exist: " + id);
        }
        return template.get();
    }

    @Override
    public List<CouponTemplateSdk> findAllUsableTemplate() {
        //可用且未过期
        List<CouponTemplate> templates = templateDao.findAllByAvailableAndExpired(true,false);
        return templates.stream().map(this::templateToTemplateSdk).collect(Collectors.toList());
    }

    @Override
    public Map<Integer, CouponTemplateSdk> findIds2TemplateSdk(Collection<Integer> ids) {
        List<CouponTemplate> templates = templateDao.findAllById(ids);
        return templates.stream().map(this::templateToTemplateSdk)
                .collect(Collectors.toMap(
                        CouponTemplateSdk::getId, Function.identity()
                ));
    }

    /**
     * CouponTemplate 转换为 CouponTemplateSdk
     * @param template {@link CouponTemplate} 优惠券模板信息
     * @return CouponTemplateSdk {@link CouponTemplateSdk}
     */
    private CouponTemplateSdk templateToTemplateSdk(CouponTemplate template){
        return new CouponTemplateSdk(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getDesc(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                //并非拼装好的key
                template.getKey(),
                template.getTarget().getCode(),
                template.getRule()
        );
    }
}
