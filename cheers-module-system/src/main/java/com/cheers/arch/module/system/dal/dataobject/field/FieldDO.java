package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字段 DO
 * 对应表 system_field
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_field")
public class FieldDO extends TenantBaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段编码 */
    private String fieldCode;

    /** 字段名称 */
    private String fieldName;

    /** 字段类型 */
    private String fieldType;

    /** 显示名称 */
    private String display;

    /** 字段描述 */
    private String description;

    /** 是否自定义字段 */
    private Boolean isCustom;

    /** 单位 */
    private String unit;

    /** 枚举值JSON */
    private String enumJson;

    /** 计算表达式 */
    private String calcExpr;

    /** 默认值 */
    private String defaultValue;

    /** 是否必填 */
    private Boolean required;

    /** 排序 */
    private Integer sort;

    /** 备注 */
    private String remark;

    /** 状态（0正常 1停用） */
    private Integer status;
} 