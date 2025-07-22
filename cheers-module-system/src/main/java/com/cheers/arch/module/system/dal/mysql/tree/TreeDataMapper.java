package com.cheers.arch.module.system.dal.mysql.tree;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 树数据关系 Mapper
 *
 * @author 系统管理员
 */
@Mapper
public interface TreeDataMapper extends BaseMapperX<TreeDataRelDO> {

    /**
     * 根据树类型获取树数据
     *
     * @param treeType 树类型
     * @return 树数据列表
     */
    default List<TreeDataRelDO> selectByTreeType(@Param("treeType") String treeType) {
        return selectList(new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType)
                .orderByAsc(TreeDataRelDO::getDisplayOrder)
                .orderByAsc(TreeDataRelDO::getCreateTime));
    }

    /**
     * 根据树类型删除所有数据
     *
     * @param treeType 树类型
     * @return 删除的记录数
     */
    default int deleteByTreeType(@Param("treeType") String treeType) {
        return delete(new LambdaQueryWrapperX<TreeDataRelDO>()
                .eq(TreeDataRelDO::getTreeType, treeType));
    }
} 