package com.cheers.arch.module.system.service.tree;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationCreateReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelationDO;

/**
 * 通用树结构-数据关联 Service 接口
 *
 * @author cheers
 */
public interface TreeDataRelationService {

    /**
     * 创建树数据关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createTreeDataRelation(TreeDataRelationCreateReqVO createReqVO);

    /**
     * 更新树数据关联
     *
     * @param updateReqVO 更新信息
     */
    void updateTreeDataRelation(TreeDataRelationUpdateReqVO updateReqVO);

    /**
     * 删除树数据关联
     *
     * @param id 编号
     */
    void deleteTreeDataRelation(Long id);

    /**
     * 获得树数据关联
     *
     * @param id 编号
     * @return 树数据关联
     */
    TreeDataRelationDO getTreeDataRelation(Long id);

    /**
     * 获得树数据关联列表
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @return 树数据关联列表
     */
    List<TreeDataRelationDO> getTreeDataRelationList(String treeType, Long treeNodeId);

    /**
     * 获取指定树类型的所有数据关联
     */
    List<TreeDataRelationDO> getAllTreeDataRelations(String treeType);

    /**
     * 获取所有数据（用于测试）
     */
    List<TreeDataRelationDO> getAllData();

    /**
     * 添加数据到树节点
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @param dataType 数据类型
     * @param dataId 数据ID
     * @param dataName 数据名称
     * @param displayOrder 显示顺序
     * @param isRequired 是否必填
     * @param metadata 元数据
     * @return 关联ID
     */
    Long addDataToTreeNode(String treeType, Long treeNodeId, String dataType, Long dataId, 
                          String dataName, Integer displayOrder, Boolean isRequired, String metadata);

    /**
     * 从树节点移除数据
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @param dataType 数据类型
     * @param dataId 数据ID
     */
    void removeDataFromTreeNode(String treeType, Long treeNodeId, String dataType, Long dataId);

    /**
     * 批量添加数据到树节点
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @param dataList 数据列表
     */
    void batchAddDataToTreeNode(String treeType, Long treeNodeId, List<TreeDataRelationCreateReqVO> dataList);

    /**
     * 搜索树节点中的数据
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @param keyword 关键词
     * @param dataType 数据类型
     * @return 搜索结果
     */
    List<TreeDataRelationDO> searchTreeNodeData(String treeType, Long treeNodeId, String keyword, String dataType);

    /**
     * 获取树节点的数据统计
     *
     * @param treeType 树类型
     * @param treeNodeId 树节点ID
     * @return 统计数据
     */
    java.util.Map<String, Long> getTreeNodeDataStats(String treeType, Long treeNodeId);

    /**
     * 获取数据类型在树中的分布
     *
     * @param treeType 树类型
     * @param dataType 数据类型
     * @return 分布数据
     */
    List<TreeDataRelationDO> getDataDistribution(String treeType, String dataType);
} 