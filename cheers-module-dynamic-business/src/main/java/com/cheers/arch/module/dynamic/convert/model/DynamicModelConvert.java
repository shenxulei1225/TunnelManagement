package com.cheers.arch.module.dynamic.convert.model;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.model.vo.DynamicModelCreateReqVO;
import com.cheers.arch.module.dynamic.controller.admin.model.vo.DynamicModelRespVO;
import com.cheers.arch.module.dynamic.controller.admin.model.vo.DynamicModelUpdateReqVO;
import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态业务模型 Convert
 */
@Mapper
public interface DynamicModelConvert {

    DynamicModelConvert INSTANCE = Mappers.getMapper(DynamicModelConvert.class);

    DynamicBusinessModelDO convert(DynamicModelCreateReqVO bean);

    DynamicBusinessModelDO convert(DynamicModelUpdateReqVO bean);

    DynamicModelRespVO convert(DynamicBusinessModelDO bean);

    List<DynamicModelRespVO> convertList(List<DynamicBusinessModelDO> list);

    PageResult<DynamicModelRespVO> convertPage(PageResult<DynamicBusinessModelDO> page);
} 