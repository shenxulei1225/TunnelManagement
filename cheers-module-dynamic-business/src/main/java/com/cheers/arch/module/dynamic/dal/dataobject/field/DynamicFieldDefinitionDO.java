package com.cheers.arch.module.dynamic.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 动态字段定义 DO
 */
@TableName("dynamic_field_definition")
@KeySequence("dynamic_field_definition_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicFieldDefinitionDO extends TenantBaseDO {

    /**
     * 字段ID
     */
    @TableId
    private Long id;

    /**
     * 所属模型编码
     */
    private String modelCode;

    /**
     * 字段编码
     */
    private String code;

    /**
     * 字段名称
     */
    private String name;

    /**
     * 字段类型
     */
    private String type;

    /**
     * 字段长度
     */
    private Integer length;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 验证规则（JSON格式）
     */
    private String validation;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;

    /**
     * 是否只读
     */
    private Boolean readonly;

    /**
     * 是否唯一
     */
    private Boolean unique;

    /**
     * 是否索引
     */
    private Boolean indexed;

    /**
     * 字段配置（JSON格式）
     */
    private String config;
} 