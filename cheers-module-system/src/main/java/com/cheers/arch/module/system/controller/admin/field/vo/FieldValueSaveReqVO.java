package com.cheers.arch.module.system.controller.admin.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

@Data
public class FieldValueSaveReqVO {

    @Schema(description = "业务类型", required = true)
    @NotBlank
    private String bizType;

    @Schema(description = "业务 ID，可为空表示新增后由服务生成")
    private Long bizId;

    @Schema(description = "字段值 map, key=fieldId", required = true)
    @NotNull
    private Map<String, String> values;
}
