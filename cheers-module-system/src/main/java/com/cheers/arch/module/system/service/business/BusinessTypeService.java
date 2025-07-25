package com.cheers.arch.module.system.service.business;

import java.util.List;

import com.cheers.arch.module.system.dal.dataobject.business.BusinessTypeDO;

/**
 * 业务类型定义 Service 接口
 */
public interface BusinessTypeService {

    /**
     * 获取所有启用的业务类型
     *
     * @return 业务类型列表
     */
    List<BusinessTypeDO> getEnabledBusinessTypes();

    /**
     * 根据编码获取业务类型
     *
     * @param typeCode 业务类型编码
     * @return 业务类型
     */
    BusinessTypeDO getByTypeCode(String typeCode);

    /**
     * 验证业务类型是否存在
     *
     * @param typeCode 业务类型编码
     * @return 是否存在
     */
    boolean existsByTypeCode(String typeCode);
} 