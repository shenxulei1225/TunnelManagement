package com.cheers.arch.framework.tree.util;

import com.cheers.arch.framework.tree.core.TreeEntity;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 */
public class TreeUtils {

    /**
     * 构建树形结构
     *
     * @param nodes 节点列表
     * @param rootId 根节点ID
     * @param <T> 实体类型
     * @param <ID> ID类型
     * @return 树形结构
     */
    public static <T extends TreeEntity<ID>, ID> List<T> buildTree(List<T> nodes, ID rootId) {
        // 构建节点映射
        Map<ID, List<T>> childrenMap = nodes.stream()
                .filter(node -> node.getParentId() != null)
                .collect(Collectors.groupingBy(TreeEntity::getParentId));

        // 获取根节点
        List<T> rootNodes = nodes.stream()
                .filter(node -> Objects.equals(node.getParentId(), rootId))
                .collect(Collectors.toList());

        // 递归构建树
        rootNodes.forEach(node -> buildChildren(node, childrenMap));

        return rootNodes;
    }

    /**
     * 递归构建子节点
     */
    private static <T extends TreeEntity<ID>, ID> void buildChildren(T node, Map<ID, List<T>> childrenMap) {
        List<T> children = childrenMap.get(node.getId());
        if (children != null) {
            children.forEach(child -> buildChildren(child, childrenMap));
        }
    }

    /**
     * 获取节点的所有父节点ID列表
     *
     * @param treePath 树路径
     * @return 父节点ID列表
     */
    public static List<String> getParentIds(String treePath) {
        if (treePath == null || treePath.isEmpty()) {
            return Collections.emptyList();
        }

        String[] parts = treePath.split("/");
        return Arrays.stream(parts)
                .filter(part -> !part.isEmpty())
                .collect(Collectors.toList());
    }

    /**
     * 检查是否是祖先节点
     *
     * @param ancestorPath 祖先节点路径
     * @param descendantPath 后代节点路径
     * @return 是否是祖先节点
     */
    public static boolean isAncestor(String ancestorPath, String descendantPath) {
        return descendantPath != null && descendantPath.startsWith(ancestorPath);
    }

    /**
     * 获取树的深度
     *
     * @param treePath 树路径
     * @return 树的深度
     */
    public static int getDepth(String treePath) {
        if (treePath == null || treePath.isEmpty()) {
            return 0;
        }
        return (int) treePath.chars().filter(ch -> ch == '/').count() - 1;
    }
} 