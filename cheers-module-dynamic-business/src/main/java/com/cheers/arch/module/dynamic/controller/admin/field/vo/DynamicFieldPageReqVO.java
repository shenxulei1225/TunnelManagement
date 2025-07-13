package com.cheers.arch.module.dynamic.controller.admin.field.vo;

import com.cheers.arch.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 动态业务字段分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DynamicFieldPageReqVO extends PageParam {

    @Schema(description = "字段名称", example = "姓名")
    private String name;

    @Schema(description = "字段编码", example = "name")
    private String code;

    @Schema(description = "字段类型", example = "STRING")
    private String type;

    @Schema(description = "业务模型ID", example = "1")
    private Long modelId;
} 