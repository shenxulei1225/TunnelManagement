package com.cheers.arch.module.system.service.field.impl;

import static com.cheers.arch.module.system.enums.ErrorCodeConstants.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.controller.admin.field.vo.*;
import com.cheers.arch.module.system.dal.dataobject.field.FieldCategoryRelDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldCategoryRelMapper;
import com.cheers.arch.module.system.service.field.FieldCategoryRelService;
import com.cheers.arch.module.system.service.field.FieldService;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 字段分类关联 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
@Slf4j
public class FieldCategoryRelServiceImpl implements FieldCategoryRelService {

    @Resource
    private FieldCategoryRelMapper fieldCategoryRelMapper;

    @Resource
    private FieldService fieldService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createFieldCategoryRel(FieldCategoryRelCreateReqVO createReqVO) {
        // 1. 参数校验
        if (createReqVO == null || createReqVO.getFieldId() == null || createReqVO.getCategoryId() == null) {
            throw ServiceExceptionUtil.exception(FIELD_CATEGORY_REL_NOT_EXISTS, "参数不能为空");
        }

        // 2. 租户权限校验
        Long tenantId = TenantContextHolder.getTenantId();
        
        // 校验字段是否存在且属于当前租户
        FieldDO field = fieldService.getField(createReqVO.getFieldId());
        if (field == null) {
            throw ServiceExceptionUtil.exception(FIELD_NOT_EXISTS);
        }
        
        // 3. 检查关联是否已存在
        FieldCategoryRelDO existing = fieldCategoryRelMapper.selectOne(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, createReqVO.getFieldId())
                .eq(FieldCategoryRelDO::getCategoryId, createReqVO.getCategoryId())
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        );
        
        if (existing != null) {
            // 如果已存在，返回现有记录的ID
            return existing.getId();
        }

        // 4. 创建新关联
        FieldCategoryRelDO fieldCategoryRel = BeanUtils.toBean(createReqVO, FieldCategoryRelDO.class);
        fieldCategoryRel.setTenantId(tenantId);
        
        // 如果未指定排序，自动获取下一个排序值
        if (fieldCategoryRel.getSort() == null) {
            fieldCategoryRel.setSort(getNextSortInCategory(createReqVO.getCategoryId()));
        }
        
