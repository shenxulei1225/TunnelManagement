package com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 工作台项目修改 Request VO")
@Data
public class WorkspaceProjectUpdateReqVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "项目ID不能为空")
    private Long id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "我的设计项目")
    @NotEmpty(message = "项目名称不能为空")
    private String name;

    @Schema(description = "项目描述", example = "这是一个设计项目的描述")
    private String description;

    @Schema(description = "项目封面", example = "https://example.com/cover.jpg")
    private String cover;

    @Schema(description = "项目类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "web")
    @NotEmpty(message = "项目类型不能为空")
    private String type;

    @Schema(description = "项目状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "项目状态不能为空")
    private Integer status;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "是否收藏不能为空")
    private Boolean starred;

    @Schema(description = "备注", example = "项目备注信息")
    private String remark;

} 