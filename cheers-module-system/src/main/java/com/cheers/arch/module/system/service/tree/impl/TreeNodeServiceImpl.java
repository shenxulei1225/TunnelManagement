package com.cheers.arch.module.system.service.tree.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.trees.core.DragOperation;
import com.cheers.arch.framework.trees.service.AbstractTreeService;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeConfigDO;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeNodeDO;
import com.cheers.arch.module.system.dal.mysql.tree.TreeConfigMapper;
import com.cheers.arch.module.system.dal.mysql.tree.TreeNodeMapper;
import com.cheers.arch.module.system.service.tree.TreeNodeService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用树节点 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
@Slf4j
public class TreeNodeServiceImpl extends AbstractTreeService<TreeNodeMapper, TreeNodeDO, Long, TreeNodeDO> 
        implements TreeNodeService {

    @Resource
    private TreeNodeMapper treeNodeMapper;

    @Resource
    private TreeConfigMapper treeConfigMapper;

    // ==================== AbstractTreeService 抽象方法实现 ====================

    @Override
    protected TreeNodeMapper getMapper() {
        return treeNodeMapper;
    }

    @Override
    protected String getTableName() {
        return "system_tree_node";
    }

    @Override
    protected List<TreeNodeDO> getChildrenByParentId(Long parentId) {
        return treeNodeMapper.selectByParentId(parentId != null ? parentId : TreeNodeDO.ROOT_ID);
    }

    // ==================== TreeService 接口实现 ====================

    @Override
    public Long createNode(TreeNodeDO node) {
        validateTreeType(node.getTreeType());
        return super.createNode(node);
    }

    @Override
    public void updateNode(TreeNodeDO node) {
        validateTreeType(node.getTreeType());
        super.updateNode(node);
    }

    @Override
    public void deleteNode(Long id) {
        super.deleteNode(id);
    }

    @Override
    public TreeNodeDO getNode(Long id) {
        return super.getNode(id);
    }

    @Override
    public List<TreeNodeDO> getChildren(Long parentId) {
        return super.getChildren(parentId);
    }

    @Override
    public List<TreeNodeDO> getTree() {
        List<TreeNodeDO> allNodes = treeNodeMapper.selectList();
        return buildTree(allNodes);
    }

    @Override
    public List<TreeNodeDO> getSubTree(Long parentId) {
        List<TreeNodeDO> allNodes = treeNodeMapper.selectList();
        return buildSubTree(allNodes, parentId);
    }

    @Override
    public boolean dragNode(DragOperation.DragRequest<Long> request) {
        // 实现拖拽逻辑
        try {
            moveNode(request.getSourceId(), request.getTargetId());
            return true;
        } catch (Exception e) {
            log.error("拖拽节点失败: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean moveNode(Long id, Long targetParentId) {
        TreeNodeDO node = getNode(id);
        if (node == null) {
            return false;
        }
        
        node.setParentId(targetParentId);
        updateNode(node);
        return true;
    }

    @Override
    public List<TreeNodeDO> getNodePath(Long id) {
        // 根据treePath构建节点路径
        TreeNodeDO node = getNode(id);
        if (node == null || node.getTreePath() == null) {
            return List.of();
        }
        
        // 解析treePath，获取路径上的所有节点ID
        String[] pathIds = node.getTreePath().split("/");
        return List.of(pathIds).stream()
                .filter(pathId -> !pathId.isEmpty())
                .map(Long::valueOf)
                .map(this::getNode)
                .filter(n -> n != null)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        TreeNodeDO existing = treeNodeMapper.selectByCode(code);
        return existing == null || existing.getId().equals(excludeId);
    }

    @Override
    public boolean isValidParent(Long parentId, Long id) {
        if (parentId == null || parentId.equals(TreeNodeDO.ROOT_ID)) {
            return true;
        }
        
        TreeNodeDO parent = getNode(parentId);
        if (parent == null) {
            return false;
        }
        
        // 不能将节点设为自己的子节点
        if (parentId.equals(id)) {
            return false;
        }
        
        // 检查是否会形成循环引用
        List<TreeNodeDO> pathToRoot = getNodePath(parentId);
        return pathToRoot.stream().noneMatch(node -> node.getId().equals(id));
    }

    // ==================== TreeNodeService 接口实现 ====================

    @Override
    public List<TreeNodeDO> getTreeByType(String treeType) {
        List<TreeNodeDO> nodes = treeNodeMapper.selectByTreeType(treeType);
        return buildTree(nodes);
    }

    @Override
    public List<TreeNodeDO> getTreeByTypeAndBusiness(String treeType, String businessType) {
        // 兼容现有test服务的businessType查询
        // 可以通过extraAttrs存储businessType信息
        List<TreeNodeDO> nodes = treeNodeMapper.selectByTreeTypeAndBusiness(treeType, businessType);
        return buildTree(nodes);
    }

    @Override
    public Long createTreeNode(String treeType, String name, String code, Long parentId, Integer sort) {
        TreeNodeDO node = new TreeNodeDO()
                .setTreeType(treeType)
                .setName(name)
                .setCode(code)
                .setParentId(parentId)
                .setSort(sort)
                .setStatus(1)
                .setReadonly(false);
        
        return createNode(node);
    }

    @Override
    public void updateTreeNode(Long id, String name, String code, Long parentId, Integer sort) {
        TreeNodeDO node = getNode(id);
        if (node == null) {
            throw new RuntimeException("节点不存在");
        }
        
        node.setName(name)
            .setCode(code)
            .setParentId(parentId)
            .setSort(sort);
            
        updateNode(node);
    }

    @Override
    public void deleteTreeNode(Long id) {
        deleteNode(id);
    }

    @Override
    public void moveTreeNode(Long id, Long targetParentId, Integer position) {
        TreeNodeDO node = getNode(id);
        if (node == null) {
            throw new RuntimeException("节点不存在");
        }
        
        node.setParentId(targetParentId);
        if (position != null) {
            node.setSort(position);
        }
        
        updateNode(node);
    }

    @Override
    public TreeNodeDO getByCode(String treeType, String code) {
        return treeNodeMapper.selectByTreeTypeAndCode(treeType, code);
    }

    @Override
    public boolean validateTreeType(String treeType) {
        TreeConfigDO config = treeConfigMapper.selectByTreeType(treeType);
        if (config == null) {
            throw new RuntimeException("树类型不存在: " + treeType);
        }
        return true;
    }

    // ==================== 私有方法 ====================

    /**
     * 构建树形结构
     */
    private List<TreeNodeDO> buildTree(List<TreeNodeDO> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return List.of();
        }
        
        // 按sort排序
        nodes.sort((a, b) -> {
            Integer sortA = a.getSort() != null ? a.getSort() : 0;
            Integer sortB = b.getSort() != null ? b.getSort() : 0;
            return sortA.compareTo(sortB);
        });
        
        // 返回根节点
        return nodes.stream()
                .filter(node -> node.getParentId() == null || node.getParentId().equals(TreeNodeDO.ROOT_ID))
                .collect(Collectors.toList());
    }

    /**
     * 构建子树
     */
    private List<TreeNodeDO> buildSubTree(List<TreeNodeDO> allNodes, Long parentId) {
        return allNodes.stream()
                .filter(node -> parentId.equals(node.getParentId()))
                .collect(Collectors.toList());
    }
} 