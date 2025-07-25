package com.cheers.arch.module.system.controller.admin.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - 领域字段关联配置")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DomainFieldConfig {

    @Schema(description = "是否必填", example = "true")
    private Boolean required;

    @Schema(description = "排序", example = "1")
    private Integer sort;

    @Schema(description = "备注", example = "配置备注")
    private String remark;

    public static DomainFieldConfig of(Boolean required, Integer sort, String remark) {
        return new DomainFieldConfig(required, sort, remark);
    }

    public static DomainFieldConfig defaultConfig() {
        return new DomainFieldConfig(false, 0, null);
    }

} 