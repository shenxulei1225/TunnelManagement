package com.cheers.arch.module.system.service.region.impl;

import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;
import com.cheers.arch.framework.tree.service.AbstractTreeService;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;
import com.cheers.arch.module.system.dal.mysql.region.RegionMapper;
import com.cheers.arch.module.system.service.region.RegionService;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 区域 Service 实现类
 */
@Service
public class RegionServiceImpl extends AbstractTreeService<RegionMapper, RegionDO, Long, BaseDO>
        implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Override
    protected RegionMapper getMapper() {
        return regionMapper;
    }

    @Override
    protected String getTableName() {
        return "system_region";
    }
} 