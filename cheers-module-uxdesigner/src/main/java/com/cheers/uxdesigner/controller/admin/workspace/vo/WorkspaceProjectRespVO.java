package com.cheers.uxdesigner.controller.admin.workspace.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台项目 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WorkspaceProjectRespVO {

    @Schema(description = "项目ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("项目ID")
    private Long id;

    @Schema(description = "项目名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "我的设计项目")
    @ExcelProperty("项目名称")
    private String name;

    @Schema(description = "项目描述", example = "这是一个设计项目的描述")
    @ExcelProperty("项目描述")
    private String description;

    @Schema(description = "项目封面", example = "https://example.com/cover.jpg")
    @ExcelProperty("项目封面")
    private String cover;

    @Schema(description = "项目类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "web")
    @ExcelProperty("项目类型")
    private String type;

    @Schema(description = "项目状态", example = "1")
    @ExcelProperty("项目状态")
    private Integer status;

    @Schema(description = "用户ID", example = "1")
    @ExcelProperty("用户ID")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    @ExcelProperty("团队ID")
    private Long teamId;

    @Schema(description = "是否收藏", requiredMode = Schema.RequiredMode.REQUIRED, example = "false")
    @ExcelProperty("是否收藏")
    private Boolean starred;

    @Schema(description = "备注", example = "项目备注信息")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

} 