package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 字段定义-分级组关联 DO
 *
 * @author cheers
 */
@TableName("system_field_def_hierarchy_rel")
@KeySequence("system_field_def_hierarchy_rel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldDefHierarchyRelDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 字段定义 ID
     */
    private Long fieldDefId;

    /**
     * 分级组 ID
     */
    private Long hierarchyGroupId;

    /**
     * 排序
     */
    private Integer sort;

} 