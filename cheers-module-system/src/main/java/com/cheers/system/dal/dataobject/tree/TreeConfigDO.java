package com.cheers.system.dal.dataobject.tree;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 树形结构配置
 */
@TableName("system_tree_config")
@KeySequence("system_tree_config_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TreeConfigDO extends BaseDO {

    /**
     * 配置ID
     */
    @TableId
    private Long id;

    /**
     * 业务类型编码
     */
    private String code;

    /**
     * 业务类型名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 节点名称标签
     */
    private String nodeNameLabel;

    /**
     * 节点编码标签
     */
    private String nodeCodeLabel;

    /**
     * 最大层级（NULL表示不限制）
     */
    private Integer maxLevel;

    /**
     * 排序类型
     */
    private Integer sortType;

    /**
     * 状态
     */
    private Integer status;
} 