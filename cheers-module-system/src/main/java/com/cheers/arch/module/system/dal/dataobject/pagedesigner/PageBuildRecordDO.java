package com.cheers.arch.module.system.dal.dataobject.pagedesigner;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 页面构建记录 DO
 *
 * @author cheers
 */
@TableName("system_page_build_record")
@KeySequence("system_page_build_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageBuildRecordDO extends TenantBaseDO {

    /**
     * 构建记录ID
     */
    @TableId
    private String id;

    /**
     * 页面设计ID
     */
    private String pageDesignId;

    /**
     * 构建ID
     */
    private String buildId;

    /**
     * 构建状态
     */
    private String buildStatus;

    /**
     * 构建进度(0-100)
     */
    private Integer progress;

    /**
     * 构建消息
     */
    private String message;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 完成时间
     */
    private LocalDateTime endTime;

    /**
     * 错误信息
     */
    private String errorMessage;

} 