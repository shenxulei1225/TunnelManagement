package com.cheers.arch.module.dynamic.convert.businessmodule;

import com.cheers.arch.framework.trees.utils.TreeUtils;
import com.cheers.arch.module.dynamic.controller.admin.businessmodule.vo.DynamicBusinessModuleTreeVO;
import com.cheers.arch.module.dynamic.dal.dataobject.businessmodule.DynamicBusinessModuleDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 动态业务分组 Convert
 */
@Mapper
public interface DynamicBusinessModuleConvert {

    DynamicBusinessModuleConvert INSTANCE = Mappers.getMapper(DynamicBusinessModuleConvert.class);

    DynamicBusinessModuleTreeVO convert(DynamicBusinessModuleDO bean);

    List<DynamicBusinessModuleTreeVO> convertList(List<DynamicBusinessModuleDO> list);

    /**
     * 构建树形结构
     */
    default List<DynamicBusinessModuleTreeVO> buildTree(List<DynamicBusinessModuleDO> list) {
        return TreeUtils.buildTree(list, 0L, this::convert);
    }
} 