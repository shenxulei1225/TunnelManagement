package com.cheers.arch.module.dynamic.service.field;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;

import java.util.List;

/**
 * 动态字段定义服务接口
 */
public interface DynamicFieldDefinitionService {

    /**
     * 创建动态字段定义
     *
     * @param field 动态字段定义信息
     * @return 动态字段定义ID
     */
    Long createField(DynamicFieldDefinitionDO field);

    /**
     * 更新动态字段定义
     *
     * @param field 动态字段定义信息
     */
    void updateField(DynamicFieldDefinitionDO field);

    /**
     * 删除动态字段定义
     *
     * @param id 动态字段定义ID
     */
    void deleteField(Long id);

    /**
     * 获取动态字段定义
     *
     * @param id 动态字段定义ID
     * @return 动态字段定义信息
     */
    DynamicFieldDefinitionDO getField(Long id);

    /**
     * 根据编码获取动态字段定义
     *
     * @param modelCode 模型编码
     * @param code 字段编码
     * @return 动态字段定义信息
     */
    DynamicFieldDefinitionDO getFieldByCode(String modelCode, String code);

    /**
     * 获取动态字段定义列表
     *
     * @param ids 动态字段定义ID列表
     * @return 动态字段定义列表
     */
    List<DynamicFieldDefinitionDO> getFieldList(List<Long> ids);

    /**
     * 获取模型的动态字段定义列表
     *
     * @param modelCode 模型编码
     * @return 动态字段定义列表
     */
    List<DynamicFieldDefinitionDO> getFieldListByModel(String modelCode);

    /**
     * 获取动态字段定义分页
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param name 字段名称
     * @param code 字段编码
     * @param type 字段类型
     * @param modelId 模型ID
     * @return 动态字段定义分页
     */
    PageResult<DynamicFieldDefinitionDO> getFieldPage(Integer pageNo, Integer pageSize, String name, String code, String type, Long modelId);

    /**
     * 校验动态字段定义编码是否唯一
     *
     * @param modelCode 模型编码
     * @param code 字段编码
     * @param excludeId 排除的动态字段定义ID
     * @return 是否唯一
     */
    boolean isCodeUnique(String modelCode, String code, Long excludeId);

    /**
     * 获取租户的所有动态字段定义列表
     *
     * @param tenantId 租户ID
     * @return 动态字段定义列表
     */
    List<DynamicFieldDefinitionDO> getFieldListByTenant(Long tenantId);

    /**
     * 校验动态字段定义是否可以删除
     *
     * @param id 动态字段定义ID
     * @return 是否可以删除
     */
    boolean validateFieldCanDelete(Long id);

    /**
     * 校验动态字段定义是否可以更新
     *
     * @param id 动态字段定义ID
     * @return 是否可以更新
     */
    boolean validateFieldCanUpdate(Long id);
} 