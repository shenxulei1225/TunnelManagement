package com.cheers.arch.module.dynamic.dal.dataobject.view;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态视图配置 DO
 */
@TableName("dynamic_view_config")
@KeySequence("dynamic_view_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicViewConfigDO extends TenantBaseDO {

    /**
     * 配置ID
     */
    @TableId
    private Long id;

    /**
     * 业务模型编码
     */
    private String modelCode;

    /**
     * 视图类型（list:列表，detail:详情，form:表单）
     */
    private String viewType;

    /**
     * 视图编码
     */
    private String viewCode;

    /**
     * 视图名称
     */
    private String viewName;

    /**
     * 布局配置（JSON格式）
     */
    private String layoutConfig;

    /**
     * 字段配置（JSON格式）
     */
    private String fieldConfig;

    /**
     * 操作配置（JSON格式）
     */
    private String actionConfig;

    /**
     * 权限配置（JSON格式）
     */
    private String permissionConfig;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;
} 