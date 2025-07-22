package com.cheers.arch.module.system.dal.mysql.tree;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeConfigDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 树结构配置 Mapper
 *
 * @author cheers
 */
@Mapper
public interface TreeConfigMapper extends BaseMapperX<TreeConfigDO> {

    default TreeConfigDO selectByTreeType(String treeType) {
        return selectOne(new LambdaQueryWrapperX<TreeConfigDO>()
                .eq(TreeConfigDO::getTreeType, treeType)
                .eq(TreeConfigDO::getStatus, 1));
    }

    default List<TreeConfigDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<TreeConfigDO>()
                .eq(TreeConfigDO::getStatus, status)
                .orderByAsc(TreeConfigDO::getTreeType));
    }

    default List<TreeConfigDO> selectList() {
        return selectList(new LambdaQueryWrapperX<TreeConfigDO>()
                .eq(TreeConfigDO::getStatus, 1)
                .orderByAsc(TreeConfigDO::getTreeType));
    }
} 