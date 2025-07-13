package com.cheers.arch.module.dynamic.convert.field;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldCreateReqVO;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldRespVO;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldUpdateReqVO;
import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态业务字段 Convert
 */
@Mapper
public interface DynamicFieldConvert {

    DynamicFieldConvert INSTANCE = Mappers.getMapper(DynamicFieldConvert.class);

    @Mapping(target = "required", source = "required", qualifiedByName = "integerToBoolean")
    @Mapping(target = "modelCode", source = "modelId", qualifiedByName = "modelIdToModelCode")
    FieldDefinitionDO convert(DynamicFieldCreateReqVO bean);

    @Mapping(target = "required", source = "required", qualifiedByName = "integerToBoolean")
    @Mapping(target = "modelCode", source = "modelId", qualifiedByName = "modelIdToModelCode")
    FieldDefinitionDO convert(DynamicFieldUpdateReqVO bean);

    @Mapping(target = "required", source = "required", qualifiedByName = "booleanToInteger")
    @Mapping(target = "modelId", source = "modelCode", qualifiedByName = "modelCodeToModelId")
    DynamicFieldRespVO convert(FieldDefinitionDO bean);

    List<DynamicFieldRespVO> convertList(List<FieldDefinitionDO> list);

    PageResult<DynamicFieldRespVO> convertPage(PageResult<FieldDefinitionDO> page);

    @Named("integerToBoolean")
    default Boolean integerToBoolean(Integer value) {
        return value != null && value == 1;
    }

    @Named("booleanToInteger")
    default Integer booleanToInteger(Boolean value) {
        return Boolean.TRUE.equals(value) ? 1 : 0;
    }

    @Named("modelIdToModelCode")
    default String modelIdToModelCode(Long modelId) {
        return modelId != null ? modelId.toString() : null;
    }

    @Named("modelCodeToModelId")
    default Long modelCodeToModelId(String modelCode) {
        try {
            return modelCode != null ? Long.parseLong(modelCode) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
} 