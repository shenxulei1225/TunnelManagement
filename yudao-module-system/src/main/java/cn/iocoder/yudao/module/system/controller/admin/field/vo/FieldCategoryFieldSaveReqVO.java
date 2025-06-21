package cn.iocoder.yudao.module.system.controller.admin.field.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 分类字段保存请求 VO
 */
@Data
public class FieldCategoryFieldSaveReqVO {

    @NotNull(message = "分类不能为空")
    private Long categoryId;

    @NotNull(message = "字段不能为空")
    private Long fieldId;

    private Boolean required;

    private Integer sort;
}
