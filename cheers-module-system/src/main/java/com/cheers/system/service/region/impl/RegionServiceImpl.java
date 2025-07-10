package com.cheers.system.service.region.impl;

import com.cheers.framework.mybatis.core.dataobject.BaseDO;
import com.cheers.framework.tree.service.AbstractTreeService;
import com.cheers.system.dal.dataobject.region.RegionDO;
import com.cheers.system.dal.mysql.region.RegionMapper;
import com.cheers.system.service.region.RegionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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