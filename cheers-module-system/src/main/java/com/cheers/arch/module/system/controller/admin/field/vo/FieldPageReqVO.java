package com.cheers.arch.module.system.controller.admin.field.vo;

import static com.cheers.arch.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

import java.time.LocalDateTime;

import com.cheers.arch.framework.common.pojo.PageParam;

import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 字段分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldPageReqVO extends PageParam {

    @Schema(description = "字段编码", example = "field_code")
    private String fieldCode;

    @Schema(description = "字段名称", example = "字段名称")
    private String fieldName;

    @Schema(description = "显示名称", example = "显示名称")
    private String display;

    @Schema(description = "字段类型", example = "STRING")
    private String fieldType;

    @Schema(description = "是否自定义字段", example = "true")
    private Boolean isCustom;

    @Schema(description = "状态（0正常 1停用）", example = "0")
    private Integer status;

    @Schema(description = "创建开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeBegin;

    @Schema(description = "创建结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTimeEnd;

} 