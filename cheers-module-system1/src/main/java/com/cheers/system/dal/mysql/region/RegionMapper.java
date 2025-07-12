package com.cheers.arch.module.system.dal.mysql.region;

import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;
import com.cheers.arch.framework.tree.mapper.TreeMapper;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMapper extends TreeMapper<RegionDO, Long, BaseDO> {
} 