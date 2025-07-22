package com.cheers.arch.module.system.controller.admin.field.vo;

import com.cheers.arch.framework.common.pojo.PageParam;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 字段分类关联分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldCategoryRelPageReqVO extends PageParam {

    @Schema(description = "字段ID", example = "1024")
    private Long fieldId;

    @Schema(description = "分类ID", example = "2048")
    private Long categoryId;

    @Schema(description = "字段编码", example = "field_001")
    private String fieldCode;

    @Schema(description = "字段名称", example = "用户名")
    private String fieldName;

} 