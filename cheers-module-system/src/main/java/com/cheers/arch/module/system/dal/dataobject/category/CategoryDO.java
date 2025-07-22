package com.cheers.arch.module.system.dal.dataobject.category;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.trees.core.TreeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 分类 DO（树形）- 已迁移到TreeEntity框架
 * 对应表 system_category
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_category")
public class CategoryDO extends com.cheers.arch.framework.tenant.core.db.TenantBaseDO implements TreeEntity<Long> {

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

    /** 图标 */
    private String icon;

    /** 状态（0正常 1停用） */
    private Integer status;

    /** 是否系统只读，1=只读（禁止删除/改名） */
    private Boolean readonly;

    /** 分类描述 */
    private String description;

    // ==================== TreeEntity接口实现 ====================

    @Override
    public CategoryDO setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public CategoryDO setTreePath(String treePath) {
        this.treePath = treePath;
        return this;
    }

    @Override
    public CategoryDO setLevel(Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public CategoryDO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }
} 