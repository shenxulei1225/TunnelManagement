package com.cheers.arch.module.dynamic.dal.dataobject.template;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态场景模板 DO
 */
@TableName("dynamic_scene_template")
@KeySequence("dynamic_scene_template_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicSceneTemplateDO extends TenantBaseDO {

    /**
     * 模板ID
     */
    @TableId
    private Long id;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 场景描述
     */
    private String description;

    /**
     * 推荐字段配置（JSON格式）
     */
    private String recommendedFields;

    /**
     * 默认布局配置（JSON格式）
     */
    private String defaultLayout;

    /**
     * 验证规则配置（JSON格式）
     */
    private String validationRules;

    /**
     * 图标
     */
    private String icon;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;
} 