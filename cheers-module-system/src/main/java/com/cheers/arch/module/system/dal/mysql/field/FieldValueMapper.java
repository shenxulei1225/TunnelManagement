package com.cheers.arch.module.system.dal.mysql.field;

import com.cheers.arch.module.system.dal.dataobject.field.FieldValueDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 动态字段值 Mapper
 */
@Mapper
public interface FieldValueMapper extends BaseMapper<FieldValueDO> {
}
