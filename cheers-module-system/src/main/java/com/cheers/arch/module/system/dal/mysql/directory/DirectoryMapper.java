package com.cheers.arch.module.system.dal.mysql.directory;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 目录 Mapper
 */
@Mapper
public interface DirectoryMapper extends BaseMapperX<DirectoryDO> {
} 