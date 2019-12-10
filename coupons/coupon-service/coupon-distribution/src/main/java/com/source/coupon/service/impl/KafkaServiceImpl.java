package com.source.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.source.coupon.constant.Constant;
import com.source.coupon.constant.CouponStatus;
import com.source.coupon.dao.CouponDao;
import com.source.coupon.entity.Coupon;
import com.source.coupon.service.IKafkaService;
import com.source.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 描述: kafka 相关service接口实现
 * 将cache中的coupon状态变化同步到db中
 * @author li
 * @date 2019/12/5
 */
@Slf4j
@Service
public class KafkaServiceImpl implements IKafkaService {

    private final CouponDao couponDao;

    @Autowired
    public KafkaServiceImpl(CouponDao couponDao) {
        this.couponDao = couponDao;
    }

    @KafkaListener(topics = {Constant.TOPIC},groupId = "coupon-1")
    @Override
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()){
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(message.toString(),CouponKafkaMessage.class);
            log.info("Receive CouponKafkaMessage:{}",message.toString());
            CouponStatus status = CouponStatus.of(couponInfo.getStatus());
            switch(status){
                case USABLE:break;
                case USED:processUsedCouponsByStatus(couponInfo);break;
                case EXPIRED:processExpiredCouponByStatus(couponInfo);break;
                default:
            }
        }
    }

    /**
     * 处理已使用的优惠券
     * @param message 消息
     */
    private void processUsedCouponsByStatus(CouponKafkaMessage message){
        processCouponsByStatus(message, CouponStatus.USED);
    }

    /**
     * 处理过期优惠券
     * @param message 消息
     */
    private void processExpiredCouponByStatus(CouponKafkaMessage message){
        processCouponsByStatus(message, CouponStatus.EXPIRED);
    }

    /**
     * 根据状态处理优惠券信息
     * 分开定义过期 已使用的操作，是因为，两种状态可能对应不同的操作
     * @param message 发送到kafka的信息
     * @param status message相关状态
     */
    private void processCouponsByStatus(CouponKafkaMessage message,CouponStatus status){
        List<Coupon> coupons = couponDao.findAllById(message.getIds());
        if (CollectionUtils.isEmpty(coupons) || coupons.size() != message.getIds().size()){
            log.error("Can Not Find Right Coupon Info:{}",JSON.toJSONString(message));
            return;
        }
        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Ops Status Coupon:{}",couponDao.saveAll(coupons).size());
    }

}
