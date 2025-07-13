package com.cheers.arch.module.dynamic.service.field;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;

import java.util.List;

/**
 * 字段定义服务接口
 */
public interface FieldDefinitionService {

    /**
     * 创建字段定义
     *
     * @param field 字段定义信息
     * @return 字段定义ID
     */
    Long createField(FieldDefinitionDO field);

    /**
     * 更新字段定义
     *
     * @param field 字段定义信息
     */
    void updateField(FieldDefinitionDO field);

    /**
     * 删除字段定义
     *
     * @param id 字段定义ID
     */
    void deleteField(Long id);

    /**
     * 获取字段定义
     *
     * @param id 字段定义ID
     * @return 字段定义信息
     */
    FieldDefinitionDO getField(Long id);

    /**
     * 获取字段定义
     *
     * @param modelCode 业务模型编码
     * @param code 字段编码
     * @return 字段定义信息
     */
    FieldDefinitionDO getFieldByCode(String modelCode, String code);

    /**
     * 获取字段定义列表
     *
     * @param ids 字段定义ID列表
     * @return 字段定义列表
     */
    List<FieldDefinitionDO> getFieldList(List<Long> ids);

    /**
     * 获取字段定义列表
     *
     * @param modelCode 业务模型编码
     * @return 字段定义列表
     */
    List<FieldDefinitionDO> getFieldListByModel(String modelCode);

    /**
     * 获取字段定义分页
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param name 字段名称
     * @param code 字段编码
     * @param type 字段类型
     * @param modelId 业务模型ID
     * @return 字段定义分页
     */
    PageResult<FieldDefinitionDO> getFieldPage(Integer pageNo, Integer pageSize, String name, String code, String type, Long modelId);

    /**
     * 校验字段编码是否唯一
     *
     * @param modelCode 业务模型编码
     * @param code 字段编码
     * @param excludeId 排除的字段定义ID
     * @return 是否唯一
     */
    boolean isCodeUnique(String modelCode, String code, Long excludeId);

    /**
     * 获取租户的所有字段定义列表
     *
     * @param tenantId 租户ID
     * @return 字段定义列表
     */
    List<FieldDefinitionDO> getFieldListByTenant(Long tenantId);

    /**
     * 校验字段定义是否可以删除
     *
     * @param id 字段定义ID
     * @return 是否可以删除
     */
    boolean validateFieldCanDelete(Long id);

    /**
     * 校验字段定义是否可以修改
     *
     * @param id 字段定义ID
     * @return 是否可以修改
     */
    boolean validateFieldCanUpdate(Long id);
} 