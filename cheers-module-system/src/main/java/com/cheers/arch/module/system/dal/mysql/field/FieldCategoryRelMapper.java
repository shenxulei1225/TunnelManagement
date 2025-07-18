package com.cheers.arch.module.system.dal.mysql.field;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.field.FieldCategoryRelDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字段与分类关联 Mapper
 */
@Mapper
public interface FieldCategoryRelMapper extends BaseMapperX<FieldCategoryRelDO> {

} 