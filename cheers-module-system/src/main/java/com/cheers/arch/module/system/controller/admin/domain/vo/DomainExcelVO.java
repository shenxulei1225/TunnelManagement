package com.cheers.arch.module.system.controller.admin.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 领域模型 Excel VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ExcelIgnoreUnannotated
public class DomainExcelVO {

    @ExcelProperty("主键")
    private Long id;

    @ExcelProperty("领域名称")
    private String name;

    @ExcelProperty("领域编码")
    private String code;

    @ExcelProperty("父领域ID")
    private Long parentId;

    @ExcelProperty("领域描述")
    private String description;

    @ExcelProperty("领域类型")
    private String type;

    @ExcelProperty("状态")
    private Integer status;

    @ExcelProperty("排序")
    private Integer sort;

    @ExcelProperty("备注")
    private String remark;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

} 