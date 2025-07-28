package com.cheers.arch.module.system.service.hierarchy.impl;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.HIERARCHY_GROUP_NOT_EXISTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Objects;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.common.enums.CommonStatusEnum;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupCreateReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupFlatVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupPageReqVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupTreeVO;
import com.cheers.arch.module.system.controller.admin.hierarchy.vo.HierarchyGroupUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupDO;
import com.cheers.arch.module.system.dal.dataobject.hierarchy.HierarchyGroupRelationDO;
import com.cheers.arch.module.system.dal.mysql.hierarchy.HierarchyGroupMapper;
import com.cheers.arch.module.system.dal.mysql.hierarchy.HierarchyGroupRelationMapper;
import com.cheers.arch.module.system.service.field.FieldHierarchyRelService;
import com.cheers.arch.module.system.service.hierarchy.HierarchyGroupService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统分级组 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
public class HierarchyGroupServiceImpl implements HierarchyGroupService {

    @Resource
    private HierarchyGroupMapper hierarchyGroupMapper;

    @Resource
    private HierarchyGroupRelationMapper hierarchyGroupRelationMapper;

    @Resource
    private FieldHierarchyRelService fieldHierarchyRelService;

    @Override
    public Long createHierarchyGroup(HierarchyGroupCreateReqVO createReqVO) {
        // 插入
        HierarchyGroupDO hierarchyGroup = BeanUtils.toBean(createReqVO, HierarchyGroupDO.class);
        hierarchyGroupMapper.insert(hierarchyGroup);
        // 返回
        return hierarchyGroup.getId();
    }

    @Override
    public void updateHierarchyGroup(HierarchyGroupUpdateReqVO updateReqVO) {
        // 校验存在
        validateHierarchyGroupExists(updateReqVO.getId());
        // 更新
        HierarchyGroupDO updateObj = BeanUtils.toBean(updateReqVO, HierarchyGroupDO.class);
        hierarchyGroupMapper.updateById(updateObj);
    }

    @Override
    public void deleteHierarchyGroup(Long id) {
        // 校验存在
        validateHierarchyGroupExists(id);
        // 删除
        hierarchyGroupMapper.deleteById(id);
        // 删除关联关系
        hierarchyGroupRelationMapper.deleteByHierarchyGroupId(id);
        // 删除字段层级关系
        fieldHierarchyRelService.deleteFieldHierarchyRelsByGroupId(id);
    }

    private void validateHierarchyGroupExists(Long id) {
        if (hierarchyGroupMapper.selectById(id) == null) {
            throw exception(HIERARCHY_GROUP_NOT_EXISTS);
        }
    }

    @Override
    public HierarchyGroupDO getHierarchyGroup(Long id) {
        return hierarchyGroupMapper.selectById(id);
    }
    // 根据ID列表批量获取
    @Override
    public List<HierarchyGroupDO> getHierarchyGroupList(List<Long> ids) {
        return hierarchyGroupMapper.selectList(new LambdaQueryWrapperX<HierarchyGroupDO>()
                .in(HierarchyGroupDO::getId, ids));
    }

    @Override
    public PageResult<HierarchyGroupDO> getHierarchyGroupPage(HierarchyGroupPageReqVO pageReqVO) {
        return hierarchyGroupMapper.selectPage(pageReqVO, new com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX<HierarchyGroupDO>()
                .likeIfPresent(HierarchyGroupDO::getName, pageReqVO.getName())
                .likeIfPresent(HierarchyGroupDO::getCode, pageReqVO.getCode())
                .eqIfPresent(HierarchyGroupDO::getParentId, pageReqVO.getParentId())
                .eqIfPresent(HierarchyGroupDO::getStatus, pageReqVO.getStatus())
                .betweenIfPresent(HierarchyGroupDO::getCreateTime, pageReqVO.getCreateTime())
                .orderByDesc(HierarchyGroupDO::getId));
    }

    

    @Override
    public HierarchyGroupTreeVO getHierarchyGroupTree() {
        // 获取所有分级组
        List<HierarchyGroupDO> allHierarchyGroups = hierarchyGroupMapper.selectList();
        
        // 获取每个分级组的关联对象数量
        Map<Long, Long> hierarchyGroupCountMap = getHierarchyGroupCountMap();
        
        // 构建简化的树形结构，返回单个根节点
        return buildHierarchyGroupTreeVORoot(allHierarchyGroups, hierarchyGroupCountMap);
    }

