package com.cheers.arch.module.system.convert.directory;

import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryRespVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 目录 Convert
 */
@Mapper
public interface DirectoryConvert {

    DirectoryConvert INSTANCE = Mappers.getMapper(DirectoryConvert.class);

    DirectoryDO convert(DirectoryCreateReqVO bean);

    DirectoryDO convert(DirectoryUpdateReqVO bean);

    DirectoryRespVO convert(DirectoryDO bean);

    List<DirectoryRespVO> convertList(List<DirectoryDO> list);
} 