package com.cheers.arch.module.system.dal.mysql.pageconfig;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.pageconfig.PageConfigDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 页面配置 Mapper
 *
 * @author cheers
 */
@Mapper
public interface PageConfigMapper extends BaseMapperX<PageConfigDO> {

    /**
     * 根据配置键和租户ID查询页面配置
     *
     * @param configKey 配置键
     * @param tenantId 租户ID
     * @return 页面配置
     */
    default PageConfigDO selectByConfigKeyAndTenantId(String configKey, Long tenantId) {
        return selectOne(PageConfigDO::getConfigKey, configKey, PageConfigDO::getTenantId, tenantId);
    }

} 