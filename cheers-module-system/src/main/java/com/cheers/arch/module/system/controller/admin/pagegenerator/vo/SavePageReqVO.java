package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 保存页面 Request VO")
@Data
public class SavePageReqVO {

    @Schema(description = "页面路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "/system/device")
    @NotBlank(message = "页面路径不能为空")
    private String pagePath;

    @Schema(description = "页面代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "页面代码不能为空")
    private String pageCode;

}
