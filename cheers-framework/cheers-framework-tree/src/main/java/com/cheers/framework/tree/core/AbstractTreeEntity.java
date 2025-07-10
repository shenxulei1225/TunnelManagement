package com.cheers.framework.tree.core;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * 树形实体抽象基类
 *
 * @param <ID> ID类型
 * @param <B> 基础实体类型
 */
@Data
public abstract class AbstractTreeEntity<ID, B> extends B implements TreeEntity<ID> {

    /**
     * 树路径
     * 格式: /1/2/3/ 表示一个三级节点的完整路径
     */
    private String treePath;

    /**
     * 层级深度
     * 根节点level=1
     */
    private Integer level;

    /**
     * 是否为叶子节点
     */
    @TableField(exist = false)
    private Boolean leaf;

    /**
     * 是否禁用
     */
    @TableField(exist = false)
    private Boolean disabled;

    /**
     * 构建树路径
     */
    public void buildTreePath(String parentTreePath) {
        this.treePath = (parentTreePath == null ? "/" : parentTreePath) + getId() + "/";
        this.level = this.treePath.split("/").length - 1;
    }

    /**
     * 更新节点状态
     */
    public void updateNodeStatus(boolean hasChildren) {
        this.leaf = !hasChildren;
        this.disabled = (getStatus() != null && getStatus() == 1);
    }
} 