package com.cheers.arch.module.system.controller.admin.pagegenerator.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - PageStatisticsRespVO")
@Data
public class PageStatisticsRespVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "名称")
    private String name;

}
