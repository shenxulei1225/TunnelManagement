package com.cheers.arch.module.dynamic.dal.mysql.model;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务模型 Mapper
 */
@Mapper
public interface DynamicBusinessModelMapper extends BaseMapperX<DynamicBusinessModelDO> {
} 