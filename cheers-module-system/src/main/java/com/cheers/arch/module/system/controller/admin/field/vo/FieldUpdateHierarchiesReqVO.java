package com.cheers.arch.module.system.controller.admin.field.vo;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段批量更新分级组 Request VO")
@Data
public class FieldUpdateHierarchiesReqVO {

    @Schema(description = "字段 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段 ID 不能为空")
    private Long fieldId;

    @Schema(description = "分级组 ID 列表", example = "[2048, 3072]")
    private List<Long> hierarchyGroupIds;

} 