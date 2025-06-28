package com.cheers.uxdesigner.controller.admin.workspace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台项目 Response VO")
@Data
public class WorkspaceProjectRespVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设计项目")
    private String name;

    @Schema(description = "项目描述", example = "项目详细描述")
    private String description;

    @Schema(description = "项目封面", example = "https://example.com/cover.jpg")
    private String cover;

    @Schema(description = "项目类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "web")
    private String type;

    @Schema(description = "项目状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    private Boolean starred;

    @Schema(description = "备注", example = "项目备注信息")
    private String remark;

    @Schema(description = "创建者", example = "admin")
    private String creator;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "更新者", example = "admin")
    private String updater;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime updateTime;

} 