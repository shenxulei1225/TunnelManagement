package com.cheers.arch.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 字段更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FieldUpdateReqVO extends FieldBaseVO {

    @Schema(description = "字段编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "字段编号不能为空")
    private Long id;

} 