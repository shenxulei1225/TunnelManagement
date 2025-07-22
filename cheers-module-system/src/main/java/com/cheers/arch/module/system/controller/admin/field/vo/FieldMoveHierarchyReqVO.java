package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段移动分组 Request VO")
@Data
public class FieldMoveHierarchyReqVO {

    @Schema(description = "字段定义 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段定义 ID 不能为空")
    private Long fieldId;

    @Schema(description = "原分级组 ID", example = "100")
    private Long fromHierarchyGroupId;

    @Schema(description = "目标分级组 ID", example = "200")
    private Long toHierarchyGroupId;

    @Schema(description = "操作类型", example = "MOVE", allowableValues = {"MOVE", "REMOVE"})
    private String operationType = "MOVE";

} 