package com.cheers.arch.module.system.service.region;

import com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO;

import java.util.List;

public interface FieldCategoryService {

    /**
     * 获取分类树；若 bizType 为空返回全部
     */
    List<FieldCategoryDO> getCategoryTree(String bizType);

    Long createCategory(FieldCategoryDO bean);

    Boolean updateCategory(FieldCategoryDO bean);

    Boolean deleteCategory(Long id);
}
