package com.cheers.uxdesigner.controller.admin.workspace.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台文件分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkspaceFilePageReqVO extends PageParam {

    @Schema(description = "文件名称", example = "设计文件")
    private String name;

    @Schema(description = "文件类型", example = "design")
    private String type;

    @Schema(description = "文件分类", example = "drafts")
    private String category;

    @Schema(description = "是否收藏", example = "true")
    private Boolean starred;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

} 