package com.cheers.arch.module.system.service.item;

import com.cheers.arch.module.system.dal.dataobject.item.ItemDO;
import com.cheers.arch.module.system.dal.mysql.item.SystemItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cheers.arch.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.SYSTEM_ITEM_NOT_EXISTS;
import static com.cheers.arch.module.system.enums.ErrorCodeConstants.SYSTEM_ITEM_CODE_EXISTS;

/**
 * 系统数据项 Service 实现类
 */
@Service
@Validated
@Slf4j
public class SystemItemServiceImpl implements SystemItemService {

    @Resource
    private SystemItemMapper systemItemMapper;

    @Override
    public Long createItem(ItemDO itemDO) {
        // 校验编码唯一性
        validateCodeUnique(null, itemDO.getCode());
        
        // 插入数据
        systemItemMapper.insert(itemDO);
        return itemDO.getId();
    }

    @Override
    public void updateItem(ItemDO itemDO) {
        // 校验存在
        validateItemExists(itemDO.getId());
        // 校验编码唯一性
        validateCodeUnique(itemDO.getId(), itemDO.getCode());
        
        // 更新数据
        systemItemMapper.updateById(itemDO);
    }

    @Override
    public void deleteItem(Long id) {
        // 校验存在
        validateItemExists(id);
        
        // 删除数据
        systemItemMapper.deleteById(id);
    }

    @Override
    public ItemDO getItem(Long id) {
        return systemItemMapper.selectById(id);
    }

    @Override
    public ItemDO getItemByCode(String code) {
        return systemItemMapper.selectByCode(code);
    }

    @Override
    public ItemDO getItemByItemTypeAndCode(String itemType, String code) {
        return systemItemMapper.selectByItemTypeAndCode(itemType, code);
    }

    @Override
    public List<ItemDO> getItemListByItemType(String itemType) {
        return systemItemMapper.selectListByItemType(itemType);
    }

    @Override
    public List<ItemDO> getItemListByIds(List<Long> ids) {
        return systemItemMapper.selectListByIds(ids);
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
        
        systemItemMapper.updateById(item);
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
        systemItemMapper.updateById(item);
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