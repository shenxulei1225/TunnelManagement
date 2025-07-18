package com.cheers.arch.module.system.service.field.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
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
        return fieldMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<FieldDO> getFieldPage(FieldPageReqVO pageReqVO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<FieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(FieldDO::getFieldKey, pageReqVO.getFieldKey())
                .like(FieldDO::getFieldLabel, pageReqVO.getFieldLabel())
                .eq(FieldDO::getValueType, pageReqVO.getValueType())
                .between(pageReqVO.getCreateTime() != null && pageReqVO.getCreateTime().length == 2, 
                        FieldDO::getCreateTime, pageReqVO.getCreateTime()[0], pageReqVO.getCreateTime()[1])
                .orderByDesc(FieldDO::getId);
        
        // 2. 执行分页查询
        return fieldMapper.selectPage(pageReqVO, queryWrapper);
    }

    @Override
    public List<FieldDO> getFieldList(FieldExportReqVO exportReqVO) {
        // 1. 构建查询条件
        LambdaQueryWrapper<FieldDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(FieldDO::getFieldKey, exportReqVO.getFieldKey())
                .like(FieldDO::getFieldLabel, exportReqVO.getFieldLabel())
                .eq(FieldDO::getValueType, exportReqVO.getValueType())
                .between(exportReqVO.getCreateTime() != null && exportReqVO.getCreateTime().length == 2, 
                        FieldDO::getCreateTime, exportReqVO.getCreateTime()[0], exportReqVO.getCreateTime()[1])
                .orderByDesc(FieldDO::getId);
        
        // 2. 执行查询
        return fieldMapper.selectList(queryWrapper);
    }

    private void validateFieldExists(Long id) {
        if (fieldMapper.selectById(id) == null) {
            throw new IllegalArgumentException("字段不存在");
        }
    }

} 