package com.cheers.arch.module.dynamic.dal.mysql.permission;

import com.cheers.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态业务权限 Mapper
 */
@Mapper
public interface DynamicPermissionMapper extends BaseMapperX<DynamicPermissionDO> {
} 