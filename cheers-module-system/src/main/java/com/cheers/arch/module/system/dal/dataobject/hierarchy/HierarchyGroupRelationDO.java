package com.cheers.arch.module.system.dal.dataobject.hierarchy;

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
 * 系统分级组关系 DO
 *
 * @author cheers
 */
@TableName("system_hierarchy_group_relation")
@KeySequence("system_hierarchy_group_relation_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyGroupRelationDO extends TenantBaseDO {

    /**
     * 关系ID
     */
    @TableId
    private Long id;

    /**
     * 分级组ID
     */
    private Long hierarchyGroupId;

    /**
     * 目标类型
     */
    private String targetType;

    /**
     * 目标ID
     */
    private Long targetId;

} 