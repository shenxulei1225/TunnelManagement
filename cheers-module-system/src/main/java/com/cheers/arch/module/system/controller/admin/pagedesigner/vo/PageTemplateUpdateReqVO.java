package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 页面模板更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PageTemplateUpdateReqVO extends PageTemplateCreateReqVO {

    @Schema(description = "模板编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "模板编号不能为空")
    private Long id;
} 