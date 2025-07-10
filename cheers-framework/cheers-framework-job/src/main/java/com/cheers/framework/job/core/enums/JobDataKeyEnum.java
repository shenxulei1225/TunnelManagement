package com.cheers.framework.job.core.enums;

/**
 * 任务数据的 key 枚举
 */
public enum JobDataKeyEnum {

    /**
     * 任务编号
     */
    JOB_ID,
    /**
     * 任务处理器的名字
     */
    JOB_HANDLER_NAME,
    /**
     * 任务处理器的参数
     */
    JOB_HANDLER_PARAM,
    /**
     * 任务的重试次数
     */
    JOB_RETRY_COUNT,
    /**
     * 任务的重试间隔
     */
    JOB_RETRY_INTERVAL,
    /**
     * 最后一次执行的开始时间
     */
    LAST_BEGIN_TIME,
    /**
     * 最后一次执行的结束时间
     */
    LAST_END_TIME,
    /**
     * 最后一次执行的成功状态
     */
    LAST_SUCCESS_STATE,
    /**
     * 下一次触发时间
     */
    NEXT_TRIGGER_TIME,

} 