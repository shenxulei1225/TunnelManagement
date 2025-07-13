package com.cheers.arch.module.dynamic.controller.admin.model.vo;

import com.cheers.arch.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 动态业务模型分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicModelPageReqVO extends PageParam {

    @Schema(description = "模型名称", example = "用户管理")
    private String name;

    @Schema(description = "模型编码", example = "user")
    private String code;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "目录ID", example = "1")
    private Long directoryId;
} 