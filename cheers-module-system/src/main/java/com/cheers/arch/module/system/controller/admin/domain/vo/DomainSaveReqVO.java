package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "管理后台 - 领域模型新增/修改 Request VO")
@Data
public class DomainSaveReqVO {

    @Schema(description = "主键", example = "1024")
    private Long id;

    @Schema(description = "领域名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "用户管理")
    @NotBlank(message = "领域名称不能为空")
    @Size(max = 100, message = "领域名称长度不能超过100个字符")
    private String name;

    @Schema(description = "领域编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "user_management")
    @NotBlank(message = "领域编码不能为空")
    @Size(max = 50, message = "领域编码长度不能超过50个字符")
    private String code;

    @Schema(description = "父领域ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @NotNull(message = "父领域ID不能为空")
    private Long parentId;

    @Schema(description = "领域描述", example = "用户相关的业务领域")
    @Size(max = 500, message = "领域描述长度不能超过500个字符")
    private String description;

    @Schema(description = "领域类型", example = "business")
    @Size(max = 50, message = "领域类型长度不能超过50个字符")
    private String type;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "备注信息")
    @Size(max = 1000, message = "备注长度不能超过1000个字符")
    private String remark;

    @Schema(description = "系统只读标识（0普通 1只读）", example = "0")
    private Integer readonly;

} 