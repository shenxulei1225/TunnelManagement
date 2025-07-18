package com.cheers.arch.module.dynamic.dal.mysql.field;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态字段定义 Mapper
 */
@Mapper
public interface DynamicFieldDefinitionMapper extends BaseMapperX<DynamicFieldDefinitionDO> {
} 