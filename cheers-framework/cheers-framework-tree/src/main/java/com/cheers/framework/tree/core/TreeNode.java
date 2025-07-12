package com.cheers.arch.framework.tree.core;

/**
 * 树节点接口
 */
public interface TreeNode<T> {

    /**
     * 获取节点ID
     */
    Long getId();

    /**
     * 获取父节点ID
     */
    Long getParentId();

    /**
     * 获取节点名称
     */
    String getName();

    /**
     * 获取节点编码
     */
    String getCode();

    /**
     * 获取树路径
     */
    String getTreePath();

    /**
     * 获取层级
     */
    Integer getLevel();

    /**
     * 获取排序号
     */
    Integer getSort();

    /**
     * 获取节点状态
     */
    Integer getStatus();

    /**
     * 获取租户ID
     */
    Long getTenantId();
} 