package cn.iocoder.yudao.module.system.service.field;

import cn.iocoder.yudao.module.system.controller.admin.field.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefDO;

import java.util.List;

public interface FieldDefService {

    Long createFieldDef(FieldDefCreateReqVO reqVO);

    boolean updateFieldDef(FieldDefUpdateReqVO reqVO);

    boolean deleteFieldDef(Long id);

    FieldDefDO getFieldDef(Long id);

    List<FieldDefDO> getFieldDefListByCategory(Long categoryId);
}
