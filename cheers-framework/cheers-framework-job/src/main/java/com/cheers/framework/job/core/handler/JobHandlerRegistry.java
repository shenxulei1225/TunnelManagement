package com.cheers.framework.job.core.handler;

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
     */
    private static final Map<String, JobHandler> HANDLERS = new ConcurrentHashMap<>();

    /**
     * 注册任务处理器
     */
    public static void register(JobHandler jobHandler) {
        String name = jobHandler.getName();
        if (HANDLERS.containsKey(name)) {
            log.warn("[register][任务处理器({})已存在,将被覆盖]", name);
        }
        HANDLERS.put(name, jobHandler);
        log.info("[register][任务处理器({})注册成功]", name);
    }

    /**
     * 获取任务处理器
     */
    public static JobHandler getHandler(String name) {
        JobHandler handler = HANDLERS.get(name);
        if (handler == null) {
            log.error("[getHandler][任务处理器({})不存在]", name);
        }
        return handler;
    }

} 