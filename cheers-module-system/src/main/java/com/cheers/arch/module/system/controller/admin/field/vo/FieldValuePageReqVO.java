package com.cheers.arch.module.system.controller.admin.field.vo;

import com.cheers.arch.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FieldValuePageReqVO extends PageParam {
    @Schema(description = "业务类型", required = true)
    private String bizType;
}
