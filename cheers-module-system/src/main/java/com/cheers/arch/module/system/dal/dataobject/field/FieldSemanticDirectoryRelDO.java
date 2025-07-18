package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字段与语义目录关联 DO (多对多)
 * 对应表 system_field_semantic_directory_rel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("system_field_semantic_directory_rel")
public class FieldSemanticDirectoryRelDO extends com.cheers.arch.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段ID */
    private Long fieldId;

    /** 语义目录ID */
    private Long semanticDirectoryId;

    /** 排序 */
    private Integer sort;

    /** 备注 */
    private String remark;
} 