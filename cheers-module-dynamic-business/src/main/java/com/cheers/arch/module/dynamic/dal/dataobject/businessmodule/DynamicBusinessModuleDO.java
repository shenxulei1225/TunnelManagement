package com.cheers.arch.module.dynamic.dal.dataobject.businessmodule;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.trees.core.TreeEntity;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 动态业务分组/系统 DO（树形）
 * 对应表 business_module
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("business_module")
public class DynamicBusinessModuleDO extends TenantBaseDO implements TreeEntity<Long> {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 父节点 ID，0 表示根 */
    private Long parentId;

    /** 节点编码 */
    private String code;

    /** 展示名称 */
    private String name;

    /** 树路径，如 "1/15/37" */
    private String treePath;

    /** 层级深度 */
    private Integer level;

    /** 排序 */
    private Integer sort;

    /** 是否系统只读，1=只读（禁止删除/改名） */
    private Boolean readonly;

    /** 状态：1=启用，0=禁用 */
    private Integer status;
}
