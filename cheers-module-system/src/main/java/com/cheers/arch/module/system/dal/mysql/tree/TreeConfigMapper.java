package com.cheers.arch.module.system.dal.mysql.tree;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeConfigDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 树配置 Mapper
 *
 * @author cheers
 */
@Mapper
public interface TreeConfigMapper extends BaseMapperX<TreeConfigDO> {

    /**
     * 根据树类型查询配置
     *
     * @param treeType 树类型
     * @return 树配置
     */
    TreeConfigDO selectByTreeType(@Param("treeType") String treeType);
} 