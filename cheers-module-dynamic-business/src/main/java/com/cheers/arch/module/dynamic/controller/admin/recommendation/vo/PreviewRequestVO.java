package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 预览请求 VO")
@Data
public class PreviewRequestVO {

    @Schema(description = "模型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "inspection")
    @NotBlank(message = "模型编码不能为空")
    private String modelCode;

    @Schema(description = "视图类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "list")
    @NotBlank(message = "视图类型不能为空")
    private String viewType;

    @Schema(description = "字段配置列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "字段配置不能为空")
    private List<FieldConfigVO> fields;

    @Schema(description = "布局配置")
    private String layoutConfig;

    @Schema(description = "操作配置")
    private String actionConfig;
} 