        try {
            fieldCategoryRelMapper.insert(fieldCategoryRel);
            return fieldCategoryRel.getId();
        } catch (DuplicateKeyException e) {
            // 并发插入时可能出现重复键异常，重新查询返回现有记录ID
            log.warn("并发创建字段分类关联，重新查询现有记录: fieldId={}, categoryId={}", 
                createReqVO.getFieldId(), createReqVO.getCategoryId());
            existing = fieldCategoryRelMapper.selectOne(
                new LambdaQueryWrapper<FieldCategoryRelDO>()
                    .eq(FieldCategoryRelDO::getFieldId, createReqVO.getFieldId())
                    .eq(FieldCategoryRelDO::getCategoryId, createReqVO.getCategoryId())
                    .eq(FieldCategoryRelDO::getTenantId, tenantId)
            );
            return existing != null ? existing.getId() : fieldCategoryRel.getId();
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldCategoryRel(FieldCategoryRelUpdateReqVO updateReqVO) {
        // 校验存在
        validateFieldCategoryRelExists(updateReqVO.getId());
        // 更新
        FieldCategoryRelDO updateObj = BeanUtils.toBean(updateReqVO, FieldCategoryRelDO.class);
        fieldCategoryRelMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldCategoryRelSort(Long fieldId, Long categoryId, Integer sort) {
        Long tenantId = TenantContextHolder.getTenantId();
        fieldCategoryRelMapper.update(null,
            new LambdaUpdateWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, fieldId)
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
                .set(FieldCategoryRelDO::getSort, sort)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldCategoryRel(Long id) {
        // 校验存在
        validateFieldCategoryRelExists(id);
        // 删除
        fieldCategoryRelMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldCategoryRel(Long fieldId, Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        fieldCategoryRelMapper.delete(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, fieldId)
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldCategoryRelsByFieldId(Long fieldId) {
        Long tenantId = TenantContextHolder.getTenantId();
        fieldCategoryRelMapper.delete(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, fieldId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldCategoryRelsByCategoryId(Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        fieldCategoryRelMapper.delete(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        );
    }

    @Override
    public FieldCategoryRelDO getFieldCategoryRel(Long id) {
        return fieldCategoryRelMapper.selectById(id);
    }

    @Override
    public PageResult<FieldCategoryRelDO> getFieldCategoryRelPage(FieldCategoryRelPageReqVO pageReqVO) {
        return fieldCategoryRelMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<FieldCategoryRelDO>()
            .eqIfPresent(FieldCategoryRelDO::getFieldId, pageReqVO.getFieldId())
            .eqIfPresent(FieldCategoryRelDO::getCategoryId, pageReqVO.getCategoryId())
            .orderByDesc(FieldCategoryRelDO::getId));
    }

    @Override
    public boolean existsFieldCategoryRel(Long fieldId, Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        return fieldCategoryRelMapper.selectCount(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, fieldId)
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        ) > 0;
    }

    @Override
    public List<FieldDO> getFieldsByCategoryId(Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        List<FieldCategoryRelDO> relations = fieldCategoryRelMapper.selectList(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
                .orderByAsc(FieldCategoryRelDO::getSort)
        );
        
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 提取字段ID列表
        List<Long> fieldIds = relations.stream()
            .map(FieldCategoryRelDO::getFieldId)
            .collect(Collectors.toList());
            
        // 通过FieldService获取完整的字段信息
        return fieldService.getFieldList(fieldIds);
    }

    @Override
    public List<Long> getCategoryIdsByFieldId(Long fieldId) {
        Long tenantId = TenantContextHolder.getTenantId();
        List<FieldCategoryRelDO> relations = fieldCategoryRelMapper.selectList(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getFieldId, fieldId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
        );
        return relations.stream()
            .map(FieldCategoryRelDO::getCategoryId)
            .collect(Collectors.toList());
    }

    @Override
    public List<FieldCategoryRelDO> getFieldCategoryRels(List<Long> fieldIds) {
        Long tenantId = TenantContextHolder.getTenantId();
        LambdaQueryWrapper<FieldCategoryRelDO> queryWrapper = new LambdaQueryWrapper<FieldCategoryRelDO>()
            .eq(FieldCategoryRelDO::getTenantId, tenantId);
        
        if (fieldIds != null && !fieldIds.isEmpty()) {
            queryWrapper.in(FieldCategoryRelDO::getFieldId, fieldIds);
        }
        
        return fieldCategoryRelMapper.selectList(queryWrapper);
    }

    @Override
    public Map<Long, Integer> getFieldSortMapInCategory(Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        List<FieldCategoryRelDO> relations = fieldCategoryRelMapper.selectList(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
                .orderByAsc(FieldCategoryRelDO::getSort)
        );
        
        return relations.stream()
            .collect(Collectors.toMap(
                FieldCategoryRelDO::getFieldId,
                rel -> rel.getSort() != null ? rel.getSort() : 0,
                (existing, replacement) -> existing
            ));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldCategoryRels(Long fieldId, List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        
        for (Long categoryId : categoryIds) {
            createFieldCategoryRel(fieldId, categoryId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchCreateFieldCategoryRels(Long fieldId, Map<Long, Integer> categoryIdSortMap) {
        if (categoryIdSortMap == null || categoryIdSortMap.isEmpty()) {
            return;
        }
        
        for (Map.Entry<Long, Integer> entry : categoryIdSortMap.entrySet()) {
            createFieldCategoryRel(fieldId, entry.getKey(), entry.getValue());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldCategoryRels(Long fieldId, List<Long> categoryIds) {
        // 删除现有关联
        deleteFieldCategoryRelsByFieldId(fieldId);
        
        // 创建新关联
        if (categoryIds != null && !categoryIds.isEmpty()) {
            batchCreateFieldCategoryRels(fieldId, categoryIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void replaceFieldCategoryRels(Long fieldId, Map<Long, Integer> categoryIdSortMap) {
        // 删除现有关联
        deleteFieldCategoryRelsByFieldId(fieldId);
        
        // 创建新关联
        if (categoryIdSortMap != null && !categoryIdSortMap.isEmpty()) {
            batchCreateFieldCategoryRels(fieldId, categoryIdSortMap);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFieldSortInCategory(Long categoryId, Map<Long, Integer> fieldIdSortMap) {
        if (fieldIdSortMap == null || fieldIdSortMap.isEmpty()) {
            return;
        }
        
        Long tenantId = TenantContextHolder.getTenantId();
        for (Map.Entry<Long, Integer> entry : fieldIdSortMap.entrySet()) {
            fieldCategoryRelMapper.update(null,
                new LambdaUpdateWrapper<FieldCategoryRelDO>()
                    .eq(FieldCategoryRelDO::getFieldId, entry.getKey())
                    .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                    .eq(FieldCategoryRelDO::getTenantId, tenantId)
                    .set(FieldCategoryRelDO::getSort, entry.getValue())
            );
        }
    }

    @Override
    public Integer getNextSortInCategory(Long categoryId) {
        Long tenantId = TenantContextHolder.getTenantId();
        FieldCategoryRelDO maxSortRel = fieldCategoryRelMapper.selectOne(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .eq(FieldCategoryRelDO::getCategoryId, categoryId)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
                .orderByDesc(FieldCategoryRelDO::getSort)
                .last("LIMIT 1")
        );
        
        return maxSortRel != null && maxSortRel.getSort() != null ? maxSortRel.getSort() + 1 : 1;
    }

    // ==================== 业务操作接口 ====================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean linkFieldCategory(FieldCategoryUpdateReqVO data) {
        if (data.getCategoryId() == null) {
            return false;
        }
        
        FieldCategoryRelCreateReqVO createReqVO = new FieldCategoryRelCreateReqVO();
        createReqVO.setFieldId(data.getFieldId());
        createReqVO.setCategoryId(data.getCategoryId());
        createReqVO.setSort(data.getSort());
        
        createFieldCategoryRel(createReqVO);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateFieldCategory(FieldCategoryUpdateReqVO data) {
        if (data.getCategoryId() == null) {
            return false;
        }
        
        // 检查关联是否存在
        if (existsFieldCategoryRel(data.getFieldId(), data.getCategoryId())) {
            // 存在则更新排序
            if (data.getSort() != null) {
                updateFieldCategoryRelSort(data.getFieldId(), data.getCategoryId(), data.getSort());
            }
        } else {
            // 不存在则创建
            linkFieldCategory(data);
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unlinkFieldCategory(Long fieldId, Long categoryId) {
        deleteFieldCategoryRel(fieldId, categoryId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unlinkAllFieldCategories(Long fieldId) {
        deleteFieldCategoryRelsByFieldId(fieldId);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchUpdateFieldCategory(FieldCategoryRelBatchCreateReqVO data) {
        // 替换字段的所有分类关联
        if (data.getCategoryIdSortMap() != null && !data.getCategoryIdSortMap().isEmpty()) {
            replaceFieldCategoryRels(data.getFieldId(), data.getCategoryIdSortMap());
        } else {
            replaceFieldCategoryRels(data.getFieldId(), data.getCategoryIds());
        }
        
        return true;
    }

    private void validateFieldCategoryRelExists(Long id) {
        if (fieldCategoryRelMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(FIELD_CATEGORY_REL_NOT_EXISTS);
        }
    }
} 