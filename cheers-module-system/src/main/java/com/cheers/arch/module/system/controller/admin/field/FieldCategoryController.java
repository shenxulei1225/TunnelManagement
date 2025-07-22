package com.cheers.arch.module.system.controller.admin.field;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelBatchCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelUpdateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryUpdateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldRespVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldCategoryRelDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.service.field.FieldCategoryRelService;
import com.cheers.arch.module.system.service.field.FieldService;

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

@Tag(name = "管理后台 - 字段分类关联管理")
@RestController
@RequestMapping("/system/field-category")
@Validated
public class FieldCategoryController {

    @Resource
    private FieldCategoryRelService fieldCategoryRelService;
    
    @Resource
    private FieldService fieldService;

    @PostMapping("/create")
    @Operation(summary = "创建字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:create')")
    public CommonResult<Long> createFieldCategoryRel(@Valid @RequestBody FieldCategoryRelCreateReqVO createReqVO) {
        return success(fieldCategoryRelService.createFieldCategoryRel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> updateFieldCategoryRel(@Valid @RequestBody FieldCategoryRelUpdateReqVO updateReqVO) {
        fieldCategoryRelService.updateFieldCategoryRel(updateReqVO);
        return success(true);
    }

    @PutMapping("/update-sort")
    @Operation(summary = "更新字段分类关联的排序")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> updateFieldCategoryRelSort(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("sort") Integer sort) {
        fieldCategoryRelService.updateFieldCategoryRelSort(fieldId, categoryId, sort);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字段分类关联")
    @Parameter(name = "id", description = "关联ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> deleteFieldCategoryRel(@RequestParam("id") Long id) {
        fieldCategoryRelService.deleteFieldCategoryRel(id);
        return success(true);
    }

    @DeleteMapping("/delete-by-field-category")
    @Operation(summary = "删除字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> deleteFieldCategoryRel(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("categoryId") Long categoryId) {
        fieldCategoryRelService.deleteFieldCategoryRel(fieldId, categoryId);
        return success(true);
    }

    @DeleteMapping("/delete-by-field")
    @Operation(summary = "删除字段的所有分类关联")
    @Parameter(name = "fieldId", description = "字段ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> deleteFieldCategoryRelsByFieldId(@RequestParam("fieldId") Long fieldId) {
        fieldCategoryRelService.deleteFieldCategoryRelsByFieldId(fieldId);
        return success(true);
    }

    @DeleteMapping("/delete-by-category")
    @Operation(summary = "删除分类的所有字段关联")
    @Parameter(name = "categoryId", description = "分类ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> deleteFieldCategoryRelsByCategoryId(@RequestParam("categoryId") Long categoryId) {
        fieldCategoryRelService.deleteFieldCategoryRelsByCategoryId(categoryId);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得字段分类关联详情")
    @Parameter(name = "id", description = "关联ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<FieldCategoryRelRespVO> getFieldCategoryRel(@RequestParam("id") Long id) {
        FieldCategoryRelDO rel = fieldCategoryRelService.getFieldCategoryRel(id);
        return success(BeanUtils.toBean(rel, FieldCategoryRelRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<PageResult<FieldCategoryRelRespVO>> getFieldCategoryRelPage(@Valid FieldCategoryRelPageReqVO pageReqVO) {
        PageResult<FieldCategoryRelDO> pageResult = fieldCategoryRelService.getFieldCategoryRelPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FieldCategoryRelRespVO.class));
    }

    @GetMapping("/exists")
    @Operation(summary = "检查字段分类关联是否存在")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<Boolean> existsFieldCategoryRel(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("categoryId") Long categoryId) {
        boolean exists = fieldCategoryRelService.existsFieldCategoryRel(fieldId, categoryId);
        return success(exists);
    }

    @GetMapping("/fields-by-category")
    @Operation(summary = "根据分类ID获取字段列表")
    @Parameter(name = "categoryId", description = "分类ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<List<FieldRespVO>> getFieldsByCategoryId(@RequestParam("categoryId") Long categoryId) {
        List<FieldDO> fields = fieldCategoryRelService.getFieldsByCategoryId(categoryId);
        return success(BeanUtils.toBean(fields, FieldRespVO.class));
    }

    @GetMapping("/categories-by-field")
    @Operation(summary = "根据字段ID获取分类ID列表")
    @Parameter(name = "fieldId", description = "字段ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<List<Long>> getCategoryIdsByFieldId(@RequestParam("fieldId") Long fieldId) {
        List<Long> categoryIds = fieldCategoryRelService.getCategoryIdsByFieldId(fieldId);
        return success(categoryIds);
    }

    @GetMapping("/relations")
    @Operation(summary = "获取字段分类关联记录")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<List<FieldCategoryRelRespVO>> getFieldCategoryRels(
            @RequestParam(value = "fieldIds", required = false) List<Long> fieldIds) {
        List<FieldCategoryRelDO> relations = fieldCategoryRelService.getFieldCategoryRels(fieldIds);
        return success(BeanUtils.toBean(relations, FieldCategoryRelRespVO.class));
    }

    @GetMapping("/sort-map")
    @Operation(summary = "获取分类中字段的排序映射")
    @Parameter(name = "categoryId", description = "分类ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<Map<Long, Integer>> getFieldSortMapInCategory(@RequestParam("categoryId") Long categoryId) {
        Map<Long, Integer> sortMap = fieldCategoryRelService.getFieldSortMapInCategory(categoryId);
        return success(sortMap);
    }

    @PostMapping("/batch-create")
    @Operation(summary = "批量创建字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:create')")
    public CommonResult<Boolean> batchCreateFieldCategoryRels(@Valid @RequestBody FieldCategoryRelBatchCreateReqVO reqVO) {
        if (reqVO.getCategoryIdSortMap() != null && !reqVO.getCategoryIdSortMap().isEmpty()) {
            fieldCategoryRelService.batchCreateFieldCategoryRels(reqVO.getFieldId(), reqVO.getCategoryIdSortMap());
        } else {
            fieldCategoryRelService.batchCreateFieldCategoryRels(reqVO.getFieldId(), reqVO.getCategoryIds());
        }
        return success(true);
    }

    @PostMapping("/batch-delete")
    @Operation(summary = "批量删除字段分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> batchDeleteFieldCategoryRels(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("categoryIds") List<Long> categoryIds) {
        // 逐个删除关联
        for (Long categoryId : categoryIds) {
            fieldCategoryRelService.deleteFieldCategoryRel(fieldId, categoryId);
        }
        return success(true);
    }

    @PostMapping("/replace")
    @Operation(summary = "替换字段的所有分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> replaceFieldCategoryRels(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam("categoryIds") List<Long> categoryIds) {
        fieldCategoryRelService.replaceFieldCategoryRels(fieldId, categoryIds);
        return success(true);
    }

    @PostMapping("/replace-with-sort")
    @Operation(summary = "替换字段的所有分类关联（带排序）")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> replaceFieldCategoryRelsWithSort(
            @RequestParam("fieldId") Long fieldId,
            @RequestBody Map<Long, Integer> categoryIdSortMap) {
        fieldCategoryRelService.replaceFieldCategoryRels(fieldId, categoryIdSortMap);
        return success(true);
    }

    @PostMapping("/batch-update-sort")
    @Operation(summary = "批量更新字段在分类中的排序")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> batchUpdateFieldSortInCategory(
            @RequestParam("categoryId") Long categoryId,
            @RequestBody Map<Long, Integer> fieldIdSortMap) {
        fieldCategoryRelService.batchUpdateFieldSortInCategory(categoryId, fieldIdSortMap);
        return success(true);
    }

    @GetMapping("/count")
    @Operation(summary = "统计分类下的字段数量")
    @Parameter(name = "categoryId", description = "分类ID", required = true)
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<Long> countFieldsByCategoryId(@RequestParam("categoryId") Long categoryId) {
        List<FieldDO> fields = fieldCategoryRelService.getFieldsByCategoryId(categoryId);
        return success((long) fields.size());
    }

    @GetMapping("/batch-count")
    @Operation(summary = "批量统计多个分类的字段数量")
    @PreAuthorize("@ss.hasPermission('system:field-category:query')")
    public CommonResult<Map<Long, Long>> countFieldsByCategoryIds(@RequestParam("categoryIds") List<Long> categoryIds) {
        Map<Long, Long> countMap = new HashMap<>();
        for (Long categoryId : categoryIds) {
            List<FieldDO> fields = fieldCategoryRelService.getFieldsByCategoryId(categoryId);
            countMap.put(categoryId, (long) fields.size());
        }
        return success(countMap);
    }

    // ==================== 业务操作接口 ====================

    @PostMapping("/link-category")
    @Operation(summary = "关联字段到分类")
    @PreAuthorize("@ss.hasPermission('system:field-category:create')")
    public CommonResult<Boolean> linkFieldCategory(@Valid @RequestBody FieldCategoryUpdateReqVO reqVO) {
        fieldCategoryRelService.createFieldCategoryRel(reqVO.getFieldId(), reqVO.getCategoryId(), reqVO.getSort());
        return success(true);
    }

    @PutMapping("/update-category")
    @Operation(summary = "更新字段的分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> updateFieldCategory(@Valid @RequestBody FieldCategoryUpdateReqVO reqVO) {
        // 业务逻辑：检查字段当前分类情况，决定是替换还是添加
        List<Long> currentCategoryIds = fieldCategoryRelService.getCategoryIdsByFieldId(reqVO.getFieldId());
        
        if (reqVO.getCategoryId() == null) {
            // 这种情况应该使用专门的删除接口，不在这里处理
            throw new IllegalArgumentException("分类ID不能为空，如需删除请使用专门的删除接口");
        } else if (currentCategoryIds.size() <= 1) {
            // 只有0个或1个分类 → 替换
            fieldCategoryRelService.replaceFieldCategoryRels(reqVO.getFieldId(), 
                    List.of(reqVO.getCategoryId()));
        } else {
            // 多个分类 → 添加新关联
            fieldCategoryRelService.createFieldCategoryRel(reqVO.getFieldId(), reqVO.getCategoryId(), reqVO.getSort());
        }
        
        return success(true);
    }

    @DeleteMapping("/unlink-category")
    @Operation(summary = "解除字段与分类的关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> unlinkFieldCategory(
            @Parameter(description = "字段ID", required = true) @RequestParam("fieldId") Long fieldId,
            @Parameter(description = "分类ID", required = true) @RequestParam("categoryId") Long categoryId) {
        fieldCategoryRelService.deleteFieldCategoryRel(fieldId, categoryId);
        return success(true);
    }

    @DeleteMapping("/unlink-all-categories")
    @Operation(summary = "解除字段与所有分类的关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:delete')")
    public CommonResult<Boolean> unlinkAllFieldCategories(
            @Parameter(description = "字段ID", required = true) @RequestParam("fieldId") Long fieldId) {
        fieldCategoryRelService.deleteFieldCategoryRelsByFieldId(fieldId);
        return success(true);
    }

    @PutMapping("/batch-update-category")
    @Operation(summary = "批量更新字段的分类关联")
    @PreAuthorize("@ss.hasPermission('system:field-category:update')")
    public CommonResult<Boolean> batchUpdateFieldCategory(@Valid @RequestBody FieldCategoryRelBatchCreateReqVO reqVO) {
        if (reqVO.getCategoryIdSortMap() != null && !reqVO.getCategoryIdSortMap().isEmpty()) {
            fieldCategoryRelService.replaceFieldCategoryRels(reqVO.getFieldId(), reqVO.getCategoryIdSortMap());
        } else {
            fieldCategoryRelService.replaceFieldCategoryRels(reqVO.getFieldId(), reqVO.getCategoryIds());
        }
        return success(true);
    }

} 