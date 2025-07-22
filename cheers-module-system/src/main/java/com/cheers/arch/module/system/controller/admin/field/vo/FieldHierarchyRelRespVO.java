package com.cheers.arch.module.system.controller.admin.field.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 字段分级组关联 Response VO")
@Data
public class FieldHierarchyRelRespVO {

    @Schema(description = "关联 ID", example = "1024")
    private Long id;

    @Schema(description = "字段 ID", example = "2048")
    private Long fieldId;

    @Schema(description = "分级组 ID", example = "3072")
    private Long hierarchyGroupId;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

} 