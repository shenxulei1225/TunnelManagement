package com.cheers.arch.module.system.dal.dataobject.tree;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 树数据关系 DO
 *
 * @author 系统管理员
 */
@TableName("system_tree_data_rel")
@KeySequence("system_tree_data_rel_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeDataRelDO extends BaseDO {

    /**
     * 关系ID
     */
    @TableId
    private Long id;

    /**
     * 树类型
     */
    private String treeType;

    /**
     * 树节点ID
     */
    private Long treeNodeId;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 数据类型标签
     */
    private String dataTypeLabel;

    /**
     * 显示顺序
     */
    private Integer displayOrder;

    /**
     * 是否必填
     */
    private Boolean isRequired;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 元数据（JSON格式）
     */
    private String metadata;
} 