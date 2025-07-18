package com.cheers.arch.module.dynamic.dal.dataobject.record;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;
import com.cheers.arch.framework.trees.core.TreeEntity;
import com.cheers.arch.framework.trees.utils.TreeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态业务数据记录
 * 实现TreeEntity接口，支持树形结构
 * 实现DynamicTreeEntity接口，支持动态字段
 */
@TableName("dynamic_business_record")
@KeySequence("dynamic_business_record_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicBusinessRecordDO extends TenantBaseDO implements TreeEntity<Long>, TreeUtils.DynamicTreeEntity {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 记录ID
     */
    @TableId
    private Long id;

    /**
     * 业务模型编码
     */
    private String modelCode;

    /**
     * 父记录ID（树形结构使用）
     */
    private Long parentId;

    /**
     * 树路径（树形结构使用）
     */
    private String treePath;

    /**
     * 层级（树形结构使用）
     */
    private Integer level;

    /**
     * 排序号
     */
    private Integer sort;

    /**
     * 状态（0:禁用，1:启用）
     */
    private Integer status;

    /**
     * 数据JSON（存储所有字段值）
     */
    private String data;

    /**
     * 记录名称（从data中解析或单独存储）
     */
    private String name;

    /**
     * 记录编码（从data中解析或单独存储）
     */
    private String code;

    /**
     * 是否只读
     */
    private Boolean readonly;

    // TreeEntity接口实现方法

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public Long getParentId() {
        return this.parentId;
    }

    @Override
    public String getTreePath() {
        return this.treePath;
    }

    @Override
    public Integer getLevel() {
        return this.level;
    }

    @Override
    public Integer getSort() {
        return this.sort;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public Integer getStatus() {
        return this.status;
    }

    @Override
    public Boolean getReadonly() {
        return this.readonly != null ? this.readonly : false;
    }

    // DynamicTreeEntity接口实现方法
    @Override
    public Map<String, Object> getDynamicFields() {
        Map<String, Object> fields = new HashMap<>();
        
        // 解析JSON数据
        if (data != null && !data.isEmpty()) {
            try {
                Map<String, Object> dataMap = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {});
                fields.putAll(dataMap);
            } catch (JsonProcessingException e) {
                // 解析失败时返回空Map
            }
        }
        
        return fields;
    }

    // 设置方法，支持链式调用

    public DynamicBusinessRecordDO setId(Long id) {
        this.id = id;
        return this;
    }

    public DynamicBusinessRecordDO setParentId(Long parentId) {
        this.parentId = parentId;
        return this;
    }

    public DynamicBusinessRecordDO setTreePath(String treePath) {
        this.treePath = treePath;
        return this;
    }

    public DynamicBusinessRecordDO setLevel(Integer level) {
        this.level = level;
        return this;
    }

    public DynamicBusinessRecordDO setSort(Integer sort) {
        this.sort = sort;
        return this;
    }

    public DynamicBusinessRecordDO setName(String name) {
        this.name = name;
        return this;
    }

    public DynamicBusinessRecordDO setCode(String code) {
        this.code = code;
        return this;
    }

    public DynamicBusinessRecordDO setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public DynamicBusinessRecordDO setReadonly(Boolean readonly) {
        this.readonly = readonly;
        return this;
    }

    public DynamicBusinessRecordDO setData(String data) {
        this.data = data;
        return this;
    }

    public DynamicBusinessRecordDO setModelCode(String modelCode) {
        this.modelCode = modelCode;
        return this;
    }
} 