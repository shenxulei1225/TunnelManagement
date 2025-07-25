package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 领域模型拖拽移动 Request VO")
@Data
public class DomainMoveReqVO {

    @Schema(description = "移动的领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "移动的领域ID不能为空")
    private Long id;

    @Schema(description = "目标父领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "目标父领域ID不能为空")
    private Long targetParentId;

    @Schema(description = "目标位置（在目标父节点下的排序）", example = "1")
    private Integer targetSort;

} 