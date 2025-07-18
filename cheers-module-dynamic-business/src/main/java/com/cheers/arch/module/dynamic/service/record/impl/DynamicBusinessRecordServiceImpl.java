package com.cheers.arch.module.dynamic.service.record.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.common.exception.ServiceException;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.trees.core.DragOperation;
import com.cheers.arch.framework.trees.utils.TreeUtils;
import com.cheers.arch.module.dynamic.dal.dataobject.record.DynamicBusinessRecordDO;
import com.cheers.arch.module.dynamic.dal.mysql.record.DynamicBusinessRecordMapper;
import com.cheers.arch.module.dynamic.enums.ErrorCodeConstants;
import com.cheers.arch.module.dynamic.service.record.DynamicBusinessRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 动态业务记录服务实现类
 */
@Service
@Slf4j
public class DynamicBusinessRecordServiceImpl implements DynamicBusinessRecordService {

    @Resource
    private DynamicBusinessRecordMapper dynamicBusinessRecordMapper;

    // ==================== TreeService 接口实现 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createNode(DynamicBusinessRecordDO node) {
        return createRecord(node);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateNode(DynamicBusinessRecordDO node) {
        updateRecord(node);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteNode(Long id) {
        deleteRecord(id);
    }

    @Override
    public DynamicBusinessRecordDO getNode(Long id) {
        return getRecord(id);
    }

    @Override
    public List<DynamicBusinessRecordDO> getChildren(Long parentId) {
        LambdaQueryWrapper<DynamicBusinessRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DynamicBusinessRecordDO::getParentId, parentId);
        queryWrapper.orderByAsc(DynamicBusinessRecordDO::getSort);
        return dynamicBusinessRecordMapper.selectList(queryWrapper);
    }

    @Override
    public List<DynamicBusinessRecordDO> getTree() {
        LambdaQueryWrapper<DynamicBusinessRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(DynamicBusinessRecordDO::getSort);
        List<DynamicBusinessRecordDO> allRecords = dynamicBusinessRecordMapper.selectList(queryWrapper);
        return TreeUtils.buildDynamicTree(allRecords, 0L);
    }

    @Override
    public List<DynamicBusinessRecordDO> getSubTree(Long parentId) {
        DynamicBusinessRecordDO parent = dynamicBusinessRecordMapper.selectById(parentId);
        if (parent == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<DynamicBusinessRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.likeRight(DynamicBusinessRecordDO::getTreePath, 
                parent.getTreePath() + "/" + parentId);
        queryWrapper.orderByAsc(DynamicBusinessRecordDO::getSort);
        List<DynamicBusinessRecordDO> subRecords = dynamicBusinessRecordMapper.selectList(queryWrapper);
        return TreeUtils.buildDynamicTree(subRecords, parentId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dragNode(DragOperation.DragRequest<Long> request) {
        Long dragId = request.getDragId();
        Long targetParentId = request.getTargetParentId();

        DynamicBusinessRecordDO dragNode = dynamicBusinessRecordMapper.selectById(dragId);
        if (dragNode == null) {
            return false;
        }

        // 校验目标父节点是否合法
        if (!isValidParent(dragId, targetParentId)) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_RECORD_INVALID_DATA);
        }

        // 更新节点信息
        dragNode.setParentId(targetParentId);
        
        // 更新树路径
        updateTreePath(dragNode);
        
        return dynamicBusinessRecordMapper.updateById(dragNode) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveNode(Long id, Long targetParentId) {
        DynamicBusinessRecordDO node = dynamicBusinessRecordMapper.selectById(id);
        if (node == null) {
            return false;
        }

        // 校验目标父节点是否合法
        if (!isValidParent(id, targetParentId)) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_RECORD_INVALID_DATA);
        }

        // 更新父节点
        node.setParentId(targetParentId);
        updateTreePath(node);
        
        return dynamicBusinessRecordMapper.updateById(node) > 0;
    }

