package com.cheers.arch.module.system.dal.mysql.category;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 分类 Mapper - 支持TreeEntity框架
 */
@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

    /**
     * 根据父节点ID查询子节点
     *
     * @param parentId 父节点ID
     * @return 子节点列表
     */
    List<CategoryDO> selectByParentId(@Param("parentId") Long parentId);

    /**
     * 根据编码查询节点
     *
     * @param code 节点编码
     * @return 分类节点
     */
    CategoryDO selectByCode(@Param("code") String code);

    /**
     * 根据名称查询节点（在同一父节点下）
     *
     * @param parentId 父节点ID
     * @param name 节点名称
     * @return 分类节点
     */
    CategoryDO selectByParentIdAndName(@Param("parentId") Long parentId, @Param("name") String name);

    /**
     * 统计子节点数量
     *
     * @param parentId 父节点ID
     * @return 子节点数量
     */
    Long selectCountByParentId(@Param("parentId") Long parentId);
} 