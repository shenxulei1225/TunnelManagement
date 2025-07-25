package com.cheers.arch.module.system.service.business.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cheers.arch.module.system.dal.dataobject.business.BusinessTypeDO;
import com.cheers.arch.module.system.dal.mysql.business.BusinessTypeMapper;
import com.cheers.arch.module.system.service.business.BusinessTypeService;

import org.springframework.stereotype.Service;

/**
 * 业务类型定义 Service 实现类
 */
@Service
public class BusinessTypeServiceImpl implements BusinessTypeService {

    @Resource
    private BusinessTypeMapper businessTypeMapper;

    @Override
    public List<BusinessTypeDO> getEnabledBusinessTypes() {
        return businessTypeMapper.selectList(new LambdaQueryWrapper<BusinessTypeDO>()
                .eq(BusinessTypeDO::getStatus, 1)
                .eq(BusinessTypeDO::getDeleted, false)
                .orderByAsc(BusinessTypeDO::getSort));
    }

    @Override
    public BusinessTypeDO getByTypeCode(String typeCode) {
        return businessTypeMapper.selectOne(new LambdaQueryWrapper<BusinessTypeDO>()
                .eq(BusinessTypeDO::getTypeCode, typeCode)
                .eq(BusinessTypeDO::getDeleted, false));
    }

    @Override
    public boolean existsByTypeCode(String typeCode) {
        return businessTypeMapper.selectCount(new LambdaQueryWrapper<BusinessTypeDO>()
                .eq(BusinessTypeDO::getTypeCode, typeCode)
                .eq(BusinessTypeDO::getDeleted, false)) > 0;
    }
} 