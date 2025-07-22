package com.cheers.arch.module.system.service.field;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldValueDO;

/**
 * 动态字段值 Service
 */
public interface FieldValueService {

    // ========== 基础操作 ==========

    /** 保存（新增或更新）一条业务记录的所有字段值 */
    Long saveFieldValues(FieldValueSaveReqVO reqVO);

    /** 更新单个字段值 */
    void updateFieldValue(Long id, String newValue);

    /** 删除单个字段值 */
    void deleteFieldValue(Long id);

    /** 删除业务记录的所有字段值 */
    void deleteFieldValuesByBizId(String bizType, Long bizId);

    // ========== 查询方法 ==========

    /** 分页查询业务数据 */
    IPage<FieldValueRespVO> getFieldValuePage(FieldValuePageReqVO pageReqVO);

    /** 根据字段ID获取所有字段值 */
    List<FieldValueDO> getFieldValuesByFieldId(Long fieldId);

    /** 根据业务ID获取所有字段值 */
    List<FieldValueDO> getFieldValuesByBizId(String bizType, Long bizId);

    /** 获取特定字段的值 */
    FieldValueDO getFieldValue(String bizType, Long bizId, Long fieldId);

    /** 根据字段ID列表批量获取字段值 */
    List<FieldValueDO> getFieldValuesByFieldIds(List<Long> fieldIds);

    // ========== 统计方法 ==========

    /** 统计字段的使用次数 */
    Long countFieldUsage(Long fieldId);

    /** 批量统计多个字段的使用次数 */
    java.util.Map<Long, Long> countFieldUsages(List<Long> fieldIds);
}
