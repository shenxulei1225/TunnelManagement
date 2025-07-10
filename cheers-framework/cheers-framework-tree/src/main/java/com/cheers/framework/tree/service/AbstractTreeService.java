package com.cheers.framework.tree.service;

import com.cheers.framework.common.exception.ServiceException;
import com.cheers.framework.tree.core.TreeNode;
import com.cheers.framework.tree.utils.TreeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 树形结构服务抽象实现类
 */
public abstract class AbstractTreeService<T extends TreeNode<T>> implements TreeService<T> {

    /**
     * 获取所有节点列表
     */
    protected abstract List<T> getNodeList();

    /**
     * 保存节点
     */
    protected abstract void saveNode(T node);

    /**
     * 删除节点
     */
    protected abstract void removeNode(Long id);

    @Override
    public Long createNode(T node) {
        // 校验父节点
        if (!isValidParent(node.getParentId(), null)) {
            throw new ServiceException("父节点不存在或不合法");
        }

        // 校验编码唯一性
        if (StringUtils.isNotEmpty(node.getCode()) && !isCodeUnique(node.getCode(), null)) {
            throw new ServiceException("节点编码已存在");
        }

        // 设置树路径和层级
        List<T> nodes = getNodeList();
        if (node.getParentId() != null && node.getParentId() != 0) {
            T parent = nodes.stream()
                    .filter(n -> Objects.equals(n.getId(), node.getParentId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("父节点不存在"));
            node.setLevel(parent.getLevel() + 1);
        } else {
            node.setLevel(1);
        }

        // 保存节点
        saveNode(node);

        // 更新树路径
        node.setTreePath(TreeUtils.generateTreePath(node, nodes));
        saveNode(node);

        return node.getId();
    }

    @Override
    public void updateNode(T node) {
        // 校验父节点
        if (!isValidParent(node.getParentId(), node.getId())) {
            throw new ServiceException("父节点不存在或不合法");
        }

        // 校验编码唯一性
        if (StringUtils.isNotEmpty(node.getCode()) && !isCodeUnique(node.getCode(), node.getId())) {
            throw new ServiceException("节点编码已存在");
        }

        // 设置树路径和层级
        List<T> nodes = getNodeList();
        if (node.getParentId() != null && node.getParentId() != 0) {
            T parent = nodes.stream()
                    .filter(n -> Objects.equals(n.getId(), node.getParentId()))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("父节点不存在"));
            node.setLevel(parent.getLevel() + 1);
        } else {
            node.setLevel(1);
        }

        // 保存节点
        saveNode(node);

        // 更新树路径
        node.setTreePath(TreeUtils.generateTreePath(node, nodes));
        saveNode(node);

        // 更新子节点
        updateChildrenTreePath(node, nodes);
    }

    @Override
    public void deleteNode(Long id) {
        // 检查是否存在子节点
        List<T> nodes = getNodeList();
        List<Long> childrenIds = TreeUtils.getChildrenIds(getNode(id), nodes);
        childrenIds.remove(id);
        if (!childrenIds.isEmpty()) {
            throw new ServiceException("存在子节点，无法删除");
        }

        // 删除节点
        removeNode(id);
    }

    @Override
    public void moveNode(Long id, Long targetParentId) {
        // 校验目标父节点
        if (!isValidParent(targetParentId, id)) {
            throw new ServiceException("目标父节点不存在或不合法");
        }

        // 获取当前节点
        T node = getNode(id);
        if (node == null) {
            throw new ServiceException("节点不存在");
        }

        // 设置新的父节点
        node.setParentId(targetParentId);

        // 更新节点
        updateNode(node);
    }

    @Override
    public List<T> getChildren(Long parentId) {
        return getNodeList().stream()
                .filter(node -> Objects.equals(node.getParentId(), parentId))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<T> getTree() {
        List<T> nodes = getNodeList();
        return TreeUtils.buildTree(nodes);
    }

    @Override
    public List<T> getTreeByNode(Long id) {
        // 获取当前节点
        T node = getNode(id);
        if (node == null) {
            return new ArrayList<>();
        }

        // 获取所有节点
        List<T> nodes = getNodeList();

        // 获取父节点ID列表
        List<Long> parentIds = TreeUtils.getParentIds(node, nodes);

        // 获取子节点ID列表
        List<Long> childrenIds = TreeUtils.getChildrenIds(node, nodes);

        // 过滤节点
        return TreeUtils.filterTree(nodes, n -> parentIds.contains(n.getId()) || childrenIds.contains(n.getId()));
    }

    @Override
    public List<T> getNodePath(Long id) {
        // 获取当前节点
        T node = getNode(id);
        if (node == null) {
            return new ArrayList<>();
        }

        // 获取所有节点
        List<T> nodes = getNodeList();

        // 获取父节点ID列表
        List<Long> parentIds = TreeUtils.getParentIds(node, nodes);

        // 按树路径排序
        return nodes.stream()
                .filter(n -> parentIds.contains(n.getId()))
                .sorted((n1, n2) -> n1.getTreePath().compareTo(n2.getTreePath()))
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        if (StringUtils.isEmpty(code)) {
            return true;
        }

        return getNodeList().stream()
                .filter(node -> Objects.equals(code, node.getCode()))
                .allMatch(node -> Objects.equals(excludeId, node.getId()));
    }

    @Override
    public boolean isValidParent(Long parentId, Long id) {
        if (parentId == null || parentId == 0) {
            return true;
        }

        // 父节点不能是自己
        if (Objects.equals(parentId, id)) {
            return false;
        }

        // 父节点必须存在
        T parent = getNode(parentId);
        if (parent == null) {
            return false;
        }

        // 父节点不能是自己的子节点
        if (id != null) {
            List<T> nodes = getNodeList();
            List<Long> childrenIds = TreeUtils.getChildrenIds(getNode(id), nodes);
            return !childrenIds.contains(parentId);
        }

        return true;
    }

    /**
     * 更新子节点的树路径
     */
    private void updateChildrenTreePath(T node, List<T> nodes) {
        List<T> children = getChildren(node.getId());
        if (CollectionUtils.isEmpty(children)) {
            return;
        }

        children.forEach(child -> {
            child.setLevel(node.getLevel() + 1);
            child.setTreePath(TreeUtils.generateTreePath(child, nodes));
            saveNode(child);
            updateChildrenTreePath(child, nodes);
        });
    }
} 