package com.source.coupon.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 描述: 自定义异步任务线程池
 *
 * @author li
 * @date 2019/11/13
 */
@Slf4j
@EnableAsync
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程池
        executor.setCorePoolSize(10);
        //最大线程数
        executor.setMaxPoolSize(20);
        //队列容量
        executor.setQueueCapacity(20);
        //空闲时线程最长生存时间
        executor.setKeepAliveSeconds(60);
        //线程名前缀
        executor.setThreadNamePrefix("couponAsync_");
        //任务关闭时线程池是否退出
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //服务关闭时，最长等待时间
        executor.setAwaitTerminationSeconds(60);
        //拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        executor.initialize();
        return executor;
    }

    @Bean
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler{

        /**
         * 异步任务异常捕捉类
         * @param throwable 异常任务抛出的异常
         * @param method 异步任务对应的方法
         * @param objects 异步任务参数数据
         */
        @SuppressWarnings("all")
        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            throwable.printStackTrace();
            log.error("AsyncErroe: {},Method: {},Param: {}",throwable.getMessage(),method.getName(), JSON.toJSONString(objects));
            // TODO 发送邮件或短信给运营人员，做进一步处理
        }
    }
}
