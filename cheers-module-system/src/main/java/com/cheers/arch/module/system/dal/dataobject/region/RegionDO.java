package com.cheers.arch.module.system.dal.dataobject.region;

import java.util.Map;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.cheers.arch.framework.trees.core.TreeEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 区域 DO
 *
 * @author 芋道源码
 */
@TableName(value = "system_region", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class RegionDO extends TenantBaseDO implements TreeEntity<Long> {

    /**
     * 区域编号
     */
    private Long id;
    
    /**
     * 区域名称
     */
    private String name;
    
    /**
     * 区域编码
     */
    private String code;
    
    /**
     * 父区域编号
     */
    private Long parentId;
    
    /**
     * 显示顺序
     */
    private Integer sort;
    
    /**
     * 所属分类ID
     */
    private Long categoryId;

    /**
     * 区域状态
     */
    private Integer status;

    /**
     * 是否只读
     */
    private Boolean readonly;
    
    /**
     * 扩展属性（JSON 格式）
     */
    @TableField(value = "extra_attrs", typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> extraAttrs;

    /**
     * 层级路径
     * 格式: /1/2/3/ 表示一个三级节点的完整路径
     */
    private String treePath;

    /**
     * 层级深度
     * 用于优化查询性能，根节点level=1
     */
    private Integer level;

    /**
     * 是否为叶子节点
     * 这个字段由数据库查询时动态设置,不存储在数据库中
     */
    @TableField(exist = false)
    private Boolean leaf;

    /**
     * 是否禁用
     * 这个字段由数据库查询时动态设置,不存储在数据库中
     */
    @TableField(exist = false)
    private Boolean disabled;

    /**
     * 构建树路径
     */
    public void buildTreePath(String parentTreePath) {
        this.treePath = (parentTreePath == null ? "/" : parentTreePath) + this.id + "/";
        this.level = this.treePath.split("/").length - 1; // 计算层级深度
    }

    // ==================== TreeEntity 接口方法实现 ====================
    
    @Override
    public TreeEntity<Long> setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    @Override
    public TreeEntity<Long> setTreePath(String treePath) {
        this.treePath = treePath;
        return this;
    }

    @Override
    public TreeEntity<Long> setLevel(Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public TreeEntity<Long> setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    @Override
    public Boolean getReadonly() {
        return this.readonly;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }
} 