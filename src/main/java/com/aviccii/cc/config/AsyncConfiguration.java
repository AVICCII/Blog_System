package com.aviccii.cc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author aviccii 2020/9/25
 * @Discrimination
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean
    public Executor asyncExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(10);
        executor.setThreadNamePrefix("blog_task_worker-");
        executor.setQueueCapacity(30);
        executor.initialize();
        return executor;
    }
}
