package com.cheers.arch.module.dynamic.service.model;

import com.cheers.arch.module.dynamic.dal.dataobject.model.BusinessModelDO;
import com.cheers.framework.common.pojo.PageResult;

import java.util.List;

/**
 * 业务模型服务接口
 */
public interface BusinessModelService {

    /**
     * 创建业务模型
     *
     * @param model 业务模型信息
     * @return 业务模型ID
     */
    Long createModel(BusinessModelDO model);

    /**
     * 更新业务模型
     *
     * @param model 业务模型信息
     */
    void updateModel(BusinessModelDO model);

    /**
     * 删除业务模型
     *
     * @param id 业务模型ID
     */
    void deleteModel(Long id);

    /**
     * 获取业务模型
     *
     * @param id 业务模型ID
     * @return 业务模型信息
     */
    BusinessModelDO getModel(Long id);

    /**
     * 获取业务模型
     *
     * @param code 业务模型编码
     * @return 业务模型信息
     */
    BusinessModelDO getModelByCode(String code);

    /**
     * 获取业务模型列表
     *
     * @param ids 业务模型ID列表
     * @return 业务模型列表
     */
    List<BusinessModelDO> getModelList(List<Long> ids);

    /**
     * 获取业务模型分页
     *
     * @param pageNo 页码
     * @param pageSize 每页条数
     * @param name 模型名称
     * @param status 状态
     * @return 业务模型分页
     */
    PageResult<BusinessModelDO> getModelPage(Integer pageNo, Integer pageSize, String name, Integer status);

    /**
     * 校验业务模型编码是否唯一
     *
     * @param code 业务模型编码
     * @param excludeId 排除的业务模型ID
     * @return 是否唯一
     */
    boolean isCodeUnique(String code, Long excludeId);

    /**
     * 获取租户的所有业务模型列表
     *
     * @param tenantId 租户ID
     * @return 业务模型列表
     */
    List<BusinessModelDO> getModelListByTenant(Long tenantId);

    /**
     * 初始化业务模型的数据表
     *
     * @param model 业务模型信息
     */
    void initModelTable(BusinessModelDO model);

    /**
     * 更新业务模型的数据表结构
     *
     * @param model 业务模型信息
     */
    void updateModelTable(BusinessModelDO model);
} 