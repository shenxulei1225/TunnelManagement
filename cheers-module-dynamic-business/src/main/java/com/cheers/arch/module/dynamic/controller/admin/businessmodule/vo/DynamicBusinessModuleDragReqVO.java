package com.cheers.arch.module.dynamic.controller.admin.businessmodule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 动态业务分组拖拽调整请求 VO")
@Data
public class DynamicBusinessModuleDragReqVO {

    @Schema(description = "拖拽的节点ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "拖拽节点ID不能为空")
    private Long dragId;

    @Schema(description = "目标父节点ID，0表示根节点", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "目标父节点ID不能为空")
    private Long targetParentId;

    @Schema(description = "拖拽位置：before-目标节点前，after-目标节点后，inner-目标节点内部", requiredMode = Schema.RequiredMode.REQUIRED, example = "inner")
    @NotNull(message = "拖拽位置不能为空")
    private String position;

    @Schema(description = "目标节点ID（当position为before或after时使用）", example = "1025")
    private Long targetId;
} 