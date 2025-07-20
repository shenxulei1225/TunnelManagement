package com.cheers.arch.module.system.dal.mysql.hierarchy;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统分级组 Mapper
 *
 * @author cheers
 */
@Mapper
public interface HierarchyGroupMapper extends BaseMapperX<HierarchyGroupDO> {

    default List<HierarchyGroupDO> selectListByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapperX<HierarchyGroupDO>()
                .eq(HierarchyGroupDO::getParentId, parentId)
                .orderByAsc(HierarchyGroupDO::getSort));
    }

    default List<HierarchyGroupDO> selectListByPath(String path) {
        return selectList(new LambdaQueryWrapperX<HierarchyGroupDO>()
                .likeRight(HierarchyGroupDO::getPath, path)
                .orderByAsc(HierarchyGroupDO::getSort));
    }

    default List<HierarchyGroupDO> selectListByCode(String code) {
        return selectList(new LambdaQueryWrapperX<HierarchyGroupDO>()
                .eq(HierarchyGroupDO::getCode, code)
                .orderByAsc(HierarchyGroupDO::getSort));
    }

} 