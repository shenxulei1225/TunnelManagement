package com.cheers.arch.module.system.service.field.impl;

import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldValueDO;
import com.cheers.arch.module.system.dal.mysql.field.FieldValueMapper;
import com.cheers.arch.module.system.service.field.FieldValueService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<FieldValueDO> list = fieldValueMapper.selectList(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<FieldValueDO>()
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
}
