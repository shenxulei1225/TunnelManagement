package com.cheers.arch.framework.category.dal.mysql;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.category.dal.dataobject.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 分类 Mapper
 */
@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

} 