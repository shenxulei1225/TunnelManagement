package com.cheers.arch.module.system.service.item;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.item.ItemCategoryRelDO;

/**
 * 数据项分类关系 Service 接口
 */
public interface ItemCategoryRelService {

    /**
     * 将数据项添加到分类
     *
     * @param itemId     数据项ID
     * @param itemType   数据项类型
     * @param categoryId 分类ID
     */
    void addItemToCategory(Long itemId, String itemType, Long categoryId);

    /**
     * 从分类中移除数据项
     *
     * @param itemId     数据项ID
     * @param categoryId 分类ID
     */
    void removeItemFromCategory(Long itemId, Long categoryId);

    /**
     * 移除数据项的所有分类关系
     *
     * @param itemId 数据项ID
     */
    void removeAllCategoriesFromItem(Long itemId);

    /**
     * 移除分类的所有数据项关系
     *
     * @param categoryId 分类ID
     */
    void removeAllItemsFromCategory(Long categoryId);

    /**
     * 根据数据项ID获取关系列表
     *
     * @param itemId 数据项ID
     * @return 关系列表
     */
    List<ItemCategoryRelDO> getRelationsByItemId(Long itemId);

    /**
     * 根据分类ID获取关系列表
     *
     * @param categoryId 分类ID
     * @return 关系列表
     */
    List<ItemCategoryRelDO> getRelationsByCategoryId(Long categoryId);

    /**
     * 根据数据项类型和分类ID获取关系列表
     *
     * @param itemType   数据项类型
     * @param categoryId 分类ID
     * @return 关系列表
     */
    List<ItemCategoryRelDO> getRelationsByItemTypeAndCategoryId(String itemType, Long categoryId);

    /**
     * 根据数据项ID获取分类ID列表
     *
     * @param itemId 数据项ID
     * @return 分类ID列表
     */
    List<Long> getCategoryIdsByItemId(Long itemId);

    /**
     * 根据分类ID获取数据项ID列表
     *
     * @param categoryId 分类ID
     * @return 数据项ID列表
     */
    List<Long> getItemIdsByCategoryId(Long categoryId);

    /**
     * 根据数据项类型和分类ID获取数据项ID列表
     *
     * @param itemType   数据项类型
     * @param categoryId 分类ID
     * @return 数据项ID列表
     */
    List<Long> getItemIdsByItemTypeAndCategoryId(String itemType, Long categoryId);

    /**
     * 判断数据项是否在分类中
     *
     * @param itemId     数据项ID
     * @param categoryId 分类ID
     * @return 是否在分类中
     */
    boolean isItemInCategory(Long itemId, Long categoryId);

    /**
     * 更新关系的排序
     *
     * @param itemId     数据项ID
     * @param categoryId 分类ID
     * @param sortOrder  排序
     */
    void updateRelationSortOrder(Long itemId, Long categoryId, Integer sortOrder);
} 