package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 构建前端页面 Request VO")
@Data
public class BuildFrontendReqVO {

    @Schema(description = "页面配置ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "页面配置ID不能为空")
    private Long configId;

    @Schema(description = "构建类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "vue")
    @NotBlank(message = "构建类型不能为空")
    private String buildType;

    @Schema(description = "目标路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "src/views/device/")
    @NotBlank(message = "目标路径不能为空")
    private String targetPath;

    @Schema(description = "是否覆盖现有文件", example = "false")
    private Boolean overwrite = false;

    @Schema(description = "是否生成测试文件", example = "true")
    private Boolean generateTest = true;
} 