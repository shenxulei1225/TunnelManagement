package com.cheers.arch.module.system.controller.admin.field;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryFieldSaveReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefCategoryRelDO;
import com.cheers.arch.module.system.service.field.FieldCategoryFieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

/**
 * 分类-字段 关联
 */
@Tag(name = "字段分类关联")
@RestController
@RequestMapping("/system/field-category-field")
@RequiredArgsConstructor
public class FieldCategoryFieldController {

    private final FieldCategoryFieldService service;

    @PostMapping("/save")
    @Operation(summary = "保存分类字段")
    public CommonResult<Boolean> save(@Validated @RequestBody FieldCategoryFieldSaveReqVO reqVO) {
        service.save(reqVO.getCategoryId(), reqVO.getFieldId(), Boolean.TRUE.equals(reqVO.getRequired()), reqVO.getSort());
        return success(true);
    }

    @GetMapping("/list-by-category")
    @Operation(summary = "按分类获取字段及必填标记")
    public CommonResult<List<FieldDefCategoryRelDO>> listByCategory(@RequestParam Long categoryId) {
        return success(service.listByCategory(categoryId));
    }
}
