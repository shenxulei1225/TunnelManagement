package cn.iocoder.yudao.module.system.service.region;

import cn.iocoder.yudao.module.system.dal.dataobject.region.FieldCategoryDO;

import java.util.List;

public interface FieldCategoryService {

    List<FieldCategoryDO> getCategoryTree();

    Long createCategory(FieldCategoryDO bean);

    Boolean updateCategory(FieldCategoryDO bean);

    Boolean deleteCategory(Long id);
}
