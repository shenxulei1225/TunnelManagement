package com.cheers.arch.module.system.controller.admin.tree.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 树形配置创建 Request VO")
@Data
public class TreeConfigCreateReqVO {

    @Schema(description = "配置名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "字段管理树配置")
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    @Schema(description = "使用场景", requiredMode = Schema.RequiredMode.REQUIRED, example = "field-management")
    @NotBlank(message = "使用场景不能为空")
    private String scene;

    @Schema(description = "配置类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "USER")
    @NotBlank(message = "配置类型不能为空")
    private String configType;

    @Schema(description = "配置JSON", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "配置JSON不能为空")
    private String configJson;

    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "备注", example = "用户自定义的树形配置")
    private String remark;

} 