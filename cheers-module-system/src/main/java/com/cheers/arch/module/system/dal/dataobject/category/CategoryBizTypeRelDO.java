package com.cheers.arch.module.system.dal.dataobject.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分类业务类型关联关系 DO
 * 用于关联分类与具体业务类型
 * 对应表 system_category_biz_type_rel
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_category_biz_type_rel")
public class CategoryBizTypeRelDO extends com.cheers.arch.framework.tenant.core.db.TenantBaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 分类编号 */
    private Long categoryId;

    /** 业务类型 */
    private String businessType;

    /** 业务对象ID */
    private Long businessId;

    /** 关联类型（如：字段分类、产品分类等） */
    private String relType;

    /** 排序 */
    private Integer sort;

    /** 是否必填 */
    private Boolean required;
} 