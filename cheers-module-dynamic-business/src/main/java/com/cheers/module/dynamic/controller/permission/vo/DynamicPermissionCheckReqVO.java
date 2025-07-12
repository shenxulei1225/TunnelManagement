package com.cheers.arch.module.dynamic.controller.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 动态权限检查 Request VO")
@Data
public class DynamicPermissionCheckReqVO {

    @Schema(description = "模型编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "user")
    @NotEmpty(message = "模型编码不能为空")
    private String modelCode;

    @Schema(description = "权限类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限类型不能为空")
    private Integer type;

    @Schema(description = "权限目标", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限目标不能为空")
    private Long target;

} 