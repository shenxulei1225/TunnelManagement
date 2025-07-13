package com.cheers.arch.module.dynamic.controller.admin.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 动态业务模型 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicModelRespVO extends DynamicModelBaseVO {

    @Schema(description = "编号", required = true, example = "1024")
    private Long id;

    @Schema(description = "目录名称", example = "设备管理")
    private String directoryName;

    @Schema(description = "创建时间", required = true)
    private LocalDateTime createTime;
} 