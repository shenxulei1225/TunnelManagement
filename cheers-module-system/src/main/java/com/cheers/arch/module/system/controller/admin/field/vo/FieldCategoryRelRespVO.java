package com.cheers.arch.module.system.controller.admin.field.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 字段分类关联 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldCategoryRelRespVO extends FieldCategoryRelBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "字段编码", example = "field_001")
    private String fieldCode;

    @Schema(description = "字段名称", example = "用户名")
    private String fieldName;

    @Schema(description = "分类名称", example = "基础信息")
    private String categoryName;

} 