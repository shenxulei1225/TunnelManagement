package com.cheers.arch.module.system.dal.mysql.hierarchy;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupRelationDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 系统分级组关系 Mapper
 *
 * @author cheers
 */
@Mapper
public interface HierarchyGroupRelationMapper extends BaseMapperX<HierarchyGroupRelationDO> {

    default List<HierarchyGroupRelationDO> selectListByHierarchyGroupId(Long hierarchyGroupId) {
        return selectList(new LambdaQueryWrapperX<HierarchyGroupRelationDO>()
                .eq(HierarchyGroupRelationDO::getHierarchyGroupId, hierarchyGroupId));
    }

    default List<HierarchyGroupRelationDO> selectListByTarget(String targetType, Long targetId) {
        return selectList(new LambdaQueryWrapperX<HierarchyGroupRelationDO>()
                .eq(HierarchyGroupRelationDO::getTargetType, targetType)
                .eq(HierarchyGroupRelationDO::getTargetId, targetId));
    }

    default void deleteByHierarchyGroupId(Long hierarchyGroupId) {
        delete(new LambdaQueryWrapperX<HierarchyGroupRelationDO>()
                .eq(HierarchyGroupRelationDO::getHierarchyGroupId, hierarchyGroupId));
    }

    default void deleteByTarget(String targetType, Long targetId) {
        delete(new LambdaQueryWrapperX<HierarchyGroupRelationDO>()
                .eq(HierarchyGroupRelationDO::getTargetType, targetType)
                .eq(HierarchyGroupRelationDO::getTargetId, targetId));
    }

} 