package com.cheers.arch.module.system.service.field.impl;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.FIELD_NOT_EXISTS;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.enums.CommonStatusEnum;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldExportReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldCategoryRelDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldCategoryRelMapper;
import com.cheers.arch.module.system.dal.mysql.field.FieldMapper;
import com.cheers.arch.module.system.dal.mysql.domain.DomainFieldRelMapper;
import com.cheers.arch.module.system.service.field.FieldService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.collection.CollUtil;

/**
 * 字段 Service 实现类
 *
 * @author cheers
 */
@Service
@Validated
public class FieldServiceImpl implements FieldService {

    @Resource
    private FieldMapper fieldMapper;

    @Resource
    private FieldCategoryRelMapper fieldCategoryRelMapper;

    @Resource
    private DomainFieldRelMapper domainFieldRelMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createField(FieldCreateReqVO createReqVO) {
        FieldDO field = BeanUtils.toBean(createReqVO, FieldDO.class);
        field.setStatus(CommonStatusEnum.ENABLE.getStatus());
        fieldMapper.insert(field);
        return field.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateField(FieldUpdateReqVO updateReqVO) {
        // 校验存在
        validateFieldExists(updateReqVO.getId());
        // 更新
        FieldDO updateObj = BeanUtils.toBean(updateReqVO, FieldDO.class);
        fieldMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteField(Long id) {
        // 校验存在
        validateFieldExists(id);
        
        // 删除Field与Domain的关联关系（不删除Domain实体）
        domainFieldRelMapper.deleteByFieldId(id);
        
        // 删除Field与Category的关联关系（如果存在）
        // fieldCategoryRelMapper.deleteByFieldId(id); // 如果需要
        
        // 删除Field实体
        fieldMapper.deleteById(id);
    }

    private void validateFieldExists(Long id) {
        if (fieldMapper.selectById(id) == null) {
            throw exception(FIELD_NOT_EXISTS);
        }
    }

    @Override
    public FieldDO getField(Long id) {
        return fieldMapper.selectById(id);
    }

    @Override
    public List<FieldDO> getFieldList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return fieldMapper.selectList(new LambdaQueryWrapper<FieldDO>().in(FieldDO::getId, ids));
    }

    @Override
    public List<FieldDO> getAllFieldList() {
        return fieldMapper.selectList();
    }

    @Override
    public PageResult<FieldDO> getFieldPage(FieldPageReqVO pageReqVO) {
        return fieldMapper.selectFieldPage(pageReqVO);
    }

    @Override
    public List<FieldDO> getFieldList(FieldExportReqVO exportReqVO) {
        return fieldMapper.selectFieldList(exportReqVO);
    }

    @Override
    public List<FieldDO> getFieldsByCategory(Long categoryId) {
        // 直接查询字段分类关联表，然后获取字段信息
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
            
        // 查询字段信息
        return getFieldList(fieldIds);
    }

    @Override
    public List<FieldDO> getFieldsByCategoryIds(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return Collections.emptyList();
        }
        
        Long tenantId = TenantContextHolder.getTenantId();
        List<FieldCategoryRelDO> relations = fieldCategoryRelMapper.selectList(
            new LambdaQueryWrapper<FieldCategoryRelDO>()
                .in(FieldCategoryRelDO::getCategoryId, categoryIds)
                .eq(FieldCategoryRelDO::getTenantId, tenantId)
                .orderByAsc(FieldCategoryRelDO::getSort)
        );
        
        if (relations.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 提取字段ID列表并去重
        List<Long> fieldIds = relations.stream()
            .map(FieldCategoryRelDO::getFieldId)
            .distinct()
            .collect(Collectors.toList());
            
        // 查询字段信息
        return getFieldList(fieldIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdateFields(List<Long> fieldIds, String updateAction, Object updateValue) {
        if (CollUtil.isEmpty(fieldIds)) {
            return;
        }

        Long tenantId = TenantContextHolder.getTenantId();
        
        switch (updateAction) {
            case "UPDATE_STATUS":
                // 批量更新字段状态
                Integer newStatus = (Integer) updateValue;
                FieldDO updateObj = new FieldDO();
                updateObj.setStatus(newStatus);
                fieldMapper.update(updateObj, 
                    new LambdaQueryWrapperX<FieldDO>()
                        .in(FieldDO::getId, fieldIds)
                        .eq(tenantId != null, FieldDO::getTenantId, tenantId)
                );
                break;
                
            case "BATCH_DELETE":
                // 批量删除字段：删除字段本身及所有关联
                // 1. 先删除相关的分类关联
                fieldCategoryRelMapper.delete(
                    new LambdaQueryWrapper<FieldCategoryRelDO>()
                        .in(FieldCategoryRelDO::getFieldId, fieldIds)
                        .eq(FieldCategoryRelDO::getTenantId, tenantId)
                );
                
                // 2. 删除字段本身
                fieldMapper.delete(new LambdaQueryWrapper<FieldDO>().in(FieldDO::getId, fieldIds));
                break;
                
            default:
                throw new IllegalArgumentException("不支持的批量更新操作: " + updateAction);
        }
    }
} 