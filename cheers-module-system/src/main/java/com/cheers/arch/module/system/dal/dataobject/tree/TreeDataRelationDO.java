package com.cheers.arch.module.system.dal.dataobject.tree;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 通用树结构-数据关联 DO
 *
 * @author cheers
 */
@TableName("system_tree_data_rel")
@KeySequence("system_tree_data_rel_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TreeDataRelationDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 树类型：field_category/hierarchy_group/region_tree/device_tree/file_category等
     */
    private String treeType;

    /**
     * 树节点ID
     */
    private Long treeNodeId;

    /**
     * 数据类型：field_def/device/region/file/document等
     */
    private String dataType;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 数据名称（冗余字段，便于查询）
     */
    private String dataName;

    /**
     * 数据类型标签（如：字段、设备、区域等）
     */
    private String dataTypeLabel;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 是否必填（针对字段类型）
     */
    private Boolean isRequired;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 扩展元数据（JSON格式）
     */
    private String metadata;
} 