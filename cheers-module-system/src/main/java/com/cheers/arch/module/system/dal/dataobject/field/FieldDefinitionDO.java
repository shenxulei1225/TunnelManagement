package com.cheers.arch.module.system.dal.dataobject.field;

import java.util.Map;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 字段定义 DO
 * 定义每个业务类型的字段结构
 */
@TableName(value = "system_field_definition", autoResultMap = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldDefinitionDO extends TenantBaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 数据项类型，如 'device', 'product', 'user' */
    private String itemType;

    /** 字段名称（英文） */
    private String fieldName;

    /** 字段标签（中文） */
    private String fieldLabel;

    /** 字段类型：text-文本, number-数字, date-日期, datetime-日期时间, select-选择, textarea-多行文本, boolean-布尔值 */
    private String fieldType;

    /** 字段长度 */
    private Integer fieldLength;

    /** 是否必填：0-否, 1-是 */
    private Integer isRequired;

    /** 是否唯一：0-否, 1-是 */
    private Integer isUnique;

    /** 默认值 */
    private String defaultValue;

    /** 验证规则（JSON格式） */
    private String validationRules;

    /** 选项值（用于select类型，JSON格式） */
    private String options;

    /** 排序 */
    private Integer sortOrder;

    /** 状态：0-禁用, 1-启用 */
    private Integer status;

    /** 字段描述 */
    private String description;

    /** 字段配置（JSON格式） */
    @com.baomidou.mybatisplus.annotation.TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> config;

    /**
     * 获取选项列表
     */
    public Map<String, String> getOptionsMap() {
        if (options == null || options.isEmpty()) {
            return null;
        }
        
        try {
            // 这里可以使用JSON工具类解析options
            // 返回 {value: label} 的Map
            return null; // 需要实现JSON解析
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置选项列表
     */
    public void setOptionsMap(Map<String, String> optionsMap) {
        if (optionsMap == null || optionsMap.isEmpty()) {
            this.options = null;
        } else {
            // 这里可以使用JSON工具类序列化optionsMap
            this.options = null; // 需要实现JSON序列化
        }
    }

    /**
     * 检查是否为必填字段
     */
    public boolean isRequired() {
        return isRequired != null && isRequired == 1;
    }

    /**
     * 检查是否为唯一字段
     */
    public boolean isUnique() {
        return isUnique != null && isUnique == 1;
    }

    /**
     * 检查字段是否启用
     */
    public boolean isEnabled() {
        return status != null && status == 1;
    }
} 