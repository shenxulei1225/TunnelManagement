package com.cheers.arch.module.system.controller.admin.category;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryRespVO;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.service.category.CategoryService;
import com.cheers.arch.module.system.service.field.FieldCategoryRelService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "管理后台 - 分类管理")
@RestController
@RequestMapping("/system/category")
@Validated
public class CategoryController {

    @Resource
    private CategoryService categoryService;
    
    @Resource
    private FieldCategoryRelService fieldCategoryRelService;

    @PostMapping("/create")
    @Operation(summary = "创建分类")
    @PreAuthorize("@ss.hasPermission('system:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody CategoryCreateReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新分类")
    @PreAuthorize("@ss.hasPermission('system:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody CategoryUpdateReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:category:query')")
    public CommonResult<CategoryRespVO> getCategory(@RequestParam("id") Long id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, CategoryRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得分类列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:category:query')")
    public CommonResult<List<CategoryRespVO>> getCategoryList(@RequestParam("ids") List<Long> ids) {
        List<CategoryDO> list = categoryService.getCategoryList(ids);
        List<CategoryRespVO> respList = list.stream()
                .map(category -> BeanUtils.toBean(category, CategoryRespVO.class))
                .collect(java.util.stream.Collectors.toList());
        return success(respList);
    }

    @GetMapping("/tree")
    @Operation(summary = "获得分类树")
    @Parameter(name = "businessType", description = "业务类型", example = "FIELD_CATEGORY")
    @Parameter(name = "includeFieldCount", description = "是否包含字段统计", example = "false")
    @PreAuthorize("@ss.hasPermission('system:category:query')")
    public CommonResult<List<CategoryRespVO>> getCategoryTree(@RequestParam(value = "businessType", required = false) String businessType,
                                                             @RequestParam(value = "includeFieldCount", required = false, defaultValue = "false") Boolean includeFieldCount) {
        List<CategoryDO> list;
        if (businessType != null && !businessType.trim().isEmpty()) {
            list = categoryService.getCategoryTreeByBusinessType(businessType);
        } else {
            list = categoryService.getCategoryTree();
        }
        
        List<CategoryRespVO> respList = list.stream()
                .map(category -> {
                    CategoryRespVO vo = BeanUtils.toBean(category, CategoryRespVO.class);
                    
                    // 如果需要包含字段统计
                    if (includeFieldCount != null && includeFieldCount) {
                        try {
                            List<FieldDO> fields = fieldCategoryRelService.getFieldsByCategoryId(category.getId());
                            vo.setFieldCount((long) fields.size());
                            
                            // 设置字段标签列表（前5个）
                            List<String> fieldLabels = fields.stream()
                                    .limit(5)
                                    .map(FieldDO::getFieldName)
                                    .collect(java.util.stream.Collectors.toList());
                            vo.setFieldLabels(fieldLabels);
                        } catch (Exception e) {
                            vo.setFieldCount(0L);
                            vo.setFieldLabels(java.util.Collections.emptyList());
                        }
                    }
                    
                    return vo;
                })
                .collect(java.util.stream.Collectors.toList());
        return success(respList);
    }

    @GetMapping("/list-by-parent")
    @Operation(summary = "根据父分类获得分类列表")
    @Parameter(name = "parentId", description = "父分类编号", example = "1024")
    @Parameter(name = "businessType", description = "业务类型", example = "FIELD_CATEGORY")
    @PreAuthorize("@ss.hasPermission('system:category:query')")
    public CommonResult<List<CategoryRespVO>> getCategoryListByParentId(@RequestParam(value = "parentId", required = false) Long parentId,
                                                                       @RequestParam(value = "businessType", required = false) String businessType) {
        List<CategoryDO> list = categoryService.getCategoryListByParentId(parentId);
        List<CategoryRespVO> respList = list.stream()
                .map(category -> BeanUtils.toBean(category, CategoryRespVO.class))
                .collect(java.util.stream.Collectors.toList());
        return success(respList);
    }
} 