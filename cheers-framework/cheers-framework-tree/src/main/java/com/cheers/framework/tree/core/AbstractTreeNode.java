package com.cheers.framework.tree.core;

import com.baomidou.mybatisplus.annotation.TableField;
import com.cheers.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 树节点抽象基类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractTreeNode<T extends TreeNode<T>> extends BaseDO implements TreeNode<T> {

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点编码
     */
    private String code;

    /**
     * 树路径
     */
    private String treePath;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 子节点列表
     */
    @TableField(exist = false)
    private List<T> children = new ArrayList<>();

    /**
     * 是否是叶子节点
     */
    @TableField(exist = false)
    private boolean leaf = true;

    /**
     * 是否展开
     */
    @TableField(exist = false)
    private boolean expanded = false;

    /**
     * 是否选中
     */
    @TableField(exist = false)
    private boolean selected = false;

    /**
     * 是否禁用
     */
    @TableField(exist = false)
    private boolean disabled = false;

    /**
     * 添加子节点
     */
    public void addChild(T child) {
        this.children.add(child);
        this.leaf = false;
    }

    /**
     * 移除子节点
     */
    public void removeChild(T child) {
        this.children.remove(child);
        this.leaf = this.children.isEmpty();
    }

    /**
     * 清空子节点
     */
    public void clearChildren() {
        this.children.clear();
        this.leaf = true;
    }

    /**
     * 获取子节点数量
     */
    public int getChildCount() {
        return this.children.size();
    }

    /**
     * 是否包含指定子节点
     */
    public boolean containsChild(T child) {
        return this.children.contains(child);
    }

    /**
     * 获取指定位置的子节点
     */
    public T getChild(int index) {
        return this.children.get(index);
    }
} 