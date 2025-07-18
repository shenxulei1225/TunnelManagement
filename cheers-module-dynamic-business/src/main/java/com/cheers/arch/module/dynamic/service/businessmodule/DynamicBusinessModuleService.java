package com.cheers.arch.module.dynamic.service.businessmodule;

import com.cheers.arch.module.dynamic.dal.dataobject.businessmodule.DynamicBusinessModuleDO;

import java.util.List;

public interface DynamicBusinessModuleService {

    /**
     * 获取业务分组树；若 bizType 为空返回全部
     */
    List<DynamicBusinessModuleDO> getBusinessModuleTree(String bizType);

    /**
     * 获取指定节点下的子树
     */
    List<DynamicBusinessModuleDO> getBusinessModuleSubTree(Long parentId);

    /**
     * 拖拽调整树结构
     */
    Boolean dragBusinessModule(Long dragId, Long targetParentId, String position, Long targetId);

    Long createBusinessModule(DynamicBusinessModuleDO bean);

    Boolean updateBusinessModule(DynamicBusinessModuleDO bean);

    Boolean deleteBusinessModule(Long id);
}
