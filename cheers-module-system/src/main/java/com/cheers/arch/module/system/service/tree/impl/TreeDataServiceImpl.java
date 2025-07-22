package com.cheers.arch.module.system.service.tree.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeRespVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelDO;
import com.cheers.arch.module.system.dal.mysql.tree.TreeDataMapper;
import com.cheers.arch.module.system.service.tree.TreeDataService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;
/**
 * 通用树数据 Service 实现类
 */
@Service
@Validated
@Slf4j
public class TreeDataServiceImpl implements TreeDataService {

    @Resource
    private TreeDataMapper treeDataMapper;

    @Override
    public List<TreeNodeRespVO> getTreeData(String treeType) {
        log.info("开始获取树数据，treeType: {}", treeType);
        
        try {
            // 从数据库获取树数据
            List<TreeDataRelDO> treeDataList = treeDataMapper.selectByTreeType(treeType);
            log.info("从数据库获取到 {} 条树数据", treeDataList.size());
            
            // 转换为树形结构
            List<TreeNodeRespVO> result = buildTreeStructure(treeDataList);
            log.info("转换为树形结构后得到 {} 个根节点", result.size());
            
            return result;
        } catch (Exception e) {
            log.error("获取树数据失败，treeType: {}, error: {}", treeType, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public TreeNodeRespVO getTreeNode(String treeType, Long nodeId) {
        TreeDataRelDO nodeData = treeDataMapper.selectOne(
            new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType)
                .eq(TreeDataRelDO::getDataId, nodeId)
        );
            
        if (nodeData == null) {
            return null;
        }
        
        return convertToTreeNodeRespVO(nodeData);
    }

    @Override
    public Long addTreeNode(String treeType, TreeNodeSaveReqVO reqVO) {
        // 创建新的树节点
        TreeDataRelDO treeData = new TreeDataRelDO();
        treeData.setTreeType(treeType);
        treeData.setTreeNodeId(reqVO.getParentId() != null ? reqVO.getParentId() : 0L);
        treeData.setDataType(reqVO.getDataType());
        treeData.setDataId(reqVO.getId());
        treeData.setDataName(reqVO.getName());
        treeData.setDataTypeLabel(getDataTypeLabel(reqVO.getDataType()));
        treeData.setDisplayOrder(reqVO.getDisplayOrder() != null ? reqVO.getDisplayOrder() : 0);
        treeData.setIsRequired(reqVO.getIsRequired() != null ? reqVO.getIsRequired() : false);
        treeData.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : 1);
        treeData.setMetadata(reqVO.getMetadata());
        
        // 保存到数据库
        treeDataMapper.insert(treeData);
        
        return treeData.getId();
    }

    @Override
    public void updateTreeNode(String treeType, Long nodeId, TreeNodeSaveReqVO reqVO) {
        // 使用BaseMapperX的通用方法查找现有节点
        TreeDataRelDO existingNode = treeDataMapper.selectOne(
            new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType)
                .eq(TreeDataRelDO::getDataId, nodeId)
        );
        
        if (existingNode == null) {
            throw new RuntimeException("节点不存在");
        }
        
        // 更新节点数据
        existingNode.setDataName(reqVO.getName());
        existingNode.setDataType(reqVO.getDataType());
        existingNode.setDataTypeLabel(getDataTypeLabel(reqVO.getDataType()));
        existingNode.setDisplayOrder(reqVO.getDisplayOrder() != null ? reqVO.getDisplayOrder() : existingNode.getDisplayOrder());
        existingNode.setIsRequired(reqVO.getIsRequired() != null ? reqVO.getIsRequired() : existingNode.getIsRequired());
        existingNode.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : existingNode.getStatus());
        existingNode.setMetadata(reqVO.getMetadata());
        
        // 更新到数据库
        treeDataMapper.updateById(existingNode);
    }

    @Override
    public void deleteTreeNode(String treeType, Long nodeId) {
        // 使用BaseMapperX的通用方法查找并删除节点
        TreeDataRelDO nodeToDelete = treeDataMapper.selectOne(
            new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType)
                .eq(TreeDataRelDO::getDataId, nodeId)
        );
        
        if (nodeToDelete != null) {
            treeDataMapper.deleteById(nodeToDelete.getId());
        }
    }

    @Override
    public void moveTreeNode(String treeType, Long nodeId, Long targetParentId, Integer position) {
        // 使用BaseMapperX的通用方法查找要移动的节点
        TreeDataRelDO nodeToMove = treeDataMapper.selectOne(
            new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType)
                .eq(TreeDataRelDO::getDataId, nodeId)
        );
        
        if (nodeToMove == null) {
            throw new RuntimeException("要移动的节点不存在");
        }
        
        // 更新父节点ID和排序
        nodeToMove.setTreeNodeId(targetParentId);
        nodeToMove.setDisplayOrder(position != null ? position : nodeToMove.getDisplayOrder());
        
        // 更新到数据库
        treeDataMapper.updateById(nodeToMove);
    }

    /**
     * 构建树形结构
     */
    private List<TreeNodeRespVO> buildTreeStructure(List<TreeDataRelDO> flatData) {
        if (flatData == null || flatData.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 转换为TreeNodeRespVO
        List<TreeNodeRespVO> nodes = flatData.stream()
            .map(this::convertToTreeNodeRespVO)
            .collect(Collectors.toList());
        
        // 构建树形结构
        return buildTree(nodes);
    }

    /**
     * 转换为TreeNodeRespVO
     */
    private TreeNodeRespVO convertToTreeNodeRespVO(TreeDataRelDO treeData) {
        TreeNodeRespVO node = new TreeNodeRespVO();
        node.setId(treeData.getDataId()); // 使用dataId作为节点ID
        node.setName(treeData.getDataName());
        node.setCode(treeData.getDataName()); // 使用数据名称作为编码
        node.setDataType(treeData.getDataType());
        node.setDataTypeLabel(treeData.getDataTypeLabel());
        node.setDisplayOrder(treeData.getDisplayOrder());
        node.setIsRequired(treeData.getIsRequired());
        node.setStatus(treeData.getStatus());
        node.setMetadata(treeData.getMetadata());
        node.setParentId(treeData.getTreeNodeId()); // treeNodeId作为父节点ID，0表示根节点
        
        return node;
    }

    /**
     * 构建树形结构
     */
    private List<TreeNodeRespVO> buildTree(List<TreeNodeRespVO> nodes) {
        List<TreeNodeRespVO> rootNodes = new ArrayList<>();
        
        // 创建ID到节点的映射
        java.util.Map<Long, TreeNodeRespVO> nodeMap = new java.util.HashMap<>();
        for (TreeNodeRespVO node : nodes) {
            nodeMap.put(node.getId(), node);
        }
        
        // 构建树形结构
        for (TreeNodeRespVO node : nodes) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                // 根节点
                rootNodes.add(node);
            } else {
                // 子节点
                TreeNodeRespVO parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }
        
        return rootNodes;
    }

    /**
     * 生成节点ID
     */
    private Long generateNodeId() {
        return System.currentTimeMillis();
    }

    /**
     * 获取数据类型标签
     */
    private String getDataTypeLabel(String dataType) {
        switch (dataType) {
            case "category":
                return "分类";
            case "field":
                return "字段";
            default:
                return "未知";
        }
    }
} 