package com.cheers.arch.module.system.service.field;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.field.FieldDefHierarchyRelDO;

/**
 * 字段定义-分级组关联 Service 接口
 *
 * @author cheers
 */
public interface FieldDefHierarchyRelService {

    /**
     * 创建字段定义-分级组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupId 分级组 ID
     * @return 关联 ID
     */
    Long createFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId);

    /**
     * 更新字段定义-分级组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupId 分级组 ID
     */
    void updateFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId);

    /**
     * 删除字段定义-分级组关联
     *
     * @param fieldDefId 字段定义 ID
     */
    void deleteFieldDefHierarchyRel(Long fieldDefId);

    /**
     * 根据字段定义 ID 获取分级组 ID（返回第一个关联的分组）
     *
     * @param fieldDefId 字段定义 ID
     * @return 分级组 ID
     */
    Long getHierarchyGroupIdByFieldDefId(Long fieldDefId);

    /**
     * 根据分级组 ID 获取字段定义 ID 列表
     *
     * @param hierarchyGroupId 分级组 ID
     * @return 字段定义 ID 列表
     */
    List<Long> getFieldDefIdsByHierarchyGroupId(Long hierarchyGroupId);

    /**
     * 根据字段定义 ID 列表获取关联信息
     *
     * @param fieldDefIds 字段定义 ID 列表
     * @return 关联信息列表
     */
    List<FieldDefHierarchyRelDO> getFieldDefHierarchyRelsByFieldDefIds(List<Long> fieldDefIds);

    /**
     * 获取字段的所有分组ID
     *
     * @param fieldDefId 字段定义 ID
     * @return 分组ID列表
     */
    List<Long> getAllHierarchyGroupIdsByFieldDefId(Long fieldDefId);

    /**
     * 删除特定的字段-分组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupId 分级组 ID
     */
    void deleteSpecificFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId);

    /**
     * 批量更新字段的分组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupIds 分级组 ID 列表
     */
    void updateFieldDefHierarchyRels(Long fieldDefId, List<Long> hierarchyGroupIds);

} 