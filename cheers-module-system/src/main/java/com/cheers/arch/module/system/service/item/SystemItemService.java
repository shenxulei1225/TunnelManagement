package com.cheers.arch.module.system.service.item;

import java.util.List;
import java.util.Map;

import com.cheers.arch.module.system.dal.dataobject.item.ItemDO;

/**
 * 系统数据项 Service 接口
 */
public interface SystemItemService {

    /**
     * 创建系统数据项
     *
     * @param itemDO 创建信息
     * @return 编号
     */
    Long createItem(ItemDO itemDO);

    /**
     * 更新系统数据项
     *
     * @param itemDO 更新信息
     */
    void updateItem(ItemDO itemDO);

    /**
     * 删除系统数据项
     *
     * @param id 编号
     */
    void deleteItem(Long id);

    /**
     * 获得系统数据项
     *
     * @param id 编号
     * @return 系统数据项
     */
    ItemDO getItem(Long id);

    /**
     * 根据编码获得系统数据项
     *
     * @param code 编码
     * @return 系统数据项
     */
    ItemDO getItemByCode(String code);

    /**
     * 根据数据项类型和编码获得系统数据项
     *
     * @param itemType 数据项类型
     * @param code     编码
     * @return 系统数据项
     */
    ItemDO getItemByItemTypeAndCode(String itemType, String code);

    /**
     * 根据数据项类型获得系统数据项列表
     *
     * @param itemType 数据项类型
     * @return 系统数据项列表
     */
    List<ItemDO> getItemListByItemType(String itemType);

    /**
     * 根据ID列表获得系统数据项列表
     *
     * @param ids ID列表
     * @return 系统数据项列表
     */
    List<ItemDO> getItemListByIds(List<Long> ids);

    // ========== 自定义字段相关方法 ==========

    /**
     * 设置自定义字段值
     *
     * @param itemId    数据项ID
     * @param fieldCode 字段编码
     * @param value     字段值
     */
    void setCustomFieldValue(Long itemId, String fieldCode, Object value);

    /**
     * 获取自定义字段值
     *
     * @param itemId    数据项ID
     * @param fieldCode 字段编码
     * @return 字段值
     */
    Object getCustomFieldValue(Long itemId, String fieldCode);

    /**
     * 获取所有自定义字段值
     *
     * @param itemId 数据项ID
     * @return 字段值Map
     */
    Map<String, Object> getAllCustomFieldValues(Long itemId);

    /**
     * 设置所有自定义字段值
     *
     * @param itemId      数据项ID
     * @param fieldValues 字段值Map
     */
    void setAllCustomFieldValues(Long itemId, Map<String, Object> fieldValues);

    /**
     * 根据自定义字段查询数据项
     *
     * @param itemType  数据项类型
     * @param fieldCode 字段编码
     * @param value     字段值
     * @return 数据项列表
     */
    List<ItemDO> getItemListByCustomField(String itemType, String fieldCode, Object value);

    // ========== 校验相关方法 ==========

    /**
     * 校验系统数据项是否存在
     *
     * @param id 编号
     */
    void validateItemExists(Long id);

    /**
     * 校验编码唯一性
     *
     * @param id   编号
     * @param code 编码
     */
    void validateCodeUnique(Long id, String code);
} 