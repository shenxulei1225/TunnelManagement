package com.cheers.framework.job.core.handler;

import com.cheers.framework.job.core.registry.JobHandlerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Quartz任务执行器
 */
@Slf4j
public class JobHandlerInvoker implements Job {

    @Autowired
    private JobHandlerRegistry jobHandlerRegistry;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        String groupName = context.getJobDetail().getKey().getGroup();
        
        try {
            // 获取任务处理器
            JobHandler handler = jobHandlerRegistry.getHandler(jobName);
            if (handler == null) {
                log.error("[execute][任务({})的处理器不存在]", jobName);
                return;
            }

            // 检查任务分组
            if (!groupName.equals(handler.getJob().group())) {
                log.error("[execute][任务({})的分组({})与处理器分组({})不匹配]", 
                    jobName, groupName, handler.getJob().group());
                return;
            }

            // 执行任务
            handler.execute(jobName);
        } catch (Exception e) {
            log.error("[execute][任务({})执行异常]", jobName, e);
            throw new JobExecutionException(e);
        }
    }
} 