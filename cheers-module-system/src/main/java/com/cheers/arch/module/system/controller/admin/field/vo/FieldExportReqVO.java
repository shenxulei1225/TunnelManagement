package com.cheers.arch.module.system.controller.admin.field.vo;

import static com.cheers.arch.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段导出 Request VO")
@Data
public class FieldExportReqVO {

    @Schema(description = "字段键名", example = "field_key")
    private String fieldKey;

    @Schema(description = "字段标签", example = "字段标签")
    private String fieldLabel;

    @Schema(description = "值类型", example = "STRING")
    private String valueType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

} 