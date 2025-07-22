package com.cheers.arch.module.system.dal.mysql.tree;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.dal.dataobject.tree.DataTypeMetaDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 数据类型元数据 Mapper
 *
 * @author cheers
 */
@Mapper
public interface DataTypeMetaMapper extends BaseMapperX<DataTypeMetaDO> {

    default DataTypeMetaDO selectByDataType(String dataType) {
        return selectOne(new LambdaQueryWrapperX<DataTypeMetaDO>()
                .eq(DataTypeMetaDO::getDataType, dataType)
                .eq(DataTypeMetaDO::getStatus, 1));
    }

    default List<DataTypeMetaDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<DataTypeMetaDO>()
                .eq(DataTypeMetaDO::getStatus, status)
                .orderByAsc(DataTypeMetaDO::getDataType));
    }

    default List<DataTypeMetaDO> selectList() {
        return selectList(new LambdaQueryWrapperX<DataTypeMetaDO>()
                .eq(DataTypeMetaDO::getStatus, 1)
                .orderByAsc(DataTypeMetaDO::getDataType));
    }
} 