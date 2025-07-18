package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
public class FieldDO extends com.cheers.arch.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段键名 */
    private String fieldKey;

    /** 字段标签 */
    private String fieldLabel;

    /** 值类型 */
    private String valueType;

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
} 