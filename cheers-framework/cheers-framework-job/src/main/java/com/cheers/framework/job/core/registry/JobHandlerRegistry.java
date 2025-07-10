package com.cheers.framework.job.core.registry;

import com.cheers.framework.job.core.handler.JobHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 任务处理器注册表
 */
@Slf4j
public class JobHandlerRegistry {

    /**
     * 任务处理器缓存
     * key: 任务名称
     * value: 任务处理器
     */
    private final Map<String, JobHandler> handlerCache = new ConcurrentHashMap<>();

    /**
     * 注册任务处理器
     */
    public void registerHandler(String jobName, JobHandler handler) {
        handlerCache.put(jobName, handler);
        log.info("[registerHandler][任务处理器({})注册成功]", jobName);
    }

    /**
     * 获取任务处理器
     */
    public JobHandler getHandler(String jobName) {
        return handlerCache.get(jobName);
    }

    /**
     * 移除任务处理器
     */
    public void removeHandler(String jobName) {
        JobHandler handler = handlerCache.remove(jobName);
        if (handler != null) {
            handler.shutdown();
            log.info("[removeHandler][任务处理器({})移除成功]", jobName);
        }
    }

    /**
     * 清空所有任务处理器
     */
    public void clear() {
        handlerCache.values().forEach(JobHandler::shutdown);
        handlerCache.clear();
        log.info("[clear][所有任务处理器已清空]");
    }
} 