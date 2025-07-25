package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 领域统计信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainStatisticsRespVO {

    @Schema(description = "领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long domainId;

    @Schema(description = "领域名称", example = "建筑领域")
    private String domainName;

    @Schema(description = "领域编码", example = "architecture")
    private String domainCode;

    @Schema(description = "直接关联的字段数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Integer directFieldCount;

    @Schema(description = "直接子领域数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    private Integer directChildCount;

    @Schema(description = "递归统计的总字段数量（包含所有子领域）", requiredMode = Schema.RequiredMode.REQUIRED, example = "15")
    private Integer totalFieldCount;

    @Schema(description = "递归统计的总子领域数量（包含所有后代）", requiredMode = Schema.RequiredMode.REQUIRED, example = "8")
    private Integer totalChildCount;

    @Schema(description = "层级深度", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Integer depth;

} 