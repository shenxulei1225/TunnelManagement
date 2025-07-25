package com.cheers.arch.module.system.dal.dataobject.business;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 业务类型定义 DO
 * 对应表 system_business_type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("system_business_type")
public class BusinessTypeDO extends TenantBaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务类型编码 */
    private String typeCode;

    /** 业务类型名称 */
    private String typeName;

    /** 业务类型描述 */
    private String description;

    /** 图标 */
    private String icon;

    /** 排序 */
    private Integer sort;

    /** 状态（0-禁用，1-启用） */
    private Integer status;
} 