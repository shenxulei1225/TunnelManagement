package com.cheers.framework.job.core.scheduler;

import com.cheers.framework.job.core.handler.JobHandler;
import com.cheers.framework.job.core.handler.JobHandlerInvoker;
import com.cheers.framework.job.core.model.JobInfo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务调度器
 */
@Slf4j
public class SchedulerManager {

    private final Scheduler scheduler;

    /**
     * 任务分组缓存
     * key: 分组名称
     * value: 任务信息列表
     */
    private final Map<String, List<JobInfo>> groupCache = new ConcurrentHashMap<>();

    /**
     * 任务依赖缓存
     * key: 任务名称
     * value: 依赖的任务名称列表
     */
    private final Map<String, List<String>> dependencyCache = new ConcurrentHashMap<>();

    public SchedulerManager(SchedulerFactoryBean schedulerFactoryBean) {
        this.scheduler = schedulerFactoryBean.getScheduler();
    }

    /**
     * 添加定时任务
     */
    public void addJob(JobInfo jobInfo) throws SchedulerException {
        // 创建JobDetail
        JobDetail jobDetail = JobBuilder.newJob(JobHandlerInvoker.class)
                .withIdentity(jobInfo.getName(), jobInfo.getGroup())
                .build();

        // 创建Trigger
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(jobInfo.getName(), jobInfo.getGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(jobInfo.getCron()))
                .build();

        // 添加任务
        scheduler.scheduleJob(jobDetail, trigger);

        // 添加到分组缓存
        addToGroup(jobInfo);

        // 添加依赖关系
        if (jobInfo.getDependencies() != null && jobInfo.getDependencies().length > 0) {
            dependencyCache.put(jobInfo.getName(), Arrays.asList(jobInfo.getDependencies()));
        }

        log.info("[addJob][任务({})添加成功]", jobInfo.getName());
    }

    /**
     * 删除定时任务
     */
    public void deleteJob(String jobName, String group) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobName, group));
        // 从分组缓存中删除
        removeFromGroup(jobName, group);
        // 从依赖缓存中删除
        dependencyCache.remove(jobName);
        log.info("[deleteJob][任务({})删除成功]", jobName);
    }

    /**
     * 暂停定时任务
     */
    public void pauseJob(String jobName, String group) throws SchedulerException {
        scheduler.pauseJob(new JobKey(jobName, group));
        log.info("[pauseJob][任务({})暂停成功]", jobName);
    }

    /**
     * 恢复定时任务
     */
    public void resumeJob(String jobName, String group) throws SchedulerException {
        scheduler.resumeJob(new JobKey(jobName, group));
        log.info("[resumeJob][任务({})恢复成功]", jobName);
    }

    /**
     * 立即执行定时任务
     */
    public void triggerJob(String jobName, String group) throws SchedulerException {
        // 检查依赖是否满足
        if (!checkDependencies(jobName)) {
            log.warn("[triggerJob][任务({})依赖未满足]", jobName);
            return;
        }

        try {
            scheduler.triggerJob(new JobKey(jobName, group));
            log.info("[triggerJob][任务({})触发成功]", jobName);
        } catch (SchedulerException e) {
            log.error("[triggerJob][任务({})触发失败]", jobName, e);
            throw e;
        }
    }

    /**
     * 获取所有定时任务
     */
    public List<JobInfo> listJobs() throws SchedulerException {
        List<JobInfo> jobInfos = new ArrayList<>();
        Set<JobKey> jobKeys = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : jobKeys) {
            JobInfo jobInfo = new JobInfo();
            jobInfo.setName(jobKey.getName());
            jobInfo.setGroup(jobKey.getGroup());
            // 获取触发器
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (!triggers.isEmpty()) {
                CronTrigger trigger = (CronTrigger) triggers.get(0);
                jobInfo.setCron(trigger.getCronExpression());
                jobInfo.setNextExecuteTime(trigger.getNextFireTime().getTime());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                jobInfo.setStatus(triggerState.name());
            }
            // 获取依赖
            List<String> dependencies = dependencyCache.get(jobKey.getName());
            if (dependencies != null) {
                jobInfo.setDependencies(dependencies.toArray(new String[0]));
            }
            jobInfos.add(jobInfo);
        }
        return jobInfos;
    }

    /**
     * 获取分组任务
     */
    public List<JobInfo> getGroupJobs(String group) {
        return groupCache.getOrDefault(group, Collections.emptyList());
    }

    /**
     * 添加到分组缓存
     */
    private void addToGroup(JobInfo jobInfo) {
        String group = jobInfo.getGroup();
        if (group == null) {
            group = "default";
        }
        groupCache.computeIfAbsent(group, k -> new ArrayList<>()).add(jobInfo);
    }

    /**
     * 从分组缓存中删除
     */
    private void removeFromGroup(String jobName, String group) {
        List<JobInfo> jobs = groupCache.get(group);
        if (jobs != null) {
            jobs.removeIf(job -> job.getName().equals(jobName));
        }
    }

    /**
     * 检查任务依赖是否满足
     */
    private boolean checkDependencies(String jobName) throws SchedulerException {
        List<String> dependencies = dependencyCache.get(jobName);
        if (dependencies == null || dependencies.isEmpty()) {
            return true;
        }
        for (String dependencyName : dependencies) {
            JobKey jobKey = new JobKey(dependencyName);
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            if (jobDetail == null) {
                return false;
            }
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            if (triggers.isEmpty()) {
                return false;
            }
            Trigger.TriggerState triggerState = scheduler.getTriggerState(triggers.get(0).getKey());
            if (triggerState != Trigger.TriggerState.NORMAL) {
                return false;
            }
        }
        return true;
    }

} 