package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 字段在领域间移动 Request VO")
@Data
public class DomainFieldMoveReqVO {

    @Schema(description = "字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "字段ID不能为空")
    private Long fieldId;

    @Schema(description = "源领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "源领域ID不能为空")
    private Long sourceDomainId;

    @Schema(description = "目标领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "目标领域ID不能为空")
    private Long targetDomainId;

    @Schema(description = "在目标领域中是否必填", example = "true")
    private Boolean required;

    @Schema(description = "在目标领域中的排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "从建筑领域移动到设备领域")
    private String remark;

} 