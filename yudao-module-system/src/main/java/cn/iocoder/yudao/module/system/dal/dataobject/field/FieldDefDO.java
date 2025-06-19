package cn.iocoder.yudao.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 通用自定义字段定义 DO
 *
 * 对应表 system_field_def
 * 说明：
 *   1. 取消 owner_type、type 等业务耦合字段，专注字段本身定义
 *   2. 通过关联表 (field_def_category_rel) 将字段分配给 0-N 个分类；若关联记录为空则视为通用字段
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("system_field_def")
public class FieldDefDO extends cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段唯一标识 */
    private String fieldKey;

    /** 字段显示名称 */
    private String fieldLabel;

    /** 数据类型 string/number/date/enum */
    private String valueType;

    /** 单位，仅当 valueType=number 时使用 */
    private String unit;

    /** 是否必填 */
    private Boolean required;

    /** 枚举值 JSON，value-label 数组，用于 enum 类型 */
    private String enumJson;

    /** 显示排序 */
    private Integer sort;
}
