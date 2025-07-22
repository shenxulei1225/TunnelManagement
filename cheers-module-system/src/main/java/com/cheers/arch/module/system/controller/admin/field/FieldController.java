package com.cheers.arch.module.system.controller.admin.field;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldBatchUpdateHierarchyReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldHierarchyRelRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateHierarchyReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldHierarchyRelDO;
import com.cheers.arch.module.system.service.field.FieldHierarchyRelService;
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
import lombok.extern.slf4j.Slf4j;

@Tag(name = "管理后台 - 字段定义")
@RestController
@RequestMapping("/system/field")
@Validated
@Slf4j
public class FieldController {

    @Resource
    private FieldService fieldService;
    
    @Resource
    private FieldHierarchyRelService fieldHierarchyRelService;

    @PostMapping("/create")
    @Operation(summary = "创建字段")
    @PreAuthorize("@ss.hasPermission('system:field:create')")
    public CommonResult<Long> createField(@Valid @RequestBody FieldCreateReqVO createReqVO) {
        return CommonResult.success(fieldService.createField(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新字段")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<Boolean> updateField(@Valid @RequestBody FieldUpdateReqVO updateReqVO) {
        fieldService.updateField(updateReqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字段")
    @PreAuthorize("@ss.hasPermission('system:field:delete')")
    public CommonResult<Boolean> deleteField(@RequestParam("id") Long id) {
        fieldService.deleteField(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得字段")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<FieldRespVO> getField(@RequestParam("id") Long id) {
        FieldDO field = fieldService.getField(id);
        return CommonResult.success(BeanUtils.toBean(field, FieldRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得字段分页")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<PageResult<FieldRespVO>> getFieldPage(@Valid FieldPageReqVO pageReqVO) {
        PageResult<FieldDO> pageResult = fieldService.getFieldPage(pageReqVO);
        return CommonResult.success(BeanUtils.toBean(pageResult, FieldRespVO.class));
    }

    @GetMapping("/list-by-category")
    @Operation(summary = "获取指定分类下的字段列表")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<List<FieldRespVO>> getFieldsByCategory(@RequestParam("categoryId") Long categoryId) {
        List<FieldDO> fields = fieldService.getFieldsByCategory(categoryId);
        return CommonResult.success(BeanUtils.toBean(fields, FieldRespVO.class));
    }

    @GetMapping("/hierarchy-rels")
    @Operation(summary = "获取字段分组关联信息")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<List<FieldHierarchyRelRespVO>> getFieldHierarchyRels(@RequestParam("fieldIds") String fieldIdsStr) {
        List<Long> fieldIds = java.util.Arrays.stream(fieldIdsStr.split(","))
                .map(Long::valueOf)
                .toList();
        List<FieldHierarchyRelDO> rels = fieldHierarchyRelService.getFieldHierarchyRels(fieldIds);
        return CommonResult.success(BeanUtils.toBean(rels, FieldHierarchyRelRespVO.class));
    }

    @PutMapping("/link-hierarchy-group")
    @Operation(summary = "关联字段到分级组")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<String> linkFieldHierarchyGroup(@Valid @RequestBody FieldUpdateHierarchyReqVO reqVO) {
        // 检查是否已存在关联
        List<Long> currentGroupIds = fieldHierarchyRelService.getHierarchyGroupIdsByFieldId(reqVO.getFieldId());
        
        if (currentGroupIds.contains(reqVO.getHierarchyGroupId())) {
            // 已存在关联，返回友好提示
            return CommonResult.success("字段已在此分组中");
        }
        // 创建新的关联
        fieldHierarchyRelService.createFieldHierarchyRel(reqVO.getFieldId(), reqVO.getHierarchyGroupId());
        return CommonResult.success("关联成功");
    }

    @PutMapping("/update-hierarchy-group")
    @Operation(summary = "更新字段的分级组关联")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<String> updateFieldHierarchy(@Valid @RequestBody FieldUpdateHierarchyReqVO reqVO) {
        // 业务逻辑：检查字段当前分组情况，决定是替换还是添加
        List<Long> currentGroupIds = fieldHierarchyRelService.getHierarchyGroupIdsByFieldId(reqVO.getFieldId());
        
        if (reqVO.getHierarchyGroupId() == null) {
            // 移除所有分组关联
            fieldHierarchyRelService.deleteFieldHierarchyRelsByFieldId(reqVO.getFieldId());
            return CommonResult.success("分组关联已移除");
        } else if (currentGroupIds.contains(reqVO.getHierarchyGroupId())) {
            // 字段已在目标分组中
            return CommonResult.success("字段已在此分组中");
        } else if (currentGroupIds.size() <= 1) {
            // 只有0个或1个分组 → 替换
            fieldHierarchyRelService.replaceFieldHierarchyRels(reqVO.getFieldId(), 
                    List.of(reqVO.getHierarchyGroupId()));
            return CommonResult.success("分组更新成功");
        } else {
            // 多个分组 → 添加新关联
            fieldHierarchyRelService.createFieldHierarchyRel(reqVO.getFieldId(), reqVO.getHierarchyGroupId());
            return CommonResult.success("分组关联已添加");
        }
    }

    @DeleteMapping("/unlink-hierarchy-group")
    @Operation(summary = "解除字段与分级组的关联")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<Boolean> unlinkFieldHierarchyGroup(
            @Parameter(description = "字段编号", required = true) @RequestParam("fieldId") Long fieldId,
            @Parameter(description = "分级组编号", required = true) @RequestParam("hierarchyGroupId") Long hierarchyGroupId) {
        fieldHierarchyRelService.deleteFieldHierarchyRel(fieldId, hierarchyGroupId);
        return CommonResult.success(true);
    }

    @PutMapping("/batch-update-hierarchy-group")
    @Operation(summary = "批量更新字段的分级组关联")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<Boolean> batchUpdateFieldHierarchyGroup(@Valid @RequestBody FieldBatchUpdateHierarchyReqVO reqVO) {
        fieldHierarchyRelService.replaceFieldHierarchyRels(reqVO.getFieldId(), reqVO.getHierarchyGroupIds());
        return CommonResult.success(true);
    }

} 