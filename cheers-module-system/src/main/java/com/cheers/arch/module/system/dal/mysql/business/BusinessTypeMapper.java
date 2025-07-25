package com.cheers.arch.module.system.dal.mysql.business;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.business.BusinessTypeDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 业务类型定义 Mapper
 */
@Mapper
public interface BusinessTypeMapper extends BaseMapperX<BusinessTypeDO> {

} 