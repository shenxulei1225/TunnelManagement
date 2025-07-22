package com.cheers.arch.module.system.controller.admin.pagedesigner.vo;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 构建结果 Response VO")
@Data
public class BuildResultRespVO {

    // 构造器
    public BuildResultRespVO() {}

    public BuildResultRespVO(String buildId, String message) {
        this.buildId = buildId;
        this.message = message;
        this.status = "started";
        this.startTime = LocalDateTime.now();
    }

    @Schema(description = "构建ID", example = "1024")
    private String buildId;

    @Schema(description = "构建状态", example = "success")
    private String status;

    @Schema(description = "构建消息", example = "构建成功")
    private String message;

    @Schema(description = "生成的文件列表")
    private List<String> generatedFiles;

    @Schema(description = "错误信息")
    private List<String> errors;

    @Schema(description = "构建开始时间")
    private LocalDateTime startTime;

    @Schema(description = "构建结束时间")
    private LocalDateTime endTime;

    @Schema(description = "构建耗时(毫秒)")
    private Long duration;
} 