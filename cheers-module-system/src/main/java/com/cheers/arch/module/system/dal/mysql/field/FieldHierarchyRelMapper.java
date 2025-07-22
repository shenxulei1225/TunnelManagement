package com.cheers.arch.module.system.dal.mysql.field;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.field.FieldHierarchyRelDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字段-分级组关联 Mapper
 *
 * @author cheers
 */
@Mapper
public interface FieldHierarchyRelMapper extends BaseMapperX<FieldHierarchyRelDO> {

} 