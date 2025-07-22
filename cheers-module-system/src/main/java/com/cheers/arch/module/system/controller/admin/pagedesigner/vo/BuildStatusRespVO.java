package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 构建状态 Response VO")
@Data
public class BuildStatusRespVO {

    // 构造器
    public BuildStatusRespVO() {}

    public BuildStatusRespVO(String buildId, String status, Integer progress, String message, LocalDateTime startTime, Long estimatedRemaining) {
        this.buildId = buildId;
        this.status = status;
        this.progress = progress;
        this.message = message;
        this.startTime = startTime;
        this.estimatedRemaining = estimatedRemaining;
    }

    @Schema(description = "构建ID", example = "1024")
    private String buildId;

    @Schema(description = "构建状态", example = "running")
    private String status;

    @Schema(description = "当前进度(0-100)", example = "75")
    private Integer progress;

    @Schema(description = "当前步骤", example = "生成Vue组件")
    private String currentStep;

    @Schema(description = "状态消息", example = "正在生成页面组件...")
    private String message;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "预计剩余时间(秒)")
    private Long estimatedRemaining;
} 