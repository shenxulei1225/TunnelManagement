package com.cheers.arch.module.system.controller.admin.field.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Map;

@Data
public class FieldValueRespVO {
    @Schema(description = "业务 ID")
    private Long bizId;

    @Schema(description = "字段值 map")
    private Map<String, String> values;
}
