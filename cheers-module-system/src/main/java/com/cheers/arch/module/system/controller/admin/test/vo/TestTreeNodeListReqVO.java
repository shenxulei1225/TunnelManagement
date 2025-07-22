package com.cheers.arch.module.system.controller.admin.test.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 测试树节点列表 Request VO")
@Data
public class TestTreeNodeListReqVO {

    @Schema(description = "节点名称", example = "总公司")
    private String name;

    @Schema(description = "节点类型", example = "dept")
    private String type;

    @Schema(description = "业务类型", example = "dept")
    private String businessType;

    @Schema(description = "状态", example = "1")
    private Integer status;

} 