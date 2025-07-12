package com.cheers.arch.module.system.service.field;

import com.cheers.arch.module.system.dal.dataobject.field.FieldDefCategoryRelDO;

import java.util.List;

public interface FieldCategoryFieldService {
    void save(Long categoryId, Long fieldId, boolean required, Integer sort);
    List<FieldDefCategoryRelDO> listByCategory(Long categoryId);
    void delete(Long categoryId, Long fieldId);
}