    /**
     * 构建分级组 Tree VO 根节点
     */
    private HierarchyGroupTreeVO buildHierarchyGroupTreeVORoot(List<HierarchyGroupDO> allHierarchyGroups, Map<Long, Long> hierarchyGroupCountMap) {
        // 按父ID分组
        Map<Long, List<HierarchyGroupDO>> parentIdMap = allHierarchyGroups.stream()
                .collect(Collectors.groupingBy(HierarchyGroupDO::getParentId));
        
        // 构建根节点
        List<HierarchyGroupDO> rootHierarchyGroups = parentIdMap.getOrDefault(0L, new ArrayList<>());
        
        // 创建一个虚拟的根节点，包含所有顶级节点作为子节点
        HierarchyGroupTreeVO rootNode = new HierarchyGroupTreeVO();
        rootNode.setId(0L);
        rootNode.setName("根节点");
        
        // 将顶级节点作为根节点的子节点
        List<HierarchyGroupTreeVO> children = rootHierarchyGroups.stream()
                .map(hierarchyGroup -> buildHierarchyGroupTreeNode(hierarchyGroup, parentIdMap, hierarchyGroupCountMap))
                .collect(Collectors.toList());
        rootNode.setChildren(children);
        
        return rootNode;
    }

    
    // 根据编码获取（用于业务逻辑）
    @Override
    public HierarchyGroupDO getHierarchyGroupByCode(String code) {
        return hierarchyGroupMapper.selectOne(HierarchyGroupDO::getCode, code);
    }

    // 根据父ID获取子分级组列表
    @Override
    public List<HierarchyGroupDO> getHierarchyGroupListByParentId(Long parentId) {
        return hierarchyGroupMapper.selectListByParentId(parentId);
    }

    // 根据路径获取分级组列表
    @Override
    public List<HierarchyGroupDO> getHierarchyGroupListByPath(String path) {
        return hierarchyGroupMapper.selectListByPath(path);
    }

