package com.cheers.arch.module.dynamic.dal.mysql.view;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.view.DynamicViewConfigDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 动态视图配置 Mapper
 */
@Mapper
public interface DynamicViewConfigMapper extends BaseMapperX<DynamicViewConfigDO> {

    /**
     * 根据模型编码查询视图配置
     */
    default List<DynamicViewConfigDO> selectByModelCode(String modelCode) {
        return selectList(DynamicViewConfigDO::getModelCode, modelCode);
    }

    /**
     * 根据模型编码和视图类型查询视图配置
     */
    default List<DynamicViewConfigDO> selectByModelCodeAndViewType(String modelCode, String viewType) {
        return selectList(new LambdaQueryWrapperX<DynamicViewConfigDO>()
                .eq(DynamicViewConfigDO::getModelCode, modelCode)
                .eq(DynamicViewConfigDO::getViewType, viewType));
    }
} 