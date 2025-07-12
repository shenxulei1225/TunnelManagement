package com.cheers.module.dynamic.controller.permission.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 动态业务权限 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicPermissionRespVO extends DynamicPermissionBaseVO {

    @Schema(description = "权限编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;
} 