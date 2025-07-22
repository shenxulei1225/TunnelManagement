package com.cheers.arch.module.system.dal.dataobject.tree;

import java.util.Map;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.cheers.arch.framework.trees.core.TreeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 通用树节点 DO
 * 
 * @author cheers
 */
@TableName(value = "system_tree_node", autoResultMap = true)
@KeySequence("system_tree_node_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class TreeNodeDO extends TenantBaseDO implements TreeEntity<Long> {

    /**
     * 根节点ID
     */
    public static final Long ROOT_ID = 0L;

    /**
     * 节点ID
     */
    @TableId
    private Long id;

    /**
     * 树类型（关联TreeConfigDO）
     */
    private String treeType;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点编码
     */
    private String code;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 树路径（如：/1/2/3/）
     */
    private String treePath;

    /**
     * 层级深度
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 节点状态（0-禁用 1-启用）
     */
    private Integer status;

    /**
     * 是否只读
     */
    private Boolean readonly;

    /**
     * 节点描述
     */
    private String description;

    /**
     * 图标
     */
    private String icon;

    /**
     * 颜色
     */
    private String color;

    /**
     * 扩展属性（JSON格式）
     */
    @TableField(value = "extra_attrs", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraAttrs;

    /**
     * 备注
     */
    private String remark;

    // ==================== TreeEntity接口实现 ====================

    @Override
    public TreeNodeDO setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public TreeNodeDO setTreePath(String treePath) {
        this.treePath = treePath;
        return this;
    }

    @Override
    public TreeNodeDO setLevel(Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public TreeNodeDO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }
} 