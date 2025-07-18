package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段推荐 VO")
@Data
public class FieldRecommendationVO {

    @Schema(description = "字段编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection_date")
    private String fieldCode;

    @Schema(description = "字段名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "巡检日期")
    private String fieldName;

    @Schema(description = "字段类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "date")
    private String fieldType;

    @Schema(description = "是否必填", example = "true")
    private Boolean required;

    @Schema(description = "默认值")
    private String defaultValue;

    @Schema(description = "推荐理由", example = "巡检业务必备字段")
    private String recommendationReason;

    @Schema(description = "推荐权重", example = "0.9")
    private Double weight;

    @Schema(description = "字段配置")
    private String fieldConfig;
} 