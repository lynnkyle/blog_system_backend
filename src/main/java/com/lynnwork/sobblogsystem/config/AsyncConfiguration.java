package com.lynnwork.sobblogsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfiguration {
    @Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //线程池的核心线程数,是线程池中始终保持活动的最小线程数,除非线程池被关闭
        executor.setCorePoolSize(2);
        //线程池的最大线程数,是线程池中允许的最大线程数
        executor.setMaxPoolSize(10);
        //线程名称的前缀
        executor.setThreadNamePrefix("sob_blog_task_worker-");
        //线程任务队列的容量
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }
}
