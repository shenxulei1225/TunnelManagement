package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 领域模型 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "领域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    private String name;

    @Schema(description = "领域编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_management")
    private String code;

    @Schema(description = "父领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Long parentId;

    @Schema(description = "领域描述", example = "用户相关的业务领域")
    private String description;

    @Schema(description = "领域类型", example = "business")
    private String type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "备注信息")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "直接关联的字段数量", example = "5")
    private Integer directFieldCount;

    @Schema(description = "直接子领域数量", example = "3")
    private Integer directChildCount;

    @Schema(description = "总字段数量（包含子领域）", example = "15")
    private Integer totalFieldCount;

    @Schema(description = "总子领域数量（包含所有后代）", example = "8")
    private Integer totalChildCount;

    @Schema(description = "层级深度", example = "2")
    private Integer depth;

    @Schema(description = "系统只读标识（0普通 1只读）", example = "0")
    private Integer readonly;

    @Schema(description = "完整路径", example = "1/2/3")
    private String treePath;

    @Schema(description = "层级深度", example = "1")
    private Integer level;

} 