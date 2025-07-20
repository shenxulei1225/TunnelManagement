package com.cheers.arch.module.system.service.hierarchy;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupRelationDO;

/**
 * 系统分级组关系 Service 接口
 *
 * @author cheers
 */
public interface HierarchyGroupRelationService {

    /**
     * 创建分级组关系
     *
     * @param hierarchyGroupId 分级组ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     */
    void createHierarchyGroupRelation(Long hierarchyGroupId, String targetType, Long targetId);

    /**
     * 批量创建分级组关系
     *
     * @param hierarchyGroupId 分级组ID
     * @param targetType 目标类型
     * @param targetIds 目标ID列表
     */
    void createHierarchyGroupRelations(Long hierarchyGroupId, String targetType, List<Long> targetIds);

    /**
     * 删除分级组关系
     *
     * @param hierarchyGroupId 分级组ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     */
    void deleteHierarchyGroupRelation(Long hierarchyGroupId, String targetType, Long targetId);

    /**
     * 删除分级组的所有关系
     *
     * @param hierarchyGroupId 分级组ID
     */
    void deleteHierarchyGroupRelations(Long hierarchyGroupId);

    /**
     * 删除目标的所有关系
     *
     * @param targetType 目标类型
     * @param targetId 目标ID
     */
    void deleteTargetRelations(String targetType, Long targetId);

    /**
     * 获取分级组的关系列表
     *
     * @param hierarchyGroupId 分级组ID
     * @return 关系列表
     */
    List<HierarchyGroupRelationDO> getHierarchyGroupRelations(Long hierarchyGroupId);

    /**
     * 获取目标的关系列表
     *
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 关系列表
     */
    List<HierarchyGroupRelationDO> getTargetRelations(String targetType, Long targetId);

    /**
     * 获取目标的分级组ID列表
     *
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 分级组ID列表
     */
    List<Long> getTargetHierarchyGroupIds(String targetType, Long targetId);

} 