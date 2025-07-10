package com.cheers.framework.tree.core;

/**
 * 树形实体接口
 * 实现此接口的实体类将获得树形结构支持
 *
 * @param <ID> ID类型
 */
public interface TreeEntity<ID> {
    
    /**
     * 获取节点ID
     */
    ID getId();

    /**
     * 获取父节点ID
     */
    ID getParentId();

    /**
     * 获取树路径
     */
    String getTreePath();

    /**
     * 设置树路径
     */
    void setTreePath(String treePath);

    /**
     * 获取层级深度
     */
    Integer getLevel();

    /**
     * 设置层级深度
     */
    void setLevel(Integer level);

    /**
     * 获取排序号
     */
    Integer getSort();

    /**
     * 是否启用
     */
    Integer getStatus();
} 