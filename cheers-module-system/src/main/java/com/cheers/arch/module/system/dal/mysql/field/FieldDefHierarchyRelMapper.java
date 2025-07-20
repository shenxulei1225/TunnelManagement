package com.cheers.arch.module.system.dal.mysql.field;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefHierarchyRelDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字段定义-分级组关联 Mapper
 *
 * @author cheers
 */
@Mapper
public interface FieldDefHierarchyRelMapper extends BaseMapperX<FieldDefHierarchyRelDO> {

} 