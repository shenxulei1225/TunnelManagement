package com.cheers.arch.module.dynamic.convert.permission;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.permission.vo.DynamicPermissionCreateReqVO;
import com.cheers.arch.module.dynamic.controller.admin.permission.vo.DynamicPermissionRespVO;
import com.cheers.arch.module.dynamic.controller.admin.permission.vo.DynamicPermissionUpdateReqVO;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态业务权限 Convert
 */
@Mapper
public interface DynamicPermissionConvert {

    DynamicPermissionConvert INSTANCE = Mappers.getMapper(DynamicPermissionConvert.class);

    DynamicPermissionDO convert(DynamicPermissionCreateReqVO bean);

    DynamicPermissionDO convert(DynamicPermissionUpdateReqVO bean);

    DynamicPermissionRespVO convert(DynamicPermissionDO bean);

    List<DynamicPermissionRespVO> convertList(List<DynamicPermissionDO> list);

    PageResult<DynamicPermissionRespVO> convertPage(PageResult<DynamicPermissionDO> page);
} 