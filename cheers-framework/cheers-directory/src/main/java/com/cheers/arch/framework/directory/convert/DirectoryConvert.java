package com.cheers.arch.framework.directory.convert;

import com.cheers.arch.framework.directory.controller.admin.vo.DirectoryCreateReqVO;
import com.cheers.arch.framework.directory.controller.admin.vo.DirectoryRespVO;
import com.cheers.arch.framework.directory.controller.admin.vo.DirectoryUpdateReqVO;
import com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO;
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