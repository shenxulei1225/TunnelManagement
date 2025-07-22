package com.cheers.arch.module.system.service.tree;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeRespVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeSaveReqVO;

/**
 * 通用树数据 Service 接口
 */
public interface TreeDataService {

    /**
     * 获取树数据
     *
     * @param treeType 树类型
     * @return 树数据列表
     */
    List<TreeNodeRespVO> getTreeData(String treeType);

    /**
     * 获取树节点详情
     *
     * @param treeType 树类型
     * @param nodeId 节点ID
     * @return 树节点详情
     */
    TreeNodeRespVO getTreeNode(String treeType, Long nodeId);

    /**
     * 添加树节点
     *
     * @param treeType 树类型
     * @param reqVO 请求参数
     * @return 节点ID
     */
    Long addTreeNode(String treeType, TreeNodeSaveReqVO reqVO);

    /**
     * 更新树节点
     *
     * @param treeType 树类型
     * @param nodeId 节点ID
     * @param reqVO 请求参数
     */
    void updateTreeNode(String treeType, Long nodeId, TreeNodeSaveReqVO reqVO);

    /**
     * 删除树节点
     *
     * @param treeType 树类型
     * @param nodeId 节点ID
     */
    void deleteTreeNode(String treeType, Long nodeId);

    /**
     * 移动树节点
     *
     * @param treeType 树类型
     * @param nodeId 节点ID
     * @param targetParentId 目标父节点ID
     * @param position 位置
     */
    void moveTreeNode(String treeType, Long nodeId, Long targetParentId, Integer position);
} 