package com.source.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * 描述: kafka相关的服务接口定义
 *
 * @author li
 * @date 2019/12/2
 */
public interface IKafkaService {

    /**
     * 消费 kafka 中优惠券消息
     * @param record {@link ConsumerRecord}
     */
    void consumeCouponKafkaMessage(ConsumerRecord<?,?> record);
}
