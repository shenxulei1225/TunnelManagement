package com.cheers.arch.module.dynamic.service.model;

import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;
import com.cheers.arch.framework.common.pojo.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 动态业务模型服务接口
 */
public interface DynamicBusinessModelService {

    /**
     * 创建动态业务模型
     *
     * @param model 动态业务模型信息
     * @return 动态业务模型ID
     */
    Long createModel(DynamicBusinessModelDO model);

    /**
     * 更新动态业务模型
     *
     * @param model 动态业务模型信息
     */
    void updateModel(DynamicBusinessModelDO model);

    /**
     * 删除动态业务模型
     *
     * @param id 动态业务模型ID
     */
    void deleteModel(Long id);

    /**
     * 获取动态业务模型
     *
     * @param id 动态业务模型ID
     * @return 动态业务模型信息
     */
    DynamicBusinessModelDO getModel(Long id);

    /**
     * 获取动态业务模型
     *
     * @param code 动态业务模型编码
     * @return 动态业务模型信息
     */
    DynamicBusinessModelDO getModelByCode(String code);

    /**
     * 获取动态业务模型列表
     *
     * @param ids 动态业务模型ID列表
     * @return 动态业务模型列表
     */
    List<DynamicBusinessModelDO> getModelList(List<Long> ids);

    /**
     * 获取动态业务模型分页
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param name 模型名称
     * @param status 状态
     * @return 动态业务模型分页
     */
    PageResult<DynamicBusinessModelDO> getModelPage(Integer pageNo, Integer pageSize, String name, Integer status);

    /**
     * 获取动态业务模型分页（按目录）
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param name 模型名称
     * @param status 状态
     * @param directoryId 目录ID
     * @return 动态业务模型分页
     */
    PageResult<DynamicBusinessModelDO> getModelPageByDirectory(Integer pageNo, Integer pageSize, String name, Integer status, Long directoryId);

    /**
     * 校验动态业务模型编码是否唯一
     *
     * @param code 动态业务模型编码
     * @param excludeId 排除的动态业务模型ID
     * @return 是否唯一
     */
    boolean isCodeUnique(String code, Long excludeId);

    /**
     * 获取租户的所有动态业务模型列表
     *
     * @param tenantId 租户ID
     * @return 动态业务模型列表
     */
    List<DynamicBusinessModelDO> getModelListByTenant(Long tenantId);

    /**
     * 初始化动态业务模型的数据表
     *
     * @param model 动态业务模型信息
     */
    void initModelTable(DynamicBusinessModelDO model);

    /**
     * 更新动态业务模型的数据表结构
     *
     * @param model 动态业务模型信息
     */
    void updateModelTable(DynamicBusinessModelDO model);

    /**
     * 批量更新排序
     *
     * @param sortList 排序列表，包含id和sort字段
     */
    void batchUpdateSort(List<Map<String, Object>> sortList);
} 