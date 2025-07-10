package com.cheers.system.dal.mysql.region;

import com.cheers.framework.mybatis.core.dataobject.BaseDO;
import com.cheers.framework.tree.mapper.TreeMapper;
import com.cheers.system.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RegionMapper extends TreeMapper<RegionDO, Long, BaseDO> {
} 