    @Override
    public List<HierarchyGroupFlatVO> getHierarchyGroupFlatList() {
        // 获取所有分级组
        List<HierarchyGroupDO> allHierarchyGroups = hierarchyGroupMapper.selectList();
        
        // 获取每个分级组的关联对象数量
        Map<Long, Long> hierarchyGroupCountMap = getHierarchyGroupCountMap();
        
        // 转换为扁平化VO
        return allHierarchyGroups.stream()
                .map(hierarchyGroup -> {
                    HierarchyGroupFlatVO flatVO = new HierarchyGroupFlatVO();
                    flatVO.setId(hierarchyGroup.getId());
                    flatVO.setName(hierarchyGroup.getName());
                    flatVO.setParentId(hierarchyGroup.getParentId());
                    flatVO.setLevel(hierarchyGroup.getLevel());
                    flatVO.setSort(hierarchyGroup.getSort());
                    flatVO.setStatus(hierarchyGroup.getStatus());
                    flatVO.setCount(hierarchyGroupCountMap.getOrDefault(hierarchyGroup.getId(), 0L));
                    return flatVO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取分级组关联对象数量映射
     */
    private Map<Long, Long> getHierarchyGroupCountMap() {
        List<HierarchyGroupRelationDO> relations = hierarchyGroupRelationMapper.selectList();
        return relations.stream()
                .collect(Collectors.groupingBy(HierarchyGroupRelationDO::getHierarchyGroupId, Collectors.counting()));
    }

    /**
     * 构建分级组 Tree VO 节点
     */
    private HierarchyGroupTreeVO buildHierarchyGroupTreeNode(HierarchyGroupDO hierarchyGroup, Map<Long, List<HierarchyGroupDO>> parentIdMap, Map<Long, Long> hierarchyGroupCountMap) {
        HierarchyGroupTreeVO node = new HierarchyGroupTreeVO();
        node.setId(hierarchyGroup.getId());
        node.setName(hierarchyGroup.getName());
        node.setCode(hierarchyGroup.getCode());
        node.setStatus(hierarchyGroup.getStatus());
        node.setCount(hierarchyGroupCountMap.getOrDefault(hierarchyGroup.getId(), 0L));
        node.setIcon(hierarchyGroup.getIcon()); // 设置图标
        
        // 递归构建子节点
        List<HierarchyGroupDO> children = parentIdMap.getOrDefault(hierarchyGroup.getId(), new ArrayList<>());
        if (!children.isEmpty()) {
            List<HierarchyGroupTreeVO> childNodes = children.stream()
                    .map(child -> buildHierarchyGroupTreeNode(child, parentIdMap, hierarchyGroupCountMap))
                    .collect(Collectors.toList());
            node.setChildren(childNodes);
        }
        
        return node;
    }

    @Override
    public List<HierarchyGroupDO> getHierarchyGroupListByUsageType(String usageType) {
        return hierarchyGroupMapper.selectList(
            new LambdaQueryWrapperX<HierarchyGroupDO>()
                .eq(HierarchyGroupDO::getUsageType, usageType)
                .eq(HierarchyGroupDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(HierarchyGroupDO::getSort)
        );
    }

    @Override
    public List<HierarchyGroupDO> getHierarchyGroupListByUsageTypeAndGroupType(String usageType, String groupType) {
        return hierarchyGroupMapper.selectList(
            new LambdaQueryWrapperX<HierarchyGroupDO>()
                .eq(HierarchyGroupDO::getUsageType, usageType)
                .eq(HierarchyGroupDO::getGroupType, groupType)
                .eq(HierarchyGroupDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                .orderByAsc(HierarchyGroupDO::getSort)
        );
    }

    @Override
    public HierarchyGroupTreeVO getHierarchyGroupTreeByUsageType(String usageType) {
        // 获取指定用途类型的所有分级组
        List<HierarchyGroupDO> hierarchyGroups = getHierarchyGroupListByUsageType(usageType);
        
        if (hierarchyGroups.isEmpty()) {
            return new HierarchyGroupTreeVO();
        }

        // 构建父ID -> 子节点列表的映射
        Map<Long, List<HierarchyGroupDO>> parentIdMap = hierarchyGroups.stream()
                .collect(Collectors.groupingBy(hierarchyGroup -> 
                        hierarchyGroup.getParentId() != null ? hierarchyGroup.getParentId() : 0L));

        // 获取分级组关联对象数量映射
        Map<Long, Long> hierarchyGroupCountMap = getHierarchyGroupCountMap();

        // 找到根节点
        List<HierarchyGroupDO> rootNodes = parentIdMap.getOrDefault(0L, new ArrayList<>());
        
        if (rootNodes.isEmpty()) {
            return new HierarchyGroupTreeVO();
        }

        // 构建树形结构（取第一个根节点作为主根节点）
        HierarchyGroupDO rootHierarchyGroup = rootNodes.get(0);
        HierarchyGroupTreeVO rootNode = buildHierarchyGroupTreeNode(rootHierarchyGroup, parentIdMap, hierarchyGroupCountMap);
        
        // 如果有多个根节点，将其他根节点作为子节点添加
        if (rootNodes.size() > 1) {
            for (int i = 1; i < rootNodes.size(); i++) {
                HierarchyGroupTreeVO siblingNode = buildHierarchyGroupTreeNode(
                        rootNodes.get(i), parentIdMap, hierarchyGroupCountMap);
                rootNode.getChildren().add(siblingNode);
            }
        }
        
        return rootNode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void moveHierarchyGroup(Long hierarchyId, Long targetParentId, Integer targetSort) {
        // 校验移动的分级组存在
        HierarchyGroupDO hierarchyGroup = hierarchyGroupMapper.selectById(hierarchyId);
        if (hierarchyGroup == null) {
            throw new IllegalArgumentException("分级组不存在");
        }

        // 校验目标父分级组的有效性
        if (targetParentId != null && targetParentId != 0) {
            HierarchyGroupDO targetParent = hierarchyGroupMapper.selectById(targetParentId);
            if (targetParent == null) {
                throw new IllegalArgumentException("目标父分级组不存在");
            }
            
            // 防止循环引用：不能移动到自己的子节点下
            if (isDescendant(hierarchyId, targetParentId)) {
                throw new IllegalArgumentException("不能移动到自己的子节点下");
            }
        }

        // 如果目标父分级组和当前父分级组相同，只需要调整排序
        if (Objects.equals(hierarchyGroup.getParentId(), targetParentId)) {
            // 同父级内部调整排序
            adjustSortWithinParent(targetParentId, hierarchyId, targetSort);
        } else {
            // 移动到不同父级
            moveToNewParent(hierarchyId, targetParentId, targetSort);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sortHierarchyGroups(List<Map<String, Object>> sortList) {
        if (sortList == null || sortList.isEmpty()) {
            return;
        }

        // 批量更新排序
        for (Map<String, Object> sortItem : sortList) {
            Long id = Long.valueOf(sortItem.get("id").toString());
            Integer sort = Integer.valueOf(sortItem.get("sort").toString());
            
            HierarchyGroupDO updateObj = new HierarchyGroupDO();
            updateObj.setId(id);
            updateObj.setSort(sort);
            hierarchyGroupMapper.updateById(updateObj);
        }
    }

    @Override
    public List<HierarchyGroupDO> searchHierarchyGroups(String keyword, String usageType) {
        LambdaQueryWrapperX<HierarchyGroupDO> queryWrapper = new LambdaQueryWrapperX<HierarchyGroupDO>()
                .like(HierarchyGroupDO::getName, keyword)
                .or()
                .like(HierarchyGroupDO::getCode, keyword)
                .or()
                .like(HierarchyGroupDO::getDescription, keyword);
        
        if (usageType != null && !usageType.trim().isEmpty()) {
            queryWrapper.eq(HierarchyGroupDO::getUsageType, usageType);
        }
        
        queryWrapper.eq(HierarchyGroupDO::getStatus, CommonStatusEnum.ENABLE.getStatus())
                   .orderByAsc(HierarchyGroupDO::getSort);
        
        return hierarchyGroupMapper.selectList(queryWrapper);
    }

    /**
     * 检查是否为子孙节点
     */
    private boolean isDescendant(Long ancestorId, Long descendantId) {
        HierarchyGroupDO descendant = hierarchyGroupMapper.selectById(descendantId);
        while (descendant != null && descendant.getParentId() != null && descendant.getParentId() != 0) {
            if (Objects.equals(descendant.getParentId(), ancestorId)) {
                return true;
            }
            descendant = hierarchyGroupMapper.selectById(descendant.getParentId());
        }
        return false;
    }

    /**
     * 在同一父级内调整排序
     */
    private void adjustSortWithinParent(Long parentId, Long moveHierarchyId, Integer targetSort) {
        List<HierarchyGroupDO> siblings = hierarchyGroupMapper.selectList(
                new LambdaQueryWrapperX<HierarchyGroupDO>()
                        .eq(HierarchyGroupDO::getParentId, parentId != null ? parentId : 0)
                        .orderByAsc(HierarchyGroupDO::getSort)
        );
        
        if (targetSort == null) {
            targetSort = siblings.size();
        }

        // 重新计算所有兄弟节点的排序
        int newSort = 1;
        for (HierarchyGroupDO sibling : siblings) {
            if (Objects.equals(sibling.getId(), moveHierarchyId)) {
                continue; // 跳过移动的节点
            }
            
            if (newSort == targetSort) {
                newSort++; // 为移动的节点预留位置
            }
            
            if (!Objects.equals(sibling.getSort(), newSort)) {
                HierarchyGroupDO updateObj = new HierarchyGroupDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(newSort);
                hierarchyGroupMapper.updateById(updateObj);
            }
            newSort++;
        }

        // 更新移动节点的排序
        HierarchyGroupDO updateObj = new HierarchyGroupDO();
        updateObj.setId(moveHierarchyId);
        updateObj.setSort(targetSort);
        hierarchyGroupMapper.updateById(updateObj);
    }

    /**
     * 移动到新的父级
     */
    private void moveToNewParent(Long hierarchyId, Long targetParentId, Integer targetSort) {
        // 1. 获取目标父级下的所有子节点
        List<HierarchyGroupDO> targetSiblings = hierarchyGroupMapper.selectList(
                new LambdaQueryWrapperX<HierarchyGroupDO>()
                        .eq(HierarchyGroupDO::getParentId, targetParentId != null ? targetParentId : 0)
                        .orderByAsc(HierarchyGroupDO::getSort)
        );
        
        if (targetSort == null || targetSort > targetSiblings.size() + 1) {
            targetSort = targetSiblings.size() + 1;
        }

        // 2. 调整目标父级下其他节点的排序
        for (HierarchyGroupDO sibling : targetSiblings) {
            if (sibling.getSort() >= targetSort) {
                HierarchyGroupDO updateObj = new HierarchyGroupDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sibling.getSort() + 1);
                hierarchyGroupMapper.updateById(updateObj);
            }
        }

        // 3. 更新移动节点的父级和排序
        HierarchyGroupDO updateObj = new HierarchyGroupDO();
        updateObj.setId(hierarchyId);
        updateObj.setParentId(targetParentId);
        updateObj.setSort(targetSort);
        hierarchyGroupMapper.updateById(updateObj);

        // 4. 重新整理原父级下的排序
        HierarchyGroupDO originalHierarchy = hierarchyGroupMapper.selectById(hierarchyId);
        if (originalHierarchy != null && originalHierarchy.getParentId() != null) {
            reorderSiblings(originalHierarchy.getParentId());
        }
    }

    /**
     * 重新整理兄弟节点的排序，消除空隙
     */
    private void reorderSiblings(Long parentId) {
        List<HierarchyGroupDO> siblings = hierarchyGroupMapper.selectList(
                new LambdaQueryWrapperX<HierarchyGroupDO>()
                        .eq(HierarchyGroupDO::getParentId, parentId != null ? parentId : 0)
                        .orderByAsc(HierarchyGroupDO::getSort)
        );
        
        int sort = 1;
        for (HierarchyGroupDO sibling : siblings) {
            if (!Objects.equals(sibling.getSort(), sort)) {
                HierarchyGroupDO updateObj = new HierarchyGroupDO();
                updateObj.setId(sibling.getId());
                updateObj.setSort(sort);
                hierarchyGroupMapper.updateById(updateObj);
            }
            sort++;
        }
    }

} 