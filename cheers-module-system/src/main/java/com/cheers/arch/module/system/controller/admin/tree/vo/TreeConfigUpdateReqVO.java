package com.cheers.arch.module.system.controller.admin.tree.vo;

import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "管理后台 - 树形配置更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeConfigUpdateReqVO extends TreeConfigCreateReqVO {

    @Schema(description = "配置编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "配置编号不能为空")
    private Long id;

} 