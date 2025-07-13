package com.cheers.arch.module.dynamic.dal.dataobject.view;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 视图配置
 */
@TableName("dynamic_view_config")
@KeySequence("dynamic_view_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ViewConfigDO extends TenantBaseDO {

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
     * 视图类型（form:表单，list:列表，tree:树形）
     */
    private String type;

    /**
     * 视图编码
     */
    private String code;

    /**
     * 视图名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 布局配置（JSON格式）
     */
    private String layout;

    /**
     * 组件配置（JSON格式）
     */
    private String components;

    /**
     * 操作配置（JSON格式）
     */
    private String actions;

    /**
     * 权限配置（JSON格式）
     */
    private String permissions;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;
} 