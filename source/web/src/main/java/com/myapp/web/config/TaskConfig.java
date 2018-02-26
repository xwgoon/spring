package com.myapp.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;

//@Configuration
//@EnableAsync
//@EnableScheduling
public class TaskConfig implements AsyncConfigurer, SchedulingConfigurer {

    @Override
    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //此方式必须手动调用initialize()方法，因为Spring不会自动调用afterPropertiesSet()方法。
//        taskExecutor.initialize();
//        taskExecutor.setCorePoolSize(5);
//        taskExecutor.setMaxPoolSize(10);
//        taskExecutor.setQueueCapacity(25);
//        return taskExecutor;

        //此方式不必手动调用initialize()方法，因为TaskExecutor是通过Spring管理的bean，因此Spring会自动调用afterPropertiesSet()方法。
        return taskExecutor();
    }

    @Bean("taskExecutor2")
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.initialize();
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(25);
        return taskExecutor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.initialize();//原理同上
        taskScheduler.setPoolSize(5);
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

}
