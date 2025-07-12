package com.cheers.arch.module.system.service.field;

import com.cheers.arch.module.system.controller.admin.field.vo.*;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefDO;

import java.util.List;

public interface FieldDefService {

    Long createFieldDef(FieldDefCreateReqVO reqVO);

    boolean updateFieldDef(FieldDefUpdateReqVO reqVO);

    boolean deleteFieldDef(Long id);

    FieldDefDO getFieldDef(Long id);

    List<FieldDefDO> getFieldDefListByCategory(Long categoryId);

    /**
     * 按业务类型获取字段列表（包含共享字段）
     */
    List<FieldDefDO> getFieldDefListByBizType(String bizType);
}
