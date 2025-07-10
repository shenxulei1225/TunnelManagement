package com.cheers.module.dynamic.dal.mysql.model;

import com.cheers.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.module.dynamic.dal.dataobject.model.BusinessModelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务模型 Mapper
 */
@Mapper
public interface BusinessModelMapper extends BaseMapperX<BusinessModelDO> {
} 