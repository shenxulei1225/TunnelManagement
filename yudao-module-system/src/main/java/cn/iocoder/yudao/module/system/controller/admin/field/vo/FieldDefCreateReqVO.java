package cn.iocoder.yudao.module.system.controller.admin.field.vo;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Data
public class FieldDefCreateReqVO {

    @NotBlank
    private String fieldLabel;
    @NotBlank
    private String valueType;
    private String unit;
    private Boolean required;
    private String enumJson;
    private Integer sort;

    // 所属分类 IDs；为空表示通用字段
    private List<Long> categoryIds;
}
