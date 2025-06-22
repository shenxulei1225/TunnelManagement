package cn.iocoder.yudao.module.system.dal.dataobject.field;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.time.LocalDateTime;

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
public class FieldValueDO {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 业务类型，如 'pipeline' */
    private String bizType;

    /** 业务记录 ID */
    private Long bizId;

    /** 字段定义 ID */
    private Long fieldId;

    /** 租户编号 */
    @TableField("tenant_id")
    private Long tenantId;

    /** 值 JSON */
    private String valueJson;

    /** 创建时间 */
    @TableField("create_time")
    private LocalDateTime createTime;

    /** 更新时间 */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
