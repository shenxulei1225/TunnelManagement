package com.cheers.arch.module.system.controller.admin.tree.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.format.annotation.DateTimeFormat;

import com.cheers.arch.framework.common.pojo.PageParam;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 树形配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class TreeConfigPageReqVO extends PageParam {

    @Schema(description = "配置名称", example = "字段管理树配置")
    private String configName;

    @Schema(description = "使用场景", example = "field-management")
    private String scene;

    @Schema(description = "配置类型", example = "USER")
    private String configType;

    @Schema(description = "用户ID", example = "1001")
    private Long userId;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;

} 