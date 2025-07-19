package com.cheers.arch.framework.tenant.core.db;

import com.cheers.arch.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 拓展多租户的 BaseDO 基类
 *
 * 这个类在 BaseDO 基础上增加了多租户支持
 * 适用于需要多租户功能的数据库实体对象
 *
 * @author cheers
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class TenantBaseDO extends BaseDO {

    /**
     * 多租户编号
     */
    private Long tenantId;

}
