package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段配置 VO")
@Data
public class FieldConfigVO {

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

    @Schema(description = "显示类型", example = "text")
    private String displayType;

    @Schema(description = "字段配置")
    private Map<String, Object> config;

    @Schema(description = "验证规则")
    private Map<String, Object> validationRules;

    @Schema(description = "排序", example = "1")
    private Integer sort;
} 