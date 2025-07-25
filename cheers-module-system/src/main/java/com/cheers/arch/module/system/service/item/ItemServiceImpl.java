package com.cheers.arch.module.system.service.item;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.SYSTEM_ITEM_CODE_EXISTS;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.SYSTEM_ITEM_NOT_EXISTS;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.annotation.Resource;

import com.cheers.arch.module.system.dal.dataobject.item.ItemDO;
import com.cheers.arch.module.system.dal.mysql.item.ItemMapper;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据项 Service 实现类
 */
@Service
@Validated
@Slf4j
public class ItemServiceImpl implements ItemService {

    @Resource
    private ItemMapper itemMapper;

    @Override
    public Long createItem(ItemDO itemDO) {
        // 校验编码唯一性
        validateCodeUnique(null, itemDO.getCode());
        
        // 插入数据
        itemMapper.insert(itemDO);
        return itemDO.getId();
    }

    @Override
    public void updateItem(ItemDO itemDO) {
        // 校验存在
        validateItemExists(itemDO.getId());
        // 校验编码唯一性
        validateCodeUnique(itemDO.getId(), itemDO.getCode());
        
        // 更新数据
        itemMapper.updateById(itemDO);
    }

    @Override
    public void deleteItem(Long id) {
        // 校验存在
        validateItemExists(id);
        
        // 删除数据
        itemMapper.deleteById(id);
    }

    @Override
    public ItemDO getItem(Long id) {
        return itemMapper.selectById(id);
    }

    @Override
    public ItemDO getItemByCode(String code) {
        return itemMapper.selectByCode(code);
    }

    @Override
    public ItemDO getItemByItemTypeAndCode(String itemType, String code) {
        return itemMapper.selectByItemTypeAndCode(itemType, code);
    }

    @Override
    public List<ItemDO> getItemListByItemType(String itemType) {
        return itemMapper.selectListByItemType(itemType);
    }

    @Override
    public List<ItemDO> getItemListByIds(List<Long> ids) {
        return itemMapper.selectListByIds(ids);
    }

    @Override
    public void setCustomFieldValue(Long itemId, String fieldCode, Object value) {
        ItemDO item = getItem(itemId);
        if (item == null) {
            throw exception(SYSTEM_ITEM_NOT_EXISTS);
        }
        
        Map<String, Object> businessData = item.getBusinessData();
        if (businessData == null) {
            businessData = new HashMap<>();
        }
        businessData.put(fieldCode, value);
        item.setBusinessData(businessData);
        
        itemMapper.updateById(item);
    }

    @Override
    public Object getCustomFieldValue(Long itemId, String fieldCode) {
        ItemDO item = getItem(itemId);
        if (item == null) {
            return null;
        }
        
        Map<String, Object> businessData = item.getBusinessData();
        if (businessData == null) {
            return null;
        }
        
        return businessData.get(fieldCode);
    }

    @Override
    public Map<String, Object> getAllCustomFieldValues(Long itemId) {
        ItemDO item = getItem(itemId);
        if (item == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> businessData = item.getBusinessData();
        return businessData != null ? businessData : new HashMap<>();
    }

    @Override
    public void setAllCustomFieldValues(Long itemId, Map<String, Object> fieldValues) {
        ItemDO item = getItem(itemId);
        if (item == null) {
            throw exception(SYSTEM_ITEM_NOT_EXISTS);
        }
        
        item.setBusinessData(fieldValues);
        itemMapper.updateById(item);
    }

    @Override
    public List<ItemDO> getItemListByCustomField(String itemType, String fieldCode, Object value) {
        // 注意：这里需要使用 JSON 查询，实际实现可能需要在 Mapper 中添加专门的方法
        // 这里暂时返回按类型查询的结果，实际项目中需要完善 JSON 查询逻辑
        return getItemListByItemType(itemType);
    }

    @Override
    public void validateItemExists(Long id) {
        if (getItem(id) == null) {
            throw exception(SYSTEM_ITEM_NOT_EXISTS);
        }
    }

    @Override
    public void validateCodeUnique(Long id, String code) {
        ItemDO item = getItemByCode(code);
        if (item == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的数据项
        if (id == null) {
            throw exception(SYSTEM_ITEM_CODE_EXISTS);
        }
        if (!item.getId().equals(id)) {
            throw exception(SYSTEM_ITEM_CODE_EXISTS);
        }
    }
} 