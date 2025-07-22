package com.cheers.arch.module.system.service.field.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldHierarchyRelDO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldHierarchyRelMapper;
import com.cheers.arch.module.system.dal.mysql.field.FieldMapper;
import com.cheers.arch.module.system.dal.mysql.hierarchy.HierarchyGroupMapper;
import com.cheers.arch.module.system.service.field.FieldHierarchyRelService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 字段-分级组关联 Service 实现类
 * 标准CRUD操作，不包含业务逻辑
 */
@Service
@Validated
@Slf4j
public class FieldHierarchyRelServiceImpl implements FieldHierarchyRelService {

    @Resource
    private FieldHierarchyRelMapper fieldHierarchyRelMapper;

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private HierarchyGroupMapper hierarchyGroupMapper;

    // ========== 基础CRUD操作 ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFieldHierarchyRel(Long fieldId, Long hierarchyGroupId) {
        return createFieldHierarchyRel(fieldId, hierarchyGroupId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFieldHierarchyRel(Long fieldId, Long hierarchyGroupId, Integer sort) {
        if (fieldId == null || hierarchyGroupId == null) {
            throw new IllegalArgumentException("fieldId 和 hierarchyGroupId 不能为空");
        }

        // 获取当前租户ID
        Long currentTenantId = TenantContextHolder.getTenantId();

        // 检查是否已存在相同的关联
        FieldHierarchyRelDO existingRel = fieldHierarchyRelMapper.selectOne(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        if (existingRel != null) {
            log.debug("字段 {} 与分组 {} 的关联已存在，返回现有ID: {}", fieldId, hierarchyGroupId, existingRel.getId());
            return existingRel.getId();
        }
        
        // 如果没有指定排序值，则自动获取下一个排序值
        if (sort == null) {
            sort = getNextSortInGroup(hierarchyGroupId);
        }
        
        try {
            // 创建新关联
            FieldHierarchyRelDO rel = FieldHierarchyRelDO.builder()
                    .fieldId(fieldId)
                    .hierarchyGroupId(hierarchyGroupId)
                    .sort(sort)
                    .build();
            fieldHierarchyRelMapper.insert(rel);
            log.debug("成功创建字段 {} 与分组 {} 的关联，ID: {}，排序: {}", fieldId, hierarchyGroupId, rel.getId(), sort);
            return rel.getId();
        } catch (org.springframework.dao.DuplicateKeyException e) {
            // 并发情况下可能出现重复键异常，再次查询现有记录
            log.warn("创建字段 {} 与分组 {} 关联时出现重复键异常，尝试查询现有记录", fieldId, hierarchyGroupId);
            existingRel = fieldHierarchyRelMapper.selectOne(
                    new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                            .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                            .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                            .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
            );
            if (existingRel != null) {
                return existingRel.getId();
            }
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldHierarchyRelSort(Long fieldId, Long hierarchyGroupId, Integer sort) {
        if (fieldId == null || hierarchyGroupId == null || sort == null) {
            throw new IllegalArgumentException("fieldId、hierarchyGroupId 和 sort 不能为空");
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        FieldHierarchyRelDO updateObj = FieldHierarchyRelDO.builder()
                .sort(sort)
                .build();
        
        int updated = fieldHierarchyRelMapper.update(updateObj,
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        if (updated > 0) {
            log.debug("更新字段 {} 在分组 {} 中的排序为: {}", fieldId, hierarchyGroupId, sort);
        } else {
            log.warn("未找到字段 {} 与分组 {} 的关联记录，无法更新排序", fieldId, hierarchyGroupId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldHierarchyRel(Long fieldId, Long hierarchyGroupId) {
        if (fieldId == null || hierarchyGroupId == null) {
            throw new IllegalArgumentException("fieldId 和 hierarchyGroupId 不能为空");
        }
        Long currentTenantId = TenantContextHolder.getTenantId();
        fieldHierarchyRelMapper.delete(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        log.debug("已删除字段 {} 与分组 {} 的关联", fieldId, hierarchyGroupId);
    }
    // 删除字段的所有分组关联 ，用于删除字段时，删除所有关联
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldHierarchyRelsByFieldId(Long fieldId) {
        if (fieldId == null) {
            throw new IllegalArgumentException("fieldId 不能为空");
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        fieldHierarchyRelMapper.delete(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        log.debug("已删除字段 {} 的所有分组关联", fieldId);
    }
    
    // 删除分组的所有字段关联 ，用于删除分组时，删除所有关联
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldHierarchyRelsByGroupId(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            throw new IllegalArgumentException("hierarchyGroupId 不能为空");
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        fieldHierarchyRelMapper.delete(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        log.debug("已删除分组 {} 的所有字段关联", hierarchyGroupId);
    }

    // 批量创建字段与分组的关联 ，用于批量创建字段与分组的关联，在弹窗窗口内选择多个分组
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds) {
        if (fieldId == null || hierarchyGroupIds == null) {
            throw new IllegalArgumentException("fieldId 和 hierarchyGroupIds 不能为空");
        }

        for (Long hierarchyGroupId : hierarchyGroupIds) {
            createFieldHierarchyRel(fieldId, hierarchyGroupId);
        }
        
        log.debug("批量创建字段 {} 与 {} 个分组的关联", fieldId, hierarchyGroupIds.size());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldHierarchyRels(Long fieldId, Map<Long, Integer> hierarchyGroupIdSortMap) {
        if (fieldId == null || hierarchyGroupIdSortMap == null) {
            throw new IllegalArgumentException("fieldId 和 hierarchyGroupIdSortMap 不能为空");
        }

        for (Map.Entry<Long, Integer> entry : hierarchyGroupIdSortMap.entrySet()) {
            createFieldHierarchyRel(fieldId, entry.getKey(), entry.getValue());
        }
        
        log.debug("批量创建字段 {} 与 {} 个分组的关联（带排序）", fieldId, hierarchyGroupIdSortMap.size());
    }

    // 批量删除字段与分组的关联 ，用于批量删除字段与分组的关联 
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds) {
        if (fieldId == null || hierarchyGroupIds == null) {
            throw new IllegalArgumentException("fieldId 和 hierarchyGroupIds 不能为空");
        }

        for (Long hierarchyGroupId : hierarchyGroupIds) {
            deleteFieldHierarchyRel(fieldId, hierarchyGroupId);
        }
        
        log.debug("批量删除字段 {} 与 {} 个分组的关联", fieldId, hierarchyGroupIds.size());
    }
   
    // 替换字段与分组的关联 ，用于替换字段与分组的关联，在编辑弹窗窗口内选择多个分组
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldHierarchyRels(Long fieldId, List<Long> hierarchyGroupIds) {
        if (fieldId == null) {
            throw new IllegalArgumentException("fieldId 不能为空");
        }

        // 删除现有所有关联
        deleteFieldHierarchyRelsByFieldId(fieldId);
        
        // 创建新关联
        if (hierarchyGroupIds != null && !hierarchyGroupIds.isEmpty()) {
            batchCreateFieldHierarchyRels(fieldId, hierarchyGroupIds);
        }
        
        log.debug("替换字段 {} 的分组关联，新关联数量: {}", fieldId, 
                hierarchyGroupIds != null ? hierarchyGroupIds.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldHierarchyRels(Long fieldId, Map<Long, Integer> hierarchyGroupIdSortMap) {
        if (fieldId == null) {
            throw new IllegalArgumentException("fieldId 不能为空");
        }

        // 删除现有所有关联
        deleteFieldHierarchyRelsByFieldId(fieldId);
        
        // 创建新关联（带排序）
        if (hierarchyGroupIdSortMap != null && !hierarchyGroupIdSortMap.isEmpty()) {
            batchCreateFieldHierarchyRels(fieldId, hierarchyGroupIdSortMap);
        }
        
        log.debug("替换字段 {} 的分组关联（带排序），新关联数量: {}", fieldId, 
                hierarchyGroupIdSortMap != null ? hierarchyGroupIdSortMap.size() : 0);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFieldSortInGroup(Long hierarchyGroupId, Map<Long, Integer> fieldIdSortMap) {
        if (hierarchyGroupId == null || fieldIdSortMap == null) {
            throw new IllegalArgumentException("hierarchyGroupId 和 fieldIdSortMap 不能为空");
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        for (Map.Entry<Long, Integer> entry : fieldIdSortMap.entrySet()) {
            Long fieldId = entry.getKey();
            Integer sort = entry.getValue();
            
            FieldHierarchyRelDO updateObj = FieldHierarchyRelDO.builder()
                    .sort(sort)
                    .build();
            
            fieldHierarchyRelMapper.update(updateObj,
                    new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                            .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                            .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                            .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
            );
        }
        
        log.debug("批量更新分组 {} 中 {} 个字段的排序", hierarchyGroupId, fieldIdSortMap.size());
    }

    // ========== 查询方法 ==========

    @Override
    public boolean existsFieldHierarchyRel(Long fieldId, Long hierarchyGroupId) {
        if (fieldId == null || hierarchyGroupId == null) {
            return false;
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        Long count = fieldHierarchyRelMapper.selectCount(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        return count > 0;
    }

    @Override
    public List<Long> getHierarchyGroupIdsByFieldId(Long fieldId) {
        if (fieldId == null) {
            return List.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        return rels.stream()
                .map(FieldHierarchyRelDO::getHierarchyGroupId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Long> getFieldIdsByHierarchyGroupId(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            return List.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        return rels.stream()
                .map(FieldHierarchyRelDO::getFieldId)
                .collect(Collectors.toList());
    }

    @Override
    public List<HierarchyGroupDO> getHierarchyGroupsByFieldId(Long fieldId) {
        if (fieldId == null) {
            return List.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getFieldId, fieldId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        Set<Long> hierarchyGroupIds = new HashSet<>();
        for (FieldHierarchyRelDO rel : rels) {
            hierarchyGroupIds.add(rel.getHierarchyGroupId());
        }

        if (hierarchyGroupIds.isEmpty()) {
            return List.of();
        }

        List<HierarchyGroupDO> hierarchyGroups = hierarchyGroupMapper.selectBatchIds(new HashSet<>(hierarchyGroupIds));
        return hierarchyGroups.stream()
                .filter(group -> hierarchyGroupIds.contains(group.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FieldDO> getFieldsByHierarchyGroupId(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            return List.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        Set<Long> fieldIds = new HashSet<>();
        for (FieldHierarchyRelDO rel : rels) {
            fieldIds.add(rel.getFieldId());
        }

        if (fieldIds.isEmpty()) {
            return List.of();
        }

        List<FieldDO> fields = fieldMapper.selectBatchIds(new HashSet<>(fieldIds));
        return fields.stream()
                .filter(field -> fieldIds.contains(field.getId()))
                .collect(Collectors.toList());
    }
    @Override
    public Map<Long, List<Long>> getHierarchyGroupIdsByFieldIds(List<Long> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return Map.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .in(FieldHierarchyRelDO::getFieldId, fieldIds)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        return rels.stream()
                .collect(Collectors.groupingBy(
                        FieldHierarchyRelDO::getFieldId,
                        Collectors.mapping(FieldHierarchyRelDO::getHierarchyGroupId, Collectors.toList())
                ));
    }

    @Override
    public List<FieldHierarchyRelDO> getFieldHierarchyRels(List<Long> fieldIds) {
        Long currentTenantId = TenantContextHolder.getTenantId();
        
        LambdaQueryWrapperX<FieldHierarchyRelDO> wrapper = new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId);
        
        if (fieldIds != null && !fieldIds.isEmpty()) {
            wrapper.in(FieldHierarchyRelDO::getFieldId, fieldIds);
        }
        
        return fieldHierarchyRelMapper.selectList(wrapper);
    }

    @Override
    public Integer getNextSortInGroup(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            return 0;
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        // 查询当前分组中的最大排序值
        FieldHierarchyRelDO maxSortRel = fieldHierarchyRelMapper.selectOne(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
                        .orderByDesc(FieldHierarchyRelDO::getSort)
                        .last("LIMIT 1")
        );
        
        return maxSortRel != null ? maxSortRel.getSort() + 10 : 10;
    }

    @Override
    public Map<Long, Integer> getFieldSortMapInGroup(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            return Map.of();
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
                        .orderByAsc(FieldHierarchyRelDO::getSort)
        );
        
        return rels.stream()
                .collect(Collectors.toMap(
                        FieldHierarchyRelDO::getFieldId,
                        FieldHierarchyRelDO::getSort,
                        (existing, replacement) -> existing
                ));
    }

    // ========== 统计方法 ==========

    @Override
    public Long countFieldsByHierarchyGroupId(Long hierarchyGroupId) {
        if (hierarchyGroupId == null) {
            return 0L;
        }

        Long currentTenantId = TenantContextHolder.getTenantId();
        
        return fieldHierarchyRelMapper.selectCount(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .eq(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
    }

    @Override
    public Map<Long, Long> countFieldsByHierarchyGroupIds(List<Long> hierarchyGroupIds) {
        if (hierarchyGroupIds == null || hierarchyGroupIds.isEmpty()) {
            return Map.of();
        }
        
        Long currentTenantId = TenantContextHolder.getTenantId();
        
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelMapper.selectList(
                new LambdaQueryWrapperX<FieldHierarchyRelDO>()
                        .in(FieldHierarchyRelDO::getHierarchyGroupId, hierarchyGroupIds)
                        .eq(currentTenantId != null, FieldHierarchyRelDO::getTenantId, currentTenantId)
        );
        
        return rels.stream()
                .collect(Collectors.groupingBy(
                        FieldHierarchyRelDO::getHierarchyGroupId,
                        Collectors.counting()
                ));
    }

} 