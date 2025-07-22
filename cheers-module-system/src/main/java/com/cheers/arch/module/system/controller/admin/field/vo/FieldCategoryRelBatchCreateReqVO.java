package com.cheers.arch.module.system.controller.admin.field.vo;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段分类关联批量创建 Request VO")
@Data
public class FieldCategoryRelBatchCreateReqVO {

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    @Schema(description = "分类ID列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "分类ID列表不能为空")
    private List<Long> categoryIds;

    @Schema(description = "分类ID与排序的映射关系", example = "{\"1\": 10, \"2\": 20}")
    private Map<Long, Integer> categoryIdSortMap;

} 