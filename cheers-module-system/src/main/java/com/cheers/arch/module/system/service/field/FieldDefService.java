package com.cheers.arch.module.system.service.field;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefDO;

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

    /**
     * 根据语义目录获取字段列表
     * @param semanticDirectoryId 语义目录ID，如果为null则获取所有字段
     * @return 字段列表
     */
    List<FieldDefDO> getFieldDefListBySemanticDirectory(Long semanticDirectoryId);

    /**
     * 更新字段的语义目录
     * @param fieldId 字段ID
     * @param semanticDirectoryId 语义目录ID
     * @return 是否成功
     */
    boolean updateFieldSemanticDirectory(Long fieldId, Long semanticDirectoryId);

    /**
     * 批量更新字段的语义目录
     * @param fieldIds 字段ID列表
     * @param semanticDirectoryId 语义目录ID
     * @return 成功更新的数量
     */
    int batchUpdateFieldSemanticDirectory(List<Long> fieldIds, Long semanticDirectoryId);

    /**
     * 获取语义目录树结构
     * @return 语义目录列表
     */
    List<Object> getSemanticDirectories();
}
