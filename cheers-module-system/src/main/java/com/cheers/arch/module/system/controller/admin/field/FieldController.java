package com.cheers.arch.module.system.controller.admin.field;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.excel.core.util.ExcelUtils;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldExportReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldHierarchyRelRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateHierarchiesReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateHierarchyReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefHierarchyRelDO;
import com.cheers.arch.module.system.service.field.FieldDefHierarchyRelService;
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

@Tag(name = "管理后台 - 字段")
@RestController
@RequestMapping("/system/field")
@Validated
public class FieldController {

    @Resource
    private FieldService fieldService;
    
    @Resource
    private FieldDefHierarchyRelService fieldDefHierarchyRelService;

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
    @Parameter(name = "id", description = "编号", required = true)
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

    @GetMapping("/list")
    @Operation(summary = "获得字段列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<List<FieldRespVO>> getFieldList(@RequestParam("ids") List<Long> ids) {
        List<FieldDO> list = fieldService.getFieldList(ids);
        List<FieldRespVO> respList = list.stream()
                .map(field -> BeanUtils.toBean(field, FieldRespVO.class))
                .collect(java.util.stream.Collectors.toList());
        return CommonResult.success(respList);
    }

    @GetMapping("/page")
    @Operation(summary = "获得字段分页")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<PageResult<FieldRespVO>> getFieldPage(@Valid FieldPageReqVO pageVO) {
        PageResult<FieldDO> pageResult = fieldService.getFieldPage(pageVO);
        PageResult<FieldRespVO> respPageResult = new PageResult<>();
        respPageResult.setList(pageResult.getList().stream()
                .map(field -> BeanUtils.toBean(field, FieldRespVO.class))
                .collect(java.util.stream.Collectors.toList()));
        respPageResult.setTotal(pageResult.getTotal());
        return CommonResult.success(respPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出字段 Excel")
    @PreAuthorize("@ss.hasPermission('system:field:export')")
    public void exportFieldExcel(@Valid FieldExportReqVO exportReqVO,
            HttpServletResponse response) throws IOException {
        List<FieldDO> list = fieldService.getFieldList(exportReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "字段.xls", "数据", FieldDO.class, list);
    }

    @PutMapping("/update-hierarchy-group")
    @Operation(summary = "更新字段分级组")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<Boolean> updateFieldHierarchyGroup(@Valid @RequestBody FieldUpdateHierarchyReqVO reqVO) {
        fieldDefHierarchyRelService.updateFieldDefHierarchyRel(reqVO.getFieldId(), reqVO.getHierarchyGroupId());
        return CommonResult.success(true);
    }

    @PutMapping("/update-hierarchy-groups")
    @Operation(summary = "批量更新字段分级组")
    @PreAuthorize("@ss.hasPermission('system:field:update')")
    public CommonResult<Boolean> updateFieldHierarchyGroups(@Valid @RequestBody FieldUpdateHierarchiesReqVO reqVO) {
        fieldDefHierarchyRelService.updateFieldDefHierarchyRels(reqVO.getFieldId(), reqVO.getHierarchyGroupIds());
        return CommonResult.success(true);
    }

    @GetMapping("/hierarchy-rels")
    @Operation(summary = "获取字段分级组关联信息")
    @PreAuthorize("@ss.hasPermission('system:field:query')")
    public CommonResult<List<FieldHierarchyRelRespVO>> getFieldHierarchyRels(@RequestParam("fieldIds") String fieldIdsStr) {
        List<Long> fieldIds = java.util.Arrays.stream(fieldIdsStr.split(","))
                .map(Long::valueOf)
                .toList();
        List<FieldDefHierarchyRelDO> rels = fieldDefHierarchyRelService.getFieldDefHierarchyRelsByFieldDefIds(fieldIds);
        return CommonResult.success(BeanUtils.toBean(rels, FieldHierarchyRelRespVO.class));
    }

} 