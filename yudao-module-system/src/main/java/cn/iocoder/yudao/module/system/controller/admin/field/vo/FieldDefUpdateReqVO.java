package cn.iocoder.yudao.module.system.controller.admin.field.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class FieldDefUpdateReqVO {
    @NotNull
    private Long id;

    @NotBlank
    private String fieldLabel;
    @NotBlank
    private String valueType;
    private String unit;
    private Boolean required;
    private String calcExpr;
    private String enumJson;
    private Integer sort;

    private List<Long> categoryIds;
}
