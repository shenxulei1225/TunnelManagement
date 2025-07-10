package com.cheers.framework.common.biz.system.tenant;

import java.util.List;

/**
 * 租户通用 API 接口
 */
public interface TenantCommonApi {

    /**
     * 获得所有租户
     *
     * @return 租户编号数组
     */
    List<Long> getTenantIds();

    /**
     * 校验租户是否合法
     *
     * @param id 租户编号
     */
    void validTenant(Long id);

} 