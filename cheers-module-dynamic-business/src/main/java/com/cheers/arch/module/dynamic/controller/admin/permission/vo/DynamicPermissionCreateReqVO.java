package com.cheers.arch.module.dynamic.controller.admin.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 动态权限创建 Request VO")
@Data
public class DynamicPermissionCreateReqVO {

    @Schema(description = "权限名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @Schema(description = "权限类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限类型不能为空")
    private Integer type;

    @Schema(description = "权限级别", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "权限级别不能为空")
    private Integer level;

    @Schema(description = "权限描述", example = "用户管理权限")
    private String description;

    @Schema(description = "业务模型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "业务模型ID不能为空")
    private Long businessModelId;

} 