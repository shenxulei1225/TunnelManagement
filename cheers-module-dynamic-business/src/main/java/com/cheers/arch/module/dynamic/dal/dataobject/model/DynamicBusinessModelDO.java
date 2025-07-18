package com.cheers.arch.module.dynamic.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.cheers.arch.module.dynamic.enums.BusinessModelStatusEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态业务模型 DO
 */
@TableName("dynamic_business_model")
@KeySequence("dynamic_business_model_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicBusinessModelDO extends TenantBaseDO {

    /**
     * 模型ID
     */
    @TableId
    private Long id;

    /**
     * 模型编码
     */
    private String code;

    /**
     * 模型名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 数据结构类型（1:树形，2:列表）
     */
    private Integer structureType;

    /**
     * 存储策略（1:单表，2:分表）
     */
    private Integer storageStrategy;

    /**
     * 模型配置（JSON格式）
     */
    private String config;

    /**
     * 数据表名（虚拟字段，用于前端显示）
     */
    private String tableName;

    /**
     * 模型类型（0:系统，1:自定义）
     */
    private Integer modelType;

    /**
     * 所属目录ID（0表示未分类）
     */
    private Long directoryId;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态
     *
     * 枚举 {@link BusinessModelStatusEnum}
     */
    private Integer status;

    /**
     * 是否只读（0:可编辑，1:只读）
     */
    private Boolean readonly;
} 