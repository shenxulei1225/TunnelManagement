package com.cheers.arch.module.system.service.tree.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationCreateReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeDataRelationUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.tree.TreeDataRelationDO;
import com.cheers.arch.module.system.dal.mysql.tree.TreeDataRelationMapper;
import com.cheers.arch.module.system.service.tree.TreeDataRelationService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 通用树结构-数据关联 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
@Slf4j
public class TreeDataRelationServiceImpl implements TreeDataRelationService {

    @Resource
    private TreeDataRelationMapper treeDataRelationMapper;

    @Override
    public Long createTreeDataRelation(TreeDataRelationCreateReqVO createReqVO) {
        // 插入
        TreeDataRelationDO treeDataRelation = BeanUtils.toBean(createReqVO, TreeDataRelationDO.class);
        treeDataRelationMapper.insert(treeDataRelation);
        // 返回
        return treeDataRelation.getId();
    }

    @Override
    public void updateTreeDataRelation(TreeDataRelationUpdateReqVO updateReqVO) {
        // 校验存在
        validateTreeDataRelationExists(updateReqVO.getId());
        // 更新
        TreeDataRelationDO updateObj = BeanUtils.toBean(updateReqVO, TreeDataRelationDO.class);
        treeDataRelationMapper.updateById(updateObj);
    }

    @Override
    public void deleteTreeDataRelation(Long id) {
        // 校验存在
        validateTreeDataRelationExists(id);
        // 删除
        treeDataRelationMapper.deleteById(id);
    }

    private void validateTreeDataRelationExists(Long id) {
        if (treeDataRelationMapper.selectById(id) == null) {
            throw new RuntimeException("树数据关联不存在");
        }
    }

    @Override
    public TreeDataRelationDO getTreeDataRelation(Long id) {
        return treeDataRelationMapper.selectById(id);
    }

    @Override
    public List<TreeDataRelationDO> getTreeDataRelationList(String treeType, Long treeNodeId) {
        return treeDataRelationMapper.selectListByTreeTypeAndNodeId(treeType, treeNodeId);
    }

    @Override
    public List<TreeDataRelationDO> getAllTreeDataRelations(String treeType) {
        return treeDataRelationMapper.selectList(new com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getStatus, 1)
                .orderByAsc(TreeDataRelationDO::getDisplayOrder));
    }

    @Override
    public List<TreeDataRelationDO> getAllData() {
        return treeDataRelationMapper.selectList(new com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX<TreeDataRelationDO>()
                .orderByAsc(TreeDataRelationDO::getDisplayOrder));
    }

    @Override
    public Long addDataToTreeNode(String treeType, Long treeNodeId, String dataType, Long dataId, 
                                 String dataName, Integer displayOrder, Boolean isRequired, String metadata) {
        // 检查是否已存在
        TreeDataRelationDO existing = treeDataRelationMapper.selectByTreeTypeAndNodeIdAndDataTypeAndDataId(
                treeType, treeNodeId, dataType, dataId);
        if (existing != null) {
            return existing.getId();
        }

        // 创建新的关联
        TreeDataRelationDO treeDataRelation = new TreeDataRelationDO();
        treeDataRelation.setTreeType(treeType);
        treeDataRelation.setTreeNodeId(treeNodeId);
        treeDataRelation.setDataType(dataType);
        treeDataRelation.setDataId(dataId);
        treeDataRelation.setDataName(dataName);
        treeDataRelation.setDisplayOrder(displayOrder != null ? displayOrder : 0);
        treeDataRelation.setIsRequired(isRequired != null ? isRequired : false);
        treeDataRelation.setMetadata(metadata);
        treeDataRelation.setStatus(1);

        treeDataRelationMapper.insert(treeDataRelation);
        return treeDataRelation.getId();
    }

    @Override
    public void removeDataFromTreeNode(String treeType, Long treeNodeId, String dataType, Long dataId) {
        treeDataRelationMapper.deleteByTreeTypeAndNodeIdAndDataTypeAndDataId(treeType, treeNodeId, dataType, dataId);
    }

    @Override
    public void batchAddDataToTreeNode(String treeType, Long treeNodeId, List<TreeDataRelationCreateReqVO> dataList) {
        for (TreeDataRelationCreateReqVO data : dataList) {
            addDataToTreeNode(treeType, treeNodeId, data.getDataType(), data.getDataId(), 
                    data.getDataName(), data.getDisplayOrder(), data.getIsRequired(), data.getMetadata());
        }
    }

    @Override
    public List<TreeDataRelationDO> searchTreeNodeData(String treeType, Long treeNodeId, String keyword, String dataType) {
        List<TreeDataRelationDO> list = treeDataRelationMapper.selectListByTreeTypeAndNodeId(treeType, treeNodeId);
        
        // 过滤关键词
        if (keyword != null && !keyword.trim().isEmpty()) {
            list = list.stream()
                    .filter(item -> item.getDataName().contains(keyword))
                    .collect(Collectors.toList());
        }
        
        // 过滤数据类型
        if (dataType != null && !dataType.trim().isEmpty()) {
            list = list.stream()
                    .filter(item -> dataType.equals(item.getDataType()))
                    .collect(Collectors.toList());
        }
        
        return list;
    }

    @Override
    public Map<String, Long> getTreeNodeDataStats(String treeType, Long treeNodeId) {
        List<TreeDataRelationDO> list = treeDataRelationMapper.selectListByTreeTypeAndNodeId(treeType, treeNodeId);
        
        return list.stream()
                .collect(Collectors.groupingBy(
                        TreeDataRelationDO::getDataType,
                        Collectors.counting()
                ));
    }

    @Override
    public List<TreeDataRelationDO> getDataDistribution(String treeType, String dataType) {
        return treeDataRelationMapper.selectList(new com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX<TreeDataRelationDO>()
                .eq(TreeDataRelationDO::getTreeType, treeType)
                .eq(TreeDataRelationDO::getDataType, dataType)
                .eq(TreeDataRelationDO::getStatus, 1)
                .orderByAsc(TreeDataRelationDO::getDisplayOrder));
    }
} 