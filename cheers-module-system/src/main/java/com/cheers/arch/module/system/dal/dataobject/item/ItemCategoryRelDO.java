package com.cheers.arch.module.system.dal.dataobject.item;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 数据项分类关系 DO
 * 对应表 system_item_category_rel
 */
@TableName("system_item_category_rel")
@KeySequence("system_item_category_rel_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemCategoryRelDO extends TenantBaseDO {

    /**
     * 主键
     */
    @TableId
    private Long id;

    /**
     * 数据项ID
     */
    private Long itemId;

    /**
     * 数据项类型：device-设备, product-产品, user-用户, field_def-字段定义
     */
    private String itemType;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 分类类型：area-区域分类, facility-设施分类, status-状态分类, brand-品牌分类, department-部门分类, role-角色分类, field-字段分类
     */
    private String categoryType;

    /**
     * 排序
     */
    private Integer sortOrder;
} 