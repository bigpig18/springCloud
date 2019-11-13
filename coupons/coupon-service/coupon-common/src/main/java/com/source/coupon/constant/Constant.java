package com.source.coupon.constant;

/**
 * 描述: 通用常量定义
 *
 * @author li
 * @date 2019/11/13
 */
public class Constant {

    /**
     * kafka 消息的 topic
     */
    public static final String TOPIC = "user_coupon_op";

    /**
     * redis key 前缀定义
     */
    public static class RedisPrefix{
        /**
         * 优惠券码key前缀
         */
        public static final String COUPON_TEMPLATE = "coupon_template_code_";
        /**
         * 用户当前所有可用的优惠券key前缀
         */
        public static final String USER_COUPON_USABLE = "user_coupon_usable_";
        /**
         * 用户已使用的优惠券key前缀
         */
        public static final String USER_COUPON_USED = "user_coupon_used";
        /**
         * 过期的优惠券
         */
        public static final String USER_COUPON_EXPIRED = "user_coupon_expired";
    }
}
