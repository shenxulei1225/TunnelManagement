package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 字段分类关联更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldCategoryRelUpdateReqVO extends FieldCategoryRelBaseVO {

    @Schema(description = "关联ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "关联ID不能为空")
    private Long id;

} 