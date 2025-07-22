package com.cheers.arch.module.system.service.field.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldValueDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldValueMapper;
import com.cheers.arch.module.system.service.field.FieldValueService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FieldValueServiceImpl implements FieldValueService {

    private final FieldValueMapper fieldValueMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveFieldValues(FieldValueSaveReqVO reqVO) {
        // 简化实现：循环保存
        reqVO.getValues().forEach((fieldKey, val) -> {
            FieldValueDO doObj = new FieldValueDO();
            doObj.setBizType(reqVO.getBizType());
            doObj.setBizId(reqVO.getBizId());
            doObj.setFieldId(Long.parseLong(fieldKey)); // 前端需传 fieldId 作为 key
            doObj.setValueJson(val);
            fieldValueMapper.insert(doObj);
        });
        return reqVO.getBizId();
    }

    @Override
    public IPage<FieldValueRespVO> getFieldValuePage(FieldValuePageReqVO pageReqVO) {
        // 1. 查询所有满足 bizType 的记录
        List<FieldValueDO> list = fieldValueMapper.selectList(new LambdaQueryWrapperX<FieldValueDO>()
                .eq(FieldValueDO::getBizType, pageReqVO.getBizType()));

        // 2. 聚合到 bizId 维度
        Map<Long, FieldValueRespVO> grouped = new LinkedHashMap<>();
        list.forEach(item -> {
            if(item.getBizId()==null){
                return; // skip invalid record
            }
            FieldValueRespVO vo = grouped.computeIfAbsent(item.getBizId(), id -> {
                FieldValueRespVO r = new FieldValueRespVO();
                r.setBizId(id);
                r.setValues(new HashMap<>());
                return r;
            });
            vo.getValues().put(item.getFieldId().toString(), item.getValueJson());
        });

        List<FieldValueRespVO> all = new ArrayList<>(grouped.values());
        long total = all.size();
        // 3. 手动分页
        long pageNo = pageReqVO.getPageNo();
        long pageSize = pageReqVO.getPageSize();
        int fromIdx = (int) ((pageNo - 1) * pageSize);
        int toIdx = Math.min(fromIdx + (int) pageSize, (int) total);
        List<FieldValueRespVO> records = fromIdx >= total ? Collections.emptyList() : all.subList(fromIdx, toIdx);

        Page<FieldValueRespVO> page = new Page<>(pageNo, pageSize, total);
        page.setRecords(records);
        return page;
    }

    // ========== 新增的方法实现 ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFieldValue(Long id, String newValue) {
        FieldValueDO existing = fieldValueMapper.selectById(id);
        if (existing == null) {
            throw new IllegalArgumentException("字段值不存在");
        }
        
        existing.setValueJson(newValue);
        fieldValueMapper.updateById(existing);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldValue(Long id) {
        fieldValueMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFieldValuesByBizId(String bizType, Long bizId) {
        fieldValueMapper.delete(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .eq(FieldValueDO::getBizType, bizType)
                        .eq(FieldValueDO::getBizId, bizId)
        );
    }

    @Override
    public List<FieldValueDO> getFieldValuesByFieldId(Long fieldId) {
        return fieldValueMapper.selectList(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .eq(FieldValueDO::getFieldId, fieldId)
                        .orderByDesc(FieldValueDO::getCreateTime)
        );
    }

    @Override
    public List<FieldValueDO> getFieldValuesByBizId(String bizType, Long bizId) {
        return fieldValueMapper.selectList(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .eq(FieldValueDO::getBizType, bizType)
                        .eq(FieldValueDO::getBizId, bizId)
                        .orderByDesc(FieldValueDO::getCreateTime)
        );
    }

    @Override
    public FieldValueDO getFieldValue(String bizType, Long bizId, Long fieldId) {
        return fieldValueMapper.selectOne(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .eq(FieldValueDO::getBizType, bizType)
                        .eq(FieldValueDO::getBizId, bizId)
                        .eq(FieldValueDO::getFieldId, fieldId)
        );
    }

    @Override
    public List<FieldValueDO> getFieldValuesByFieldIds(List<Long> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return List.of();
        }
        
        return fieldValueMapper.selectList(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .in(FieldValueDO::getFieldId, fieldIds)
                        .orderByDesc(FieldValueDO::getCreateTime)
        );
    }

    @Override
    public Long countFieldUsage(Long fieldId) {
        return fieldValueMapper.selectCount(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .eq(FieldValueDO::getFieldId, fieldId)
        );
    }

    @Override
    public java.util.Map<Long, Long> countFieldUsages(List<Long> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return java.util.Map.of();
        }
        
        List<FieldValueDO> values = fieldValueMapper.selectList(
                new LambdaQueryWrapperX<FieldValueDO>()
                        .in(FieldValueDO::getFieldId, fieldIds)
        );
        
        return values.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        FieldValueDO::getFieldId,
                        java.util.stream.Collectors.counting()
                ));
    }
}
