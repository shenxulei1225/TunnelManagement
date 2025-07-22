package com.cheers.arch.module.system.service.field;

import java.util.List;
import java.util.Map;

import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldHierarchyRelDO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;

/**
 * 字段-分级组关联 Service 接口
 * 标准CRUD操作，不包含业务逻辑
 */
public interface FieldHierarchyRelService {

    // ========== 基础CRUD操作 ==========

    /**
     * 创建字段-分组关联
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupId 分组ID
     * @return 关联ID
     */
    Long createFieldHierarchyRel(Long fieldId, Long hierarchyGroupId);

    /**
     * 创建字段-分组关联（指定排序）
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupId 分组ID
     * @param sort 排序值
     * @return 关联ID
     */
    Long createFieldHierarchyRel(Long fieldId, Long hierarchyGroupId, Integer sort);

    /**
     * 更新字段-分组关联的排序
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupId 分组ID
     * @param sort 新的排序值
     */
    void updateFieldHierarchyRelSort(Long fieldId, Long hierarchyGroupId, Integer sort);

    /**
     * 删除字段-分组关联
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupId 分组ID
     */
    void deleteFieldHierarchyRel(Long fieldId, Long hierarchyGroupId);

    /**
     * 删除字段的所有分组关联
     *
     * @param fieldId 字段ID
     */
    void deleteFieldHierarchyRelsByFieldId(Long fieldId);

    /**
     * 删除分组的所有字段关联
     *
     * @param hierarchyGroupId 分组ID
     */
    void deleteFieldHierarchyRelsByGroupId(Long hierarchyGroupId);

    /**
     * 批量创建字段-分组关联
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupIds 分组ID列表
     */
    void batchCreateFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds);

    /**
     * 批量创建字段-分组关联（带排序）
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupIdSortMap 分组ID -> 排序值的映射
     */
    void batchCreateFieldHierarchyRels(Long fieldId, Map<Long, Integer> hierarchyGroupIdSortMap);

    /**
     * 批量删除字段-分组关联
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupIds 分组ID列表
     */
    void batchDeleteFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds);

    /**
     * 替换字段的所有分组关联
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupIds 新的分组ID列表
     */
    void replaceFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds);

    /**
     * 替换字段的所有分组关联（带排序）
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupIdSortMap 分组ID -> 排序值的映射
     */
    void replaceFieldHierarchyRels(Long fieldId, Map<Long, Integer> hierarchyGroupIdSortMap);

    /**
     * 批量更新字段在分组中的排序
     *
     * @param hierarchyGroupId 分组ID
     * @param fieldIdSortMap 字段ID -> 排序值的映射
     */
    void batchUpdateFieldSortInGroup(Long hierarchyGroupId, Map<Long, Integer> fieldIdSortMap);

    // ========== 查询方法 ==========

    /**
     * 检查字段-分组关联是否存在
     *
     * @param fieldId 字段ID
     * @param hierarchyGroupId 分组ID
     * @return 是否存在
     */
    boolean existsFieldHierarchyRel(Long fieldId, Long hierarchyGroupId);

    /**
     * 获取字段关联的分组ID列表
     *
     * @param fieldId 字段ID
     * @return 分组ID列表
     */
    List<Long> getHierarchyGroupIdsByFieldId(Long fieldId);

    /**
     * 获取分组关联的字段ID列表
     *
     * @param hierarchyGroupId 分组ID
     * @return 字段ID列表
     */
    List<Long> getFieldIdsByHierarchyGroupId(Long hierarchyGroupId);

    /**
     * 获取字段关联的分组详情列表
     *
     * @param fieldId 字段ID
     * @return 分组详情列表
     */
    List<HierarchyGroupDO> getHierarchyGroupsByFieldId(Long fieldId);

    /**
     * 获取分组关联的字段详情列表
     *
     * @param hierarchyGroupId 分组ID
     * @return 字段详情列表
     */
    List<FieldDO> getFieldsByHierarchyGroupId(Long hierarchyGroupId);

    /**
     * 获取多个字段的分组关联信息
     *
     * @param fieldIds 字段ID列表
     * @return 字段ID -> 分组ID列表的映射
     */
    Map<Long, List<Long>> getHierarchyGroupIdsByFieldIds(List<Long> fieldIds);

    /**
     * 获取所有字段-分组关联记录
     *
     * @param fieldIds 字段ID列表（可选）
     * @return 关联记录列表
     */
    List<FieldHierarchyRelDO> getFieldHierarchyRels(List<Long> fieldIds);

    /**
     * 获取字段在分组中的下一个排序值
     *
     * @param hierarchyGroupId 分组ID
     * @return 下一个排序值
     */
    Integer getNextSortInGroup(Long hierarchyGroupId);

    /**
     * 获取分组中字段的排序映射
     *
     * @param hierarchyGroupId 分组ID
     * @return 字段ID -> 排序值的映射
     */
    Map<Long, Integer> getFieldSortMapInGroup(Long hierarchyGroupId);

    // ========== 统计方法 ==========

    /**
     * 统计分组下的字段数量
     *
     * @param hierarchyGroupId 分组ID
     * @return 字段数量
     */
    Long countFieldsByHierarchyGroupId(Long hierarchyGroupId);

    /**
     * 批量统计多个分组的字段数量
     *
     * @param hierarchyGroupIds 分组ID列表
     * @return 分组ID -> 字段数量的映射
     */
    Map<Long, Long> countFieldsByHierarchyGroupIds(List<Long> hierarchyGroupIds);

} 