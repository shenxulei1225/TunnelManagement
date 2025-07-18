package com.cheers.arch.module.system.dal.mysql.region;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RegionMapper extends BaseMapperX<RegionDO> {
    
    /**
     * 根据父节点ID查询子节点
     */
    List<RegionDO> selectByParentId(@Param("parentId") Long parentId);
    
    /**
     * 根据分类ID查询区域列表
     */
    List<RegionDO> selectByCategoryId(@Param("categoryId") Long categoryId);
} 