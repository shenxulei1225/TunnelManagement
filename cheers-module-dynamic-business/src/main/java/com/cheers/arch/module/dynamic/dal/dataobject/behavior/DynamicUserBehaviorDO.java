package com.cheers.arch.module.dynamic.dal.dataobject.behavior;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态用户行为 DO
 */
@TableName("dynamic_user_behavior")
@KeySequence("dynamic_user_behavior_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicUserBehaviorDO extends TenantBaseDO {

    /**
     * 行为ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 场景类型
     */
    private String sceneType;

    /**
     * 模型编码
     */
    private String modelCode;

    /**
     * 字段使用情况（JSON格式）
     */
    private String fieldUsage;

    /**
     * 操作类型（create:创建，update:更新，view:查看）
     */
    private String operationType;

    /**
     * 操作时间
     */
    private LocalDateTime operationTime;

    /**
     * 操作详情（JSON格式）
     */
    private String operationDetail;
} 