package com.cheers.arch.module.system.dal.dataobject.region;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 区域自定义字段值 DO
 * 对应表 system_region_field_value
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_field_value")
public class RegionFieldValueDO extends com.cheers.arch.framework.mybatis.core.dataobject.BaseDO {

    /** 主键 */
    @TableId
    private Long id;

    /** 字段归属类型 */
    private String ownerType;

    /** 区域 ID */
    private Long regionId;

    /** 字段 key */
    private String fieldKey;

    /** 字段值（统一字符串存储） */
    private String valueText;
}