    @Override
    public List<DynamicBusinessRecordDO> getNodePath(Long id) {
        DynamicBusinessRecordDO node = dynamicBusinessRecordMapper.selectById(id);
        if (node == null) {
            return List.of();
        }

        // 根据树路径获取节点路径
        String[] pathIds = node.getTreePath().split("/");
        List<Long> ids = new java.util.ArrayList<>();
        for (String pathId : pathIds) {
            if (!pathId.equals("0") && !pathId.isEmpty()) {
                ids.add(Long.valueOf(pathId));
            }
        }
        ids.add(id);

        if (ids.isEmpty()) {
            return List.of();
        }

        return dynamicBusinessRecordMapper.selectBatchIds(ids);
    }

    @Override
    public boolean isCodeUnique(String code, Long excludeId) {
        LambdaQueryWrapper<DynamicBusinessRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DynamicBusinessRecordDO::getCode, code);
        if (excludeId != null) {
            queryWrapper.ne(DynamicBusinessRecordDO::getId, excludeId);
        }
        return dynamicBusinessRecordMapper.selectCount(queryWrapper) == 0;
    }

    @Override
    public boolean isValidParent(Long parentId, Long id) {
        // 不能将自己设为父节点
        if (id.equals(parentId)) {
            return false;
        }

        // 如果父节点为0，表示根节点，合法
        if (parentId == 0L) {
            return true;
        }

        // 检查父节点是否存在
        DynamicBusinessRecordDO parent = dynamicBusinessRecordMapper.selectById(parentId);
        if (parent == null) {
            return false;
        }

        // 检查是否形成循环引用
        DynamicBusinessRecordDO target = dynamicBusinessRecordMapper.selectById(id);
        if (target == null) {
            return false;
        }

        // 检查目标节点的树路径是否包含当前节点，避免循环引用
        return !target.getTreePath().contains("/" + id + "/") && !target.getTreePath().endsWith("/" + id);
    }

    // ==================== 原有业务方法 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createRecord(DynamicBusinessRecordDO record) {
        if (StringUtils.isEmpty(record.getModelCode())) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_MODEL_CODE_EMPTY);
        }
        if (record.getStatus() == null) {
            record.setStatus(1);
        }
        if (record.getSort() == null) {
            record.setSort(0);
        }
        if (record.getLevel() == null) {
            record.setLevel(0);
        }
        if (record.getReadonly() == null) {
            record.setReadonly(false);
        }
        dynamicBusinessRecordMapper.insert(record);
        updateTreePath(record);
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecord(DynamicBusinessRecordDO record) {
        DynamicBusinessRecordDO oldRecord = validateRecordExists(record.getId());
        dynamicBusinessRecordMapper.updateById(record);
        if (!oldRecord.getParentId().equals(record.getParentId())) {
            updateTreePath(record);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRecord(Long id) {
        DynamicBusinessRecordDO record = validateRecordExists(id);
        List<DynamicBusinessRecordDO> children = dynamicBusinessRecordMapper.selectChildrenByModelCodeAndParentId(
                record.getModelCode(), id);
        if (!children.isEmpty()) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_RECORD_HAS_CHILDREN);
        }
        dynamicBusinessRecordMapper.deleteById(id);
    }

    @Override
    public DynamicBusinessRecordDO getRecord(Long id) {
        return dynamicBusinessRecordMapper.selectById(id);
    }

    @Override
    public List<DynamicBusinessRecordDO> getRecordList(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return dynamicBusinessRecordMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<DynamicBusinessRecordDO> getRecordPage(Integer pageNo, Integer pageSize, String modelCode, Integer status) {
        LambdaQueryWrapperX<DynamicBusinessRecordDO> queryWrapper = new LambdaQueryWrapperX<>();
        queryWrapper.eqIfPresent(DynamicBusinessRecordDO::getModelCode, modelCode);
        queryWrapper.eqIfPresent(DynamicBusinessRecordDO::getStatus, status);
        queryWrapper.orderByAsc(DynamicBusinessRecordDO::getSort);
        Page<DynamicBusinessRecordDO> page = dynamicBusinessRecordMapper.selectPage(new Page<>(pageNo, pageSize), queryWrapper);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<DynamicBusinessRecordDO> getRecordListByModelCode(String modelCode) {
        return dynamicBusinessRecordMapper.selectListByModelCode(modelCode);
    }

    @Override
    public List<DynamicBusinessRecordDO> getRecordTreeByModelCode(String modelCode) {
        List<DynamicBusinessRecordDO> allRecords = dynamicBusinessRecordMapper.selectListByModelCode(modelCode);
        return TreeUtils.buildDynamicTree(allRecords, 0L);
    }

    @Override
    public List<Map<String, Object>> getRecordTreeWithFields(String modelCode, List<String> fields) {
        List<DynamicBusinessRecordDO> allRecords = dynamicBusinessRecordMapper.selectListByModelCode(modelCode);
        return TreeUtils.buildDynamicTreeWithFields(allRecords, 0L, fields);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> batchCreateRecords(List<DynamicBusinessRecordDO> records) {
        if (records == null || records.isEmpty()) {
            return List.of();
        }
        dynamicBusinessRecordMapper.batchInsert(records);
        for (DynamicBusinessRecordDO record : records) {
            updateTreePath(record);
        }
        return records.stream().map(DynamicBusinessRecordDO::getId).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateRecords(List<DynamicBusinessRecordDO> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        dynamicBusinessRecordMapper.batchUpdate(records);
        for (DynamicBusinessRecordDO record : records) {
            updateTreePath(record);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRecords(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        for (Long id : ids) {
            DynamicBusinessRecordDO record = dynamicBusinessRecordMapper.selectById(id);
            if (record != null) {
                List<DynamicBusinessRecordDO> children = dynamicBusinessRecordMapper.selectChildrenByModelCodeAndParentId(
                        record.getModelCode(), id);
                if (!children.isEmpty()) {
                    throw new ServiceException(ErrorCodeConstants.BUSINESS_RECORD_HAS_CHILDREN);
                }
            }
        }
        dynamicBusinessRecordMapper.deleteBatchIds(ids);
    }

    @Override
    public Map<String, Object> importRecords(String modelCode, List<Map<String, Object>> dataList) {
        // TODO: 实现导入逻辑
        return Map.of();
    }

    @Override
    public List<Map<String, Object>> exportRecords(String modelCode, List<Long> recordIds) {
        // TODO: 实现导出逻辑
        return List.of();
    }

    @Override
    public boolean validateRecordData(DynamicBusinessRecordDO record) {
        // TODO: 实现校验逻辑
        return true;
    }

    @Override
    public Map<String, Object> getRecordStatistics(String modelCode) {
        Long totalCount = dynamicBusinessRecordMapper.selectCountByModelCode(modelCode);
        Long enabledCount = dynamicBusinessRecordMapper.selectCountByModelCodeAndStatus(modelCode, 1);
        Long disabledCount = dynamicBusinessRecordMapper.selectCountByModelCodeAndStatus(modelCode, 0);
        return Map.of(
                "total", totalCount,
                "enabled", enabledCount,
                "disabled", disabledCount
        );
    }

    private DynamicBusinessRecordDO validateRecordExists(Long id) {
        DynamicBusinessRecordDO record = dynamicBusinessRecordMapper.selectById(id);
        if (record == null) {
            throw new ServiceException(ErrorCodeConstants.BUSINESS_RECORD_NOT_EXISTS);
        }
        return record;
    }

    private void updateTreePath(DynamicBusinessRecordDO record) {
        if (record.getParentId() == null || record.getParentId() == 0L) {
            // 根节点
            record.setTreePath("0");
            record.setLevel(0);
        } else {
            // 子节点
            DynamicBusinessRecordDO parent = dynamicBusinessRecordMapper.selectById(record.getParentId());
            if (parent != null) {
                record.setTreePath(parent.getTreePath() + "/" + parent.getId());
                record.setLevel(parent.getLevel() + 1);
            }
        }
        
        // 更新当前记录
        dynamicBusinessRecordMapper.updateById(record);
        
        // 更新所有子节点的树路径
        List<DynamicBusinessRecordDO> children = dynamicBusinessRecordMapper.selectChildrenByModelCodeAndParentId(
                record.getModelCode(), record.getId());
        for (DynamicBusinessRecordDO child : children) {
            child.setTreePath(record.getTreePath() + "/" + record.getId());
            child.setLevel(record.getLevel() + 1);
            updateTreePath(child);
        }
    }
} 