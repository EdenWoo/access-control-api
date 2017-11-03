package com.cfgglobal.test.config;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@EnableScheduling
@Configuration
public class TaskConfig {

    @Bean
    public Executor taskScheduler() {
        ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("task-thread-%d").build();
        return Executors.newScheduledThreadPool(10, namedThreadFactory);
    }

}
