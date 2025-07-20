package com.cheers.arch.module.dynamic.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态字段分组 DO
 */
@TableName("dynamic_field_group")
@KeySequence("dynamic_field_group_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicFieldGroupDO extends TenantBaseDO {

    /**
     * 分组ID
     */
    @TableId
    private Long id;

    /**
     * 所属模型编码
     */
    private String modelCode;

    /**
     * 分组名称
     */
    private String name;

    /**
     * 分组编码
     */
    private String code;

    /**
     * 分组描述
     */
    private String description;

    /**
     * 父分组ID
     */
    private Long parentId;

    /**
     * 分组路径
     */
    private String path;

    /**
     * 分组层级
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;

    /**
     * 分组图标
     */
    private String icon;

    /**
     * 分组颜色
     */
    private String color;
} 