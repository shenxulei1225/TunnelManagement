package com.cheers.arch.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cheers.arch.framework.tenant.core.db.TenantBaseDO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 通用动态字段值 DO
 * 一行即某业务实体在某字段上的一个值。
 *
 * 索引建议：(biz_type, biz_id) 复合；(field_id)；如有查询需求可再加 (field_id, biz_id)
 */
@TableName("system_field_value")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldValueDO extends TenantBaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务类型，如 'device', 'product', 'user' */
    private String bizType;

    /** 业务ID */
    private Long bizId;

    /** 字段定义ID */
    private Long fieldId;

    /** 字段值（JSON格式） */
    private String valueJson;

    /**
     * 获取字段值（支持类型转换）
     */
    public <T> T getFieldValue(Class<T> targetType) {
        if (valueJson == null) {
            return null;
        }
        
        // 根据目标类型进行转换
        if (targetType == String.class) {
            return (T) valueJson;
        } else if (targetType == Integer.class) {
            return (T) Integer.valueOf(valueJson);
        } else if (targetType == Long.class) {
            return (T) Long.valueOf(valueJson);
        } else if (targetType == Double.class) {
            return (T) Double.valueOf(valueJson);
        } else if (targetType == Boolean.class) {
            return (T) Boolean.valueOf(valueJson);
        }
        
        // 其他类型可以扩展
        return null;
    }

    /**
     * 设置字段值
     */
    public void setFieldValue(Object value) {
        if (value == null) {
            this.valueJson = null;
        } else {
            this.valueJson = value.toString();
        }
    }
}
