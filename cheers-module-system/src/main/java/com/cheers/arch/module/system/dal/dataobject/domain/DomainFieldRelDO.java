package com.cheers.arch.module.system.dal.dataobject.domain;

import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 领域模型字段关联 DO
 *
 * @author cheers
 */
@TableName(value = "system_domain_field_rel", autoResultMap = true)
@KeySequence("system_domain_field_rel_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainFieldRelDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 领域ID
     */
    private Long domainId;

    /**
     * 字段ID
     */
    private Long fieldId;

    /**
     * 是否必填
     */
    private Boolean required;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

} 