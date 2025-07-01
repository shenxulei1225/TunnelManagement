package com.cheers.uxdesigner.controller.admin.workspace.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作台项目分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkspaceProjectPageReqVO extends PageParam {

    @Schema(description = "项目名称", example = "我的设计项目")
    private String name;

    @Schema(description = "项目类型", example = "web")
    private String type;

    @Schema(description = "是否收藏", example = "true")
    private Boolean starred;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "团队ID", example = "1")
    private Long teamId;

    @Schema(description = "项目状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime[] createTime;

} 