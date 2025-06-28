package com.cheers.uxdesigner.controller.admin.workspace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Schema(description = "管理后台 - 工作台文件新增 Request VO")
@Data
public class WorkspaceFileCreateReqVO {

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设计文件")
    @NotEmpty(message = "文件名称不能为空")
    private String name;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "design")
    @NotEmpty(message = "文件类型不能为空")
    private String type;

    @Schema(description = "缩略图URL", example = "https://example.com/thumb.jpg")
    private String thumbnail;

    @Schema(description = "文件内容", example = "{}")
    private String content;

    @Schema(description = "文件分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "drafts")
    @NotEmpty(message = "文件分类不能为空")
    private String category;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @NotNull(message = "是否收藏不能为空")
    private Boolean starred;

    @Schema(description = "所属项目ID", example = "1")
    private Long projectId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "备注", example = "这是一个设计文件")
    private String remark;

} 