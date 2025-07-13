package com.cheers.arch.module.dynamic.dal.mysql.model;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.model.BusinessModelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务模型 Mapper
 */
@Mapper
public interface BusinessModelMapper extends BaseMapperX<BusinessModelDO> {
} 