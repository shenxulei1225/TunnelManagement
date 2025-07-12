package com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台文件 Response VO")
@Data
public class WorkspaceFileRespVO {

    @Schema(description = "文件ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "文件名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设计文件")
    private String name;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "design")
    private String type;

    @Schema(description = "缩略图URL", example = "https://example.com/thumb.jpg")
    private String thumbnail;

    @Schema(description = "文件内容", example = "{}")
    private String content;

    @Schema(description = "文件分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "drafts")
    private String category;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    private Boolean starred;

    @Schema(description = "文件大小", example = "1024")
    private Long fileSize;

    @Schema(description = "所属项目ID", example = "1")
    private Long projectId;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "备注", example = "这是一个设计文件")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

} 