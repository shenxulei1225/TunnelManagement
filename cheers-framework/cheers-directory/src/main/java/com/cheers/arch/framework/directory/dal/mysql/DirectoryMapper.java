package com.cheers.arch.framework.directory.dal.mysql;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目录 Mapper
 */
@Mapper
public interface DirectoryMapper extends BaseMapperX<DirectoryDO> {
} 