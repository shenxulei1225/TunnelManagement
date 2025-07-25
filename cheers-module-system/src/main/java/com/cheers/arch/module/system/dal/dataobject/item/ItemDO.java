package com.cheers.arch.module.system.dal.dataobject.item;

import java.util.Map;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 系统统一数据项 DO
 * 用于替代多个业务DO（设备、部门、人员等）
 */
@TableName(value = "system_item", autoResultMap = true)
@KeySequence("system_item_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemDO extends TenantBaseDO {

    /**
     * 数据项ID
     */
    @TableId
    private Long id;

    /**
     * 数据项类型
     * 如：device-设备, product-产品, user-用户, department-部门, person-人员
     */
    private String itemType;

    /**
     * 数据项名称
     */
    private String name;

    /**
     * 数据项编码
     */
    private String code;

    /**
     * 数据项描述
     */
    private String description;

    /**
     * 数据项状态
     * 0-禁用, 1-启用
     */
    private Integer status;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 图标
     */
    private String icon;

    /**
     * 颜色
     */
    private String color;

    /**
     * 业务数据（JSON格式）
     * 存储业务特有的字段数据
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> businessData;

    /**
     * 扩展数据（JSON格式）
     * 存储其他扩展信息
     */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraData;
} 