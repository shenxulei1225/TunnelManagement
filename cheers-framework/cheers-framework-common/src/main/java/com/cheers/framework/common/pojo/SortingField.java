package com.cheers.framework.common.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 排序字段 DTO
 */
@Schema(description = "排序字段")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortingField {

    @Schema(description = "字段")
    private String field;

    @Schema(description = "顺序")
    private String order;

    public boolean isAsc() {
        return "asc".equalsIgnoreCase(order);
    }

} 