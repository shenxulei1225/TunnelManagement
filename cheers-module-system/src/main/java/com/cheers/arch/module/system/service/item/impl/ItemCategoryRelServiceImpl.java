package com.cheers.arch.module.system.service.item.impl;

import java.util.List;

import jakarta.annotation.Resource;

import com.cheers.arch.module.system.dal.dataobject.item.ItemCategoryRelDO;
import com.cheers.arch.module.system.dal.mysql.item.ItemCategoryRelMapper;
import com.cheers.arch.module.system.service.item.ItemCategoryRelService;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据项分类关系 Service 实现类
 */
@Service
@Validated
@Slf4j
public class ItemCategoryRelServiceImpl implements ItemCategoryRelService {

    @Resource
    private ItemCategoryRelMapper itemCategoryRelMapper;

    @Override
    public void addItemToCategory(Long itemId, String itemType, Long categoryId) {
        // 检查是否已存在关系
        ItemCategoryRelDO existing = itemCategoryRelMapper.selectByItemIdAndCategoryId(itemId, categoryId);
        if (existing != null) {
            return; // 已存在，不重复添加
        }

        ItemCategoryRelDO relDO = new ItemCategoryRelDO();
        relDO.setItemId(itemId);
        relDO.setItemType(itemType);
        relDO.setCategoryId(categoryId);
        relDO.setSortOrder(0);
        
        itemCategoryRelMapper.insert(relDO);
    }

    @Override
    public void removeItemFromCategory(Long itemId, Long categoryId) {
        itemCategoryRelMapper.deleteByItemIdAndCategoryId(itemId, categoryId);
    }

    @Override
    public void removeAllCategoriesFromItem(Long itemId) {
        itemCategoryRelMapper.deleteByItemId(itemId);
    }

    @Override
    public void removeAllItemsFromCategory(Long categoryId) {
        itemCategoryRelMapper.deleteByCategoryId(categoryId);
    }

    @Override
    public List<ItemCategoryRelDO> getRelationsByItemId(Long itemId) {
        return itemCategoryRelMapper.selectListByItemId(itemId);
    }

    @Override
    public List<ItemCategoryRelDO> getRelationsByCategoryId(Long categoryId) {
        return itemCategoryRelMapper.selectListByCategoryId(categoryId);
    }

    @Override
    public List<ItemCategoryRelDO> getRelationsByItemTypeAndCategoryId(String itemType, Long categoryId) {
        return itemCategoryRelMapper.selectListByItemTypeAndCategoryId(itemType, categoryId);
    }

    @Override
    public List<Long> getCategoryIdsByItemId(Long itemId) {
        List<ItemCategoryRelDO> relations = getRelationsByItemId(itemId);
        return relations.stream().map(ItemCategoryRelDO::getCategoryId).toList();
    }

    @Override
    public List<Long> getItemIdsByCategoryId(Long categoryId) {
        List<ItemCategoryRelDO> relations = getRelationsByCategoryId(categoryId);
        return relations.stream().map(ItemCategoryRelDO::getItemId).toList();
    }

    @Override
    public List<Long> getItemIdsByItemTypeAndCategoryId(String itemType, Long categoryId) {
        List<ItemCategoryRelDO> relations = getRelationsByItemTypeAndCategoryId(itemType, categoryId);
        return relations.stream().map(ItemCategoryRelDO::getItemId).toList();
    }

    @Override
    public boolean isItemInCategory(Long itemId, Long categoryId) {
        ItemCategoryRelDO relation = itemCategoryRelMapper.selectByItemIdAndCategoryId(itemId, categoryId);
        return relation != null;
    }

    @Override
    public void updateRelationSortOrder(Long itemId, Long categoryId, Integer sortOrder) {
        ItemCategoryRelDO relation = itemCategoryRelMapper.selectByItemIdAndCategoryId(itemId, categoryId);
        if (relation != null) {
            relation.setSortOrder(sortOrder);
            itemCategoryRelMapper.updateById(relation);
        }
    }
} 