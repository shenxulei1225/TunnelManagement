package com.cheers.system.controller.admin.region.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;

@Schema(description = "管理后台 - 区域新增/修改 Request VO")
@Data
public class RegionSaveReqVO {

    @Schema(description = "区域编号", example = "1024")
    private Long id;

    @Schema(description = "区域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "华东区")
    @NotBlank(message = "区域名称不能为空")
    @Size(max = 30, message = "区域名称长度不能超过30个字符")
    private String name;

    @Schema(description = "父区域编号", example = "1024")
    private Long parentId;

    @Schema(description = "显示顺序", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "显示顺序不能为空")
    private Integer sort;

    @Schema(description = "所属分类ID", example = "1")
    private Long categoryId;

    @Schema(description = "区域状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "区域状态不能为空")
    private Integer status;

    @Schema(description = "扩展属性", example = "{\"key1\":\"value1\",\"key2\":\"value2\"}")
    private Map<String, Object> extraAttrs;

}