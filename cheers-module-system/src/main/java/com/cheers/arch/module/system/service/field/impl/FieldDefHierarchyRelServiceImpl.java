package com.cheers.arch.module.system.service.field.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.cheers.arch.module.system.dal.dataobject.field.FieldDefHierarchyRelDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldDefHierarchyRelMapper;
import com.cheers.arch.module.system.service.field.FieldDefHierarchyRelService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 字段定义-分级组关联 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
@Slf4j
public class FieldDefHierarchyRelServiceImpl implements FieldDefHierarchyRelService {

    @Resource
    private FieldDefHierarchyRelMapper fieldDefHierarchyRelMapper;

    @Override
    public Long createFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId) {
        // 检查是否已存在相同的关联
        FieldDefHierarchyRelDO existingRel = fieldDefHierarchyRelMapper.selectOne(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getFieldDefId, fieldDefId)
                        .eq(FieldDefHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
        );
        
        if (existingRel != null) {
            // 如果已存在，直接返回现有ID
            return existingRel.getId();
        }
        
        // 创建新关联
        FieldDefHierarchyRelDO rel = FieldDefHierarchyRelDO.builder()
                .fieldDefId(fieldDefId)
                .hierarchyGroupId(hierarchyGroupId)
                .sort(0)
                .build();
        fieldDefHierarchyRelMapper.insert(rel);
        return rel.getId();
    }

    @Override
    public void updateFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId) {
        // 如果 hierarchyGroupId 为 null，则删除该字段的所有关联
        if (hierarchyGroupId == null) {
            deleteFieldDefHierarchyRel(fieldDefId);
            return;
        }
        
        // 对于单个分组更新，先删除所有现有关联，再创建新关联
        deleteFieldDefHierarchyRel(fieldDefId);
        createFieldDefHierarchyRel(fieldDefId, hierarchyGroupId);
    }

    /**
     * 批量更新字段的分组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupIds 分级组 ID 列表
     */
    public void updateFieldDefHierarchyRels(Long fieldDefId, List<Long> hierarchyGroupIds) {
        // 先删除所有现有关联
        deleteFieldDefHierarchyRel(fieldDefId);
        
        // 如果分组列表为空，则完成删除操作
        if (hierarchyGroupIds == null || hierarchyGroupIds.isEmpty()) {
            return;
        }
        
        // 为每个分组创建关联
        for (Long hierarchyGroupId : hierarchyGroupIds) {
            createFieldDefHierarchyRel(fieldDefId, hierarchyGroupId);
        }
    }

    @Override
    public void deleteFieldDefHierarchyRel(Long fieldDefId) {
        fieldDefHierarchyRelMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getFieldDefId, fieldDefId)
        );
    }

    @Override
    public Long getHierarchyGroupIdByFieldDefId(Long fieldDefId) {
        // 由于是1对多关系，这里返回第一个关联的分组ID
        List<FieldDefHierarchyRelDO> rels = fieldDefHierarchyRelMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getFieldDefId, fieldDefId)
                        .orderByAsc(FieldDefHierarchyRelDO::getSort)
                        .last("LIMIT 1")
        );
        return rels.isEmpty() ? null : rels.get(0).getHierarchyGroupId();
    }

    @Override
    public List<Long> getFieldDefIdsByHierarchyGroupId(Long hierarchyGroupId) {
        List<FieldDefHierarchyRelDO> rels = fieldDefHierarchyRelMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
        );
        return rels.stream()
                .map(FieldDefHierarchyRelDO::getFieldDefId)
                .toList();
    }

    @Override
    public List<FieldDefHierarchyRelDO> getFieldDefHierarchyRelsByFieldDefIds(List<Long> fieldDefIds) {
        if (fieldDefIds == null || fieldDefIds.isEmpty()) {
            return List.of();
        }
        return fieldDefHierarchyRelMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .in(FieldDefHierarchyRelDO::getFieldDefId, fieldDefIds)
        );
    }

    /**
     * 获取字段的所有分组ID
     *
     * @param fieldDefId 字段定义 ID
     * @return 分组ID列表
     */
    public List<Long> getAllHierarchyGroupIdsByFieldDefId(Long fieldDefId) {
        List<FieldDefHierarchyRelDO> rels = fieldDefHierarchyRelMapper.selectList(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getFieldDefId, fieldDefId)
                        .orderByAsc(FieldDefHierarchyRelDO::getSort)
        );
        return rels.stream()
                .map(FieldDefHierarchyRelDO::getHierarchyGroupId)
                .toList();
    }

    /**
     * 删除特定的字段-分组关联
     *
     * @param fieldDefId 字段定义 ID
     * @param hierarchyGroupId 分级组 ID
     */
    public void deleteSpecificFieldDefHierarchyRel(Long fieldDefId, Long hierarchyGroupId) {
        fieldDefHierarchyRelMapper.delete(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldDefHierarchyRelDO>()
                        .eq(FieldDefHierarchyRelDO::getFieldDefId, fieldDefId)
                        .eq(FieldDefHierarchyRelDO::getHierarchyGroupId, hierarchyGroupId)
        );
    }

} 