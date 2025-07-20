package com.cheers.arch.module.system.service.field.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.tenant.core.context.TenantContextHolder;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldExportReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldMapper;
import com.cheers.arch.module.system.service.field.FieldService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 字段 Service 实现类
 */
@Service
@Validated
public class FieldServiceImpl implements FieldService {

    @Resource
    private FieldMapper fieldMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createField(FieldCreateReqVO createReqVO) {
        // 1. 插入
        FieldDO field = BeanUtils.toBean(createReqVO, FieldDO.class);
        field.setTenantId(TenantContextHolder.getTenantId()); // 从租户上下文获取
        fieldMapper.insert(field);
        // 2. 返回
        return field.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateField(FieldUpdateReqVO updateReqVO) {
        // 1. 校验存在
        validateFieldExists(updateReqVO.getId());
        // 2. 更新
        FieldDO updateObj = BeanUtils.toBean(updateReqVO, FieldDO.class);
        fieldMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteField(Long id) {
        // 1. 校验存在
        validateFieldExists(id);
        // 2. 删除
        fieldMapper.deleteById(id);
    }

    @Override
    public FieldDO getField(Long id) {
        return fieldMapper.selectById(id);
    }

    @Override
    public List<FieldDO> getFieldList(List<Long> ids) {
        return fieldMapper.selectFieldListByIds(ids);
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

    private void validateFieldExists(Long id) {
        if (fieldMapper.selectById(id) == null) {
            throw new IllegalArgumentException("字段不存在");
        }
    }

} 