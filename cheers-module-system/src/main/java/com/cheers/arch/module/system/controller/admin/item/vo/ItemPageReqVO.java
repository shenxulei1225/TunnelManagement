package com.cheers.arch.module.system.controller.admin.item.vo;

import com.cheers.arch.framework.common.pojo.PageParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 数据项分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ItemPageReqVO extends PageParam {

    @Schema(description = "数据项类型", example = "device")
    private String itemType;

    @Schema(description = "名称", example = "示例设备")
    private String name;

    @Schema(description = "编码", example = "DEVICE_001")
    private String code;

    @Schema(description = "状态", example = "1")
    private Integer status;
} 