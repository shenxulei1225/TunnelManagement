package com.cheers.arch.module.dynamic.controller.admin.recommendation.vo;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 验证结果 VO")
@Data
public class ValidationResultVO {

    @Schema(description = "是否验证通过", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean valid;

    @Schema(description = "错误信息列表")
    private List<String> errors;

    @Schema(description = "警告信息列表")
    private List<String> warnings;

    @Schema(description = "建议信息列表")
    private List<String> suggestions;
} 