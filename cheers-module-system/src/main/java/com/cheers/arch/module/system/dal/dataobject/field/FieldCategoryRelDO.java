package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字段与分类关联 DO (多对多)
 * 对应表 system_field_category_rel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("system_field_category_rel")
public class FieldCategoryRelDO extends com.cheers.arch.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段ID */
    private Long fieldId;

    /** 分类ID */
    private Long categoryId;

    /** 是否必填 */
    private Boolean required;

    /** 排序 */
    private Integer sort;
} 