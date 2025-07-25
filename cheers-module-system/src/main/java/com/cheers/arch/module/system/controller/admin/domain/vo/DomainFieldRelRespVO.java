package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 领域字段关联 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainFieldRelRespVO {

    @Schema(description = "关联ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long domainId;

    @Schema(description = "领域名称", example = "建筑领域")
    private String domainName;

    @Schema(description = "领域编码", example = "architecture")
    private String domainCode;

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long fieldId;

    @Schema(description = "字段名称", example = "建筑高度")
    private String fieldName;

    @Schema(description = "字段编码", example = "building_height")
    private String fieldCode;

    @Schema(description = "字段类型", example = "number")
    private String fieldType;

    @Schema(description = "是否必填", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean required;

    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

} 