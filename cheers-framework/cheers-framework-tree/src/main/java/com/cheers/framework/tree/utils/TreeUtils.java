package com.cheers.arch.framework.tree.utils;

import com.cheers.arch.framework.tree.core.TreeNode;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 */
public class TreeUtils {

    private static final String PATH_SEPARATOR = "/";

    /**
     * 构建树形结构
     *
     * @param nodes 节点列表
     * @return 树形结构
     */
    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList<>();
        }

        // 构建节点Map
        Map<Long, T> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));

        List<T> tree = new ArrayList<>();
        nodes.forEach(node -> {
            Long parentId = node.getParentId();
            if (parentId == null || parentId == 0) {
                tree.add(node);
                return;
            }

            T parent = nodeMap.get(parentId);
            if (parent != null) {
                parent.addChild(node);
            }
        });

        return tree;
    }

    /**
     * 生成树路径
     *
     * @param node 当前节点
     * @param nodes 所有节点
     * @return 树路径
     */
    public static <T extends TreeNode<T>> String generateTreePath(T node, List<T> nodes) {
        if (node == null) {
            return PATH_SEPARATOR;
        }

        // 构建节点Map
        Map<Long, T> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));

        List<String> paths = new ArrayList<>();
        paths.add(String.valueOf(node.getId()));

        // 向上查找父节点
        T current = node;
        while (current.getParentId() != null && current.getParentId() != 0) {
            T parent = nodeMap.get(current.getParentId());
            if (parent == null) {
                break;
            }
            paths.add(String.valueOf(parent.getId()));
            current = parent;
        }

        // 反转并拼接路径
        Collections.reverse(paths);
        return PATH_SEPARATOR + StringUtils.join(paths, PATH_SEPARATOR) + PATH_SEPARATOR;
    }

    /**
     * 获取所有子节点ID（包含当前节点）
     *
     * @param node 当前节点
     * @param nodes 所有节点
     * @return 子节点ID列表
     */
    public static <T extends TreeNode<T>> List<Long> getChildrenIds(T node, List<T> nodes) {
        List<Long> ids = new ArrayList<>();
        ids.add(node.getId());

        // 查找直接子节点
        List<T> children = nodes.stream()
                .filter(n -> Objects.equals(n.getParentId(), node.getId()))
                .collect(Collectors.toList());

        // 递归查找所有子节点
        children.forEach(child -> ids.addAll(getChildrenIds(child, nodes)));

        return ids;
    }

    /**
     * 获取所有父节点ID（包含当前节点）
     *
     * @param node 当前节点
     * @param nodes 所有节点
     * @return 父节点ID列表
     */
    public static <T extends TreeNode<T>> List<Long> getParentIds(T node, List<T> nodes) {
        List<Long> ids = new ArrayList<>();
        ids.add(node.getId());

        // 构建节点Map
        Map<Long, T> nodeMap = nodes.stream()
                .collect(Collectors.toMap(TreeNode::getId, Function.identity()));

        // 向上查找父节点
        T current = node;
        while (current.getParentId() != null && current.getParentId() != 0) {
            T parent = nodeMap.get(current.getParentId());
            if (parent == null) {
                break;
            }
            ids.add(parent.getId());
            current = parent;
        }

        return ids;
    }

    /**
     * 过滤树形结构
     *
     * @param nodes 树形结构
     * @param filter 过滤函数
     * @return 过滤后的树形结构
     */
    public static <T extends TreeNode<T>> List<T> filterTree(List<T> nodes, Function<T, Boolean> filter) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList<>();
        }

        return nodes.stream()
                .filter(node -> {
                    if (node instanceof TreeNode) {
                        List<T> children = filterTree(((TreeNode<T>) node).getChildren(), filter);
                        ((TreeNode<T>) node).clearChildren();
                        children.forEach(child -> ((TreeNode<T>) node).addChild(child));
                    }
                    return filter.apply(node) || !((TreeNode<T>) node).getChildren().isEmpty();
                })
                .collect(Collectors.toList());
    }

    /**
     * 遍历树形结构
     *
     * @param nodes 树形结构
     * @param consumer 节点处理函数
     */
    public static <T extends TreeNode<T>> void traverseTree(List<T> nodes, Function<T, Boolean> consumer) {
        if (CollectionUtils.isEmpty(nodes)) {
            return;
        }

        nodes.forEach(node -> {
            if (consumer.apply(node) && node instanceof TreeNode) {
                traverseTree(((TreeNode<T>) node).getChildren(), consumer);
            }
        });
    }
} 