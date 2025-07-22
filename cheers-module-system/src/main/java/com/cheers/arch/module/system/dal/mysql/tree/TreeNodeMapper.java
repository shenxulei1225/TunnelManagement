package com.cheers.arch.module.system.dal.mysql.tree;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeNodeDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通用树节点 Mapper
 *
 * @author cheers
 */
@Mapper
public interface TreeNodeMapper extends BaseMapperX<TreeNodeDO> {

    /**
     * 根据父节点ID查询子节点
     *
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<TreeNodeDO> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据树类型查询节点
     *
     * @param treeType 树类型
     * @return 节点列表
     */
    List<TreeNodeDO> selectByTreeType(@Param("treeType") String treeType);

    /**
     * 根据编码查询节点
     *
     * @param code 节点编码
     * @return 树节点
     */
    TreeNodeDO selectByCode(@Param("code") String code);

    /**
     * 根据树类型和编码查询节点
     *
     * @param treeType 树类型
     * @param code 节点编码
     * @return 树节点
     */
    TreeNodeDO selectByTreeTypeAndCode(@Param("treeType") String treeType, @Param("code") String code);

    /**
     * 根据树类型和业务类型查询节点（兼容test服务）
     *
     * @param treeType 树类型
     * @param businessType 业务类型
     * @return 节点列表
     */
    List<TreeNodeDO> selectByTreeTypeAndBusiness(@Param("treeType") String treeType, @Param("businessType") String businessType);
} 