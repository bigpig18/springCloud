package com.source.coupon.schedule;


import com.source.coupon.dao.CouponTemplateDao;
import com.source.coupon.entity.CouponTemplate;
import com.source.coupon.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 描述: 定时清理已过期的优惠券模板
 *
 * @author li
 * @date 2019/11/14
 */
@Slf4j
@Component
public class ScheduleTask {

    private final CouponTemplateDao templateDao;

    @Autowired
    public ScheduleTask(CouponTemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 下线已过期的优惠券模板
     */
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineCouponTemplate(){
        log.info("Start to Expire CouponTemplate");
        //查询出来没有过期的优惠券
        List<CouponTemplate> templates = templateDao.findAllByExpired(false);
        if (CollectionUtils.isEmpty(templates)){
            log.info("Done to Expire CouponTemplate");
            return;
        }
        //当前时间
        Date cur = new Date();
        List<CouponTemplate> list = new ArrayList<>(templates.size());
        templates.forEach(t -> {
            //根据优惠券模板规则中的 过期规则 校验优惠券模板是否过期
            TemplateRule rule = t.getRule();
            if (rule.getExpiration().getDeadline() < cur.getTime()){
                t.setExpired(true);
                list.add(t);
            }
        });
        if (CollectionUtils.isNotEmpty(list)){
            log.info("Expired CouponTemplate Num: {}",templateDao.saveAll(list));
        }
        log.info("Done to Expire CouponTemplate");
    }
}
