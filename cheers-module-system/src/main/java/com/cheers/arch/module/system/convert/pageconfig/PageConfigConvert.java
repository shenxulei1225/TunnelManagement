package com.cheers.arch.module.system.convert.pageconfig;

import java.util.List;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigCreateReqVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigRespVO;
import com.cheers.arch.module.system.controller.admin.pageconfig.vo.PageConfigUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.pageconfig.PageConfigDO;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 页面配置 Convert
 *
 * @author cheers
 */
@Mapper
public interface PageConfigConvert {

    PageConfigConvert INSTANCE = Mappers.getMapper(PageConfigConvert.class);

    PageConfigDO convert(PageConfigCreateReqVO bean);

    PageConfigDO convert(PageConfigUpdateReqVO bean);

    PageConfigRespVO convert(PageConfigDO bean);

    List<PageConfigRespVO> convertList(List<PageConfigDO> list);

    PageResult<PageConfigRespVO> convertPage(PageResult<PageConfigDO> page);

} 