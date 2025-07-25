package com.cheers.arch.module.system.dal.mysql.item;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.item.ItemCategoryRelDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据项分类关系 Mapper
 */
@Mapper
public interface ItemCategoryRelMapper extends BaseMapperX<ItemCategoryRelDO> {

    /**
     * 根据数据项ID查询关系列表
     */
    default List<ItemCategoryRelDO> selectListByItemId(Long itemId) {
        return selectList(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getItemId, itemId)
                .orderByAsc(ItemCategoryRelDO::getSortOrder));
    }

    /**
     * 根据分类ID查询关系列表
     */
    default List<ItemCategoryRelDO> selectListByCategoryId(Long categoryId) {
        return selectList(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getCategoryId, categoryId)
                .orderByAsc(ItemCategoryRelDO::getSortOrder));
    }

    /**
     * 根据数据项类型和分类ID查询关系列表
     */
    default List<ItemCategoryRelDO> selectListByItemTypeAndCategoryId(String itemType, Long categoryId) {
        return selectList(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getItemType, itemType)
                .eq(ItemCategoryRelDO::getCategoryId, categoryId)
                .orderByAsc(ItemCategoryRelDO::getSortOrder));
    }

    /**
     * 根据数据项ID和分类ID查询关系
     */
    default ItemCategoryRelDO selectByItemIdAndCategoryId(Long itemId, Long categoryId) {
        return selectOne(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getItemId, itemId)
                .eq(ItemCategoryRelDO::getCategoryId, categoryId));
    }

    /**
     * 删除数据项ID和分类ID的关系
     */
    default void deleteByItemIdAndCategoryId(Long itemId, Long categoryId) {
        delete(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getItemId, itemId)
                .eq(ItemCategoryRelDO::getCategoryId, categoryId));
    }

    /**
     * 删除数据项的所有分类关系
     */
    default void deleteByItemId(Long itemId) {
        delete(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getItemId, itemId));
    }

    /**
     * 删除分类的所有数据项关系
     */
    default void deleteByCategoryId(Long categoryId) {
        delete(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .eq(ItemCategoryRelDO::getCategoryId, categoryId));
    }

    /**
     * 批量查询数据项IDs
     */
    default List<Long> selectItemIdsByCategoryId(@Param("categoryId") Long categoryId) {
        return selectObjs(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .select(ItemCategoryRelDO::getItemId)
                .eq(ItemCategoryRelDO::getCategoryId, categoryId));
    }

    /**
     * 批量查询分类IDs
     */
    default List<Long> selectCategoryIdsByItemId(@Param("itemId") Long itemId) {
        return selectObjs(new LambdaQueryWrapperX<ItemCategoryRelDO>()
                .select(ItemCategoryRelDO::getCategoryId)
                .eq(ItemCategoryRelDO::getItemId, itemId));
    }
} 