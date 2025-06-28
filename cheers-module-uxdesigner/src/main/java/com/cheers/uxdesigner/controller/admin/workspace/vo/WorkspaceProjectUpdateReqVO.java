package com.cheers.uxdesigner.controller.admin.workspace.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "管理后台 - 工作台项目更新 Request VO")
@Data
public class WorkspaceProjectUpdateReqVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "项目ID不能为空")
    private Long id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "设计项目")
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100个字符")
    private String name;

    @Schema(description = "项目描述", example = "项目详细描述")
    @Size(max = 500, message = "项目描述长度不能超过500个字符")
    private String description;

    @Schema(description = "项目封面", example = "https://example.com/cover.jpg")
    @Size(max = 500, message = "项目封面URL长度不能超过500个字符")
    private String cover;

    @Schema(description = "项目类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "web")
    @NotBlank(message = "项目类型不能为空")
    @Size(max = 50, message = "项目类型长度不能超过50个字符")
    private String type;

    @Schema(description = "项目状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "项目状态不能为空")
    private Integer status;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "是否收藏", example = "false")
    private Boolean starred;

    @Schema(description = "备注", example = "项目备注信息")
    @Size(max = 500, message = "备注长度不能超过500个字符")
    private String remark;

} 