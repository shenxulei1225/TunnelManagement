package com.cheers.arch.module.system.dal.mysql.item;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.item.ItemDO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 数据项 Mapper
 */
@Mapper
public interface ItemMapper extends BaseMapperX<ItemDO> {

    /**
     * 根据业务类型查询数据项
     */
    default List<ItemDO> selectListByItemType(String itemType) {
        return selectList(new LambdaQueryWrapperX<ItemDO>()
                .eq(ItemDO::getItemType, itemType)
                .eq(ItemDO::getStatus, 1)
                .orderByAsc(ItemDO::getSortOrder, ItemDO::getId));
    }

    /**
     * 根据编码查询数据项
     */
    default ItemDO selectByCode(String code) {
        return selectOne(new LambdaQueryWrapperX<ItemDO>()
                .eq(ItemDO::getCode, code));
    }

    /**
     * 根据业务类型和编码查询数据项
     */
    default ItemDO selectByItemTypeAndCode(String itemType, String code) {
        return selectOne(new LambdaQueryWrapperX<ItemDO>()
                .eq(ItemDO::getItemType, itemType)
                .eq(ItemDO::getCode, code));
    }

    /**
     * 查询指定IDs的数据项
     */
    default List<ItemDO> selectListByIds(@Param("ids") List<Long> ids) {
        return selectBatchIds(ids);
    }
} 