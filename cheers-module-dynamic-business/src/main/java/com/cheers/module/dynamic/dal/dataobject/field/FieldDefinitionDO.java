package com.cheers.module.dynamic.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.module.dynamic.dal.dataobject.base.TenantBaseDO;
import com.cheers.module.dynamic.enums.field.DisplayTypeEnum;
import com.cheers.module.dynamic.enums.field.FieldStatusEnum;
import com.cheers.module.dynamic.enums.field.FieldTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 字段定义 DO
 */
@TableName("dynamic_field_definition")
@KeySequence("dynamic_field_definition_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldDefinitionDO extends TenantBaseDO {

    /**
     * 字段定义ID
     */
    @TableId
    private Long id;

    /**
     * 业务模型编码
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
     *
     * 枚举 {@link FieldTypeEnum}
     */
    private FieldTypeEnum type;

    /**
     * 字段长度
     */
    private Integer length;

    /**
     * 字段精度
     */
    private Integer precision;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 默认值
     */
    private String defaultValue;

    /**
     * 校验规则
     */
    private String validationRules;

    /**
     * 显示类型
     *
     * 枚举 {@link DisplayTypeEnum}
     */
    private DisplayTypeEnum displayType;

    /**
     * 显示配置
     */
    private String displayConfig;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态
     *
     * 枚举 {@link FieldStatusEnum}
     */
    private FieldStatusEnum status;

    /**
     * 备注
     */
    private String remark;
} 