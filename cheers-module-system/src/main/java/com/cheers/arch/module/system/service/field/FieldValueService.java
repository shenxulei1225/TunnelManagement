package com.cheers.arch.module.system.service.field;

import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 动态字段值 Service
 */
public interface FieldValueService {

    /** 保存（新增或更新）一条业务记录的所有字段值 */
    Long saveFieldValues(FieldValueSaveReqVO reqVO);

    /** 分页查询业务数据 */
    IPage<FieldValueRespVO> getFieldValuePage(FieldValuePageReqVO pageReqVO);
}
