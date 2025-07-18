package com.cheers.arch.module.dynamic.dal.mysql.template;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.template.DynamicSceneTemplateDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 动态场景模板 Mapper
 */
@Mapper
public interface DynamicSceneTemplateMapper extends BaseMapperX<DynamicSceneTemplateDO> {

    /**
     * 根据业务类型查询场景模板
     */
    default List<DynamicSceneTemplateDO> selectByBusinessType(String businessType) {
        return selectList(DynamicSceneTemplateDO::getBusinessType, businessType);
    }

    /**
     * 根据编码查询场景模板
     */
    default DynamicSceneTemplateDO selectByCode(String code) {
        return selectOne(DynamicSceneTemplateDO::getCode, code);
    }
} 