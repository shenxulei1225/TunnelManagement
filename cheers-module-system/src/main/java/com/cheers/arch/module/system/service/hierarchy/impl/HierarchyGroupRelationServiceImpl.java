package com.cheers.arch.module.system.service.hierarchy.impl;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupRelationDO;
import com.cheers.arch.module.system.dal.mysql.hierarchy.HierarchyGroupRelationMapper;
import com.cheers.arch.module.system.service.hierarchy.HierarchyGroupRelationService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * 系统分级组关系 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
public class HierarchyGroupRelationServiceImpl implements HierarchyGroupRelationService {

    @Resource
    private HierarchyGroupRelationMapper hierarchyGroupRelationMapper;

    @Override
    public void createHierarchyGroupRelation(Long hierarchyGroupId, String targetType, Long targetId) {
        HierarchyGroupRelationDO relation = HierarchyGroupRelationDO.builder()
                .hierarchyGroupId(hierarchyGroupId)
                .targetType(targetType)
                .targetId(targetId)
                .build();
        hierarchyGroupRelationMapper.insert(relation);
    }

    @Override
    public void createHierarchyGroupRelations(Long hierarchyGroupId, String targetType, List<Long> targetIds) {
        if (targetIds == null || targetIds.isEmpty()) {
            return;
        }
        
        List<HierarchyGroupRelationDO> relations = targetIds.stream()
                .map(targetId -> HierarchyGroupRelationDO.builder()
                        .hierarchyGroupId(hierarchyGroupId)
                        .targetType(targetType)
                        .targetId(targetId)
                        .build())
                .collect(Collectors.toList());
        
        for (HierarchyGroupRelationDO relation : relations) {
            hierarchyGroupRelationMapper.insert(relation);
        }
    }

    @Override
    public void deleteHierarchyGroupRelation(Long hierarchyGroupId, String targetType, Long targetId) {
        hierarchyGroupRelationMapper.delete(new com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX<HierarchyGroupRelationDO>()
                .eq(HierarchyGroupRelationDO::getHierarchyGroupId, hierarchyGroupId)
                .eq(HierarchyGroupRelationDO::getTargetType, targetType)
                .eq(HierarchyGroupRelationDO::getTargetId, targetId));
    }

    @Override
    public void deleteHierarchyGroupRelations(Long hierarchyGroupId) {
        hierarchyGroupRelationMapper.deleteByHierarchyGroupId(hierarchyGroupId);
    }

    @Override
    public void deleteTargetRelations(String targetType, Long targetId) {
        hierarchyGroupRelationMapper.deleteByTarget(targetType, targetId);
    }

    @Override
    public List<HierarchyGroupRelationDO> getHierarchyGroupRelations(Long hierarchyGroupId) {
        return hierarchyGroupRelationMapper.selectListByHierarchyGroupId(hierarchyGroupId);
    }

    @Override
    public List<HierarchyGroupRelationDO> getTargetRelations(String targetType, Long targetId) {
        return hierarchyGroupRelationMapper.selectListByTarget(targetType, targetId);
    }

    @Override
    public List<Long> getTargetHierarchyGroupIds(String targetType, Long targetId) {
        List<HierarchyGroupRelationDO> relations = getTargetRelations(targetType, targetId);
        return relations.stream()
                .map(HierarchyGroupRelationDO::getHierarchyGroupId)
                .collect(Collectors.toList());
    }

} 