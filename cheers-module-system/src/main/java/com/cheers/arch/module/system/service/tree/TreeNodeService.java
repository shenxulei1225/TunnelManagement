package com.cheers.arch.module.system.service.tree;

import java.util.List;

import com.cheers.arch.framework.trees.service.TreeService;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeNodeDO;

/**
 * 通用树节点 Service 接口
 *
 * @author cheers
 */
public interface TreeNodeService extends TreeService<TreeNodeDO> {

    /**
     * 根据树类型获取树形结构
     *
     * @param treeType 树类型
     * @return 树形结构
     */
    List<TreeNodeDO> getTreeByType(String treeType);

    /**
     * 根据树类型和业务条件获取树形结构
     *
     * @param treeType 树类型
     * @param businessType 业务类型（用于兼容现有test服务）
     * @return 树形结构
     */
    List<TreeNodeDO> getTreeByTypeAndBusiness(String treeType, String businessType);

    /**
     * 创建树节点
     *
     * @param treeType 树类型
     * @param name 节点名称
     * @param code 节点编码
     * @param parentId 父节点ID
     * @param sort 排序
     * @return 节点ID
     */
    Long createTreeNode(String treeType, String name, String code, Long parentId, Integer sort);

    /**
     * 更新树节点
     *
     * @param id 节点ID
     * @param name 节点名称
     * @param code 节点编码
     * @param parentId 父节点ID
     * @param sort 排序
     */
    void updateTreeNode(Long id, String name, String code, Long parentId, Integer sort);

    /**
     * 删除树节点
     *
     * @param id 节点ID
     */
    void deleteTreeNode(Long id);

    /**
     * 移动树节点
     *
     * @param id 节点ID
     * @param targetParentId 目标父节点ID
     * @param position 位置
     */
    void moveTreeNode(Long id, Long targetParentId, Integer position);

    /**
     * 根据编码查找节点
     *
     * @param treeType 树类型
     * @param code 节点编码
     * @return 树节点
     */
    TreeNodeDO getByCode(String treeType, String code);

    /**
     * 验证树类型是否存在
     *
     * @param treeType 树类型
     * @return 是否存在
     */
    boolean validateTreeType(String treeType);
} 