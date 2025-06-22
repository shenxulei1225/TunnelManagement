package cn.iocoder.yudao.module.system.service.region;

import cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO;

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
