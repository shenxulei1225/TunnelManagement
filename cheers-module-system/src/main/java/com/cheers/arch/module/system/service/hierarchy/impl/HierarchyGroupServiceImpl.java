package com.cheers.arch.module.system.service.hierarchy.impl;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.HIERARCHY_GROUP_NOT_EXISTS;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

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
import com.cheers.arch.module.system.service.hierarchy.HierarchyGroupService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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

} 