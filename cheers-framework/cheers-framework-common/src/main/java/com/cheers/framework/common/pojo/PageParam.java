package com.cheers.framework.common.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "分页参数")
@Data
public class PageParam implements Serializable {

    private static final Integer PAGE_NO = 1;
    private static final Integer PAGE_SIZE = 10;

    @Schema(description = "页码，从 1 开始", example = "1")
    private Integer pageNo = PAGE_NO;

    @Schema(description = "每页条数，最大值为 100", example = "10")
    private Integer pageSize = PAGE_SIZE;

    @Schema(description = "排序字段数组")
    private List<SortingField> sortingFields = new ArrayList<>();

} 