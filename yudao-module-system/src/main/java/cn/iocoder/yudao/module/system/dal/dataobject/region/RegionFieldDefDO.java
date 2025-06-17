package cn.iocoder.yudao.module.system.dal.dataobject.region;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 区域自定义字段定义 DO
 * <p>
 * 对应表 system_region_field_def
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Accessors(chain = true)
@TableName("system_region_field_def")
public class RegionFieldDefDO extends cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 字段归属类型，REGION/DEVICE/ALARM */
    private String ownerType;

    /** 适用区域类型，0 表示通用（仅当 ownerType=REGION 时有效） */
    private Integer regionType;

    /** 字段唯一标识 */
    private String fieldKey;

    /** 字段显示名称 */
    private String fieldLabel;

    /** 数据类型 string/number/date/enum */
    private String valueType;

    /** 是否必填 */
    private Boolean required;

    /** 枚举值 JSON，value-label 数组，用于 enum 类型 */
    private String enumJson;

    /** 显示排序 */
    private Integer sort;
}
