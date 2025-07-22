package com.cheers.arch.module.system.dal.dataobject.tree;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 树配置 DO
 *
 * @author 系统管理员
 */
@TableName("system_tree_config")
@KeySequence("system_tree_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeConfigDO extends BaseDO {

    /**
     * 配置ID
     */
    @TableId
    private Long id;

    /**
     * 树类型
     */
    private String treeType;

    /**
     * 树名称
     */
    private String treeName;

    /**
     * 描述
     */
    private String description;

    /**
     * 允许的数据类型列表（JSON格式）
     */
    private String allowedDataTypes;

    /**
     * 最大层级
     */
    private Integer maxLevel;

    /**
     * 节点名称标签
     */
    private String nodeNameLabel;

    /**
     * 节点编码标签
     */
    private String nodeCodeLabel;

    /**
     * 排序类型
     */
    private Integer sortType;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 配置JSON
     */
    private String configJson;
} 