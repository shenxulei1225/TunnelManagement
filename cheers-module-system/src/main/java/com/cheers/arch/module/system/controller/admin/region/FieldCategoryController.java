package com.cheers.arch.module.system.controller.admin.region;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.dal.dataobject.region.FieldCategoryDO;
import com.cheers.arch.module.system.service.region.FieldCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 自定义字段分类")
@RestController
@RequestMapping("/system/field-category")
public class FieldCategoryController {

    @Resource
    private FieldCategoryService categoryService;

    @GetMapping("/tree")
    @Operation(summary = "获得分类树")
    public CommonResult<List<FieldCategoryDO>> getCategoryTree(@RequestParam(value = "bizType", required = false) String bizType) {
        return success(categoryService.getCategoryTree(bizType));
    }

    @PostMapping("/create")
    @Operation(summary = "创建分类")
    public CommonResult<Long> createCategory(@Valid @RequestBody FieldCategoryDO req) {
        return success(categoryService.createCategory(req));
    }

    @PutMapping("/update")
    @Operation(summary = "更新分类")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody FieldCategoryDO req) {
        return success(categoryService.updateCategory(req));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除分类")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        return success(categoryService.deleteCategory(id));
    }
}
