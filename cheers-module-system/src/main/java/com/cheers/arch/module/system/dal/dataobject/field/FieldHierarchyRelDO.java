package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 字段-分组关联 DO
 * 对应表 system_field_hierarchy_rel
 * 
 * 设计说明：
 * - sort: 表示字段在特定分组中的显示顺序
 * - 注意：此表不包含required字段，因为分组(hierarchy)主要用于字段的逻辑归类，
 *   而required是UI层面的业务规则，主要在分类(category)中使用
 *
 * @author cheers
 */
@TableName("system_field_hierarchy_rel")
@KeySequence("system_field_hierarchy_rel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldHierarchyRelDO extends TenantBaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;

    /**
     * 字段 ID
     */
    private Long fieldId;

    /**
     * 分组 ID
     */
    private Long hierarchyGroupId;

    /**
     * 排序
     */
    private Integer sort;

} 