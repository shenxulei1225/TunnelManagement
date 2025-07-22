package com.cheers.arch.module.system.dal.mysql.tree;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelationDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 通用树结构-数据关联 Mapper
 *
 * @author cheers
 */
@Mapper
public interface TreeDataRelationMapper extends BaseMapperX<TreeDataRelationDO> {

    default List<TreeDataRelationDO> selectListByTreeTypeAndNodeId(String treeType, Long treeNodeId) {
        return selectList(new LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getTreeNodeId, treeNodeId)
                .eq(TreeDataRelationDO::getStatus, 1)
                .orderByAsc(TreeDataRelationDO::getDisplayOrder));
    }

    default List<TreeDataRelationDO> selectListByDataTypeAndDataId(String dataType, Long dataId) {
        return selectList(new LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getDataType, dataType)
                .eq(TreeDataRelationDO::getDataId, dataId)
                .eq(TreeDataRelationDO::getStatus, 1));
    }

    default TreeDataRelationDO selectByTreeTypeAndNodeIdAndDataTypeAndDataId(String treeType, Long treeNodeId, String dataType, Long dataId) {
        return selectOne(new LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getTreeNodeId, treeNodeId)
                .eq(TreeDataRelationDO::getDataType, dataType)
                .eq(TreeDataRelationDO::getDataId, dataId));
    }

    default void deleteByTreeTypeAndNodeId(String treeType, Long treeNodeId) {
        delete(new LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getTreeNodeId, treeNodeId));
    }

    default void deleteByTreeTypeAndNodeIdAndDataTypeAndDataId(String treeType, Long treeNodeId, String dataType, Long dataId) {
        delete(new LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getTreeNodeId, treeNodeId)
                .eq(TreeDataRelationDO::getDataType, dataType)
                .eq(TreeDataRelationDO::getDataId, dataId));
    }
} 