package com.cheers.arch.module.system.controller.admin.field.vo;

import java.util.List;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段批量更新分组关联 Request VO")
@Data
public class FieldBatchUpdateHierarchyReqVO {

    @Schema(description = "字段 ID", required = true, example = "1024")
    @NotNull(message = "字段 ID 不能为空")
    private Long fieldId;

    @Schema(description = "分组 ID 列表", required = true)
    @NotNull(message = "分组 ID 列表不能为空")
    private List<Long> hierarchyGroupIds;

} 