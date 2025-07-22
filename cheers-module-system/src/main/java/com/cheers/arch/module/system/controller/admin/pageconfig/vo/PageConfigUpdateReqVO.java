package com.cheers.arch.module.system.controller.admin.pageconfig.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 页面配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PageConfigUpdateReqVO extends PageConfigCreateReqVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "配置编号不能为空")
    private Long id;
} 