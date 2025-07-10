package com.cheers.framework.job.config;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务 Quartz 配置类
 */
@Slf4j
@AutoConfiguration(before = QuartzAutoConfiguration.class)
@ConditionalOnClass({Scheduler.class, SchedulerFactoryBean.class})
@ConditionalOnProperty(prefix = "cheers.job", value = "enable", matchIfMissing = true)
public class CheersQuartzAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public SchedulerFactoryBean schedulerFactoryBean() {
        log.info("[schedulerFactoryBean][初始化 Quartz 调度器]");
        return new SchedulerFactoryBean();
    }

} 