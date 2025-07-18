package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段匹配 VO")
@Data
public class FieldMatchVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection_date")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "巡检日期")
    private String fieldName;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "date")
    private String fieldType;

    @Schema(description = "匹配度", example = "0.95")
    private Double matchScore;

    @Schema(description = "匹配原因", example = "基于用户历史行为推荐")
    private String matchReason;

    @Schema(description = "是否推荐", example = "true")
    private Boolean recommended;
} 