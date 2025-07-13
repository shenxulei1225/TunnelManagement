package com.cheers.arch.module.dynamic.controller.admin.field;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.*;
import com.cheers.arch.module.dynamic.convert.field.DynamicFieldConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.arch.module.dynamic.service.field.FieldDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "管理后台 - 动态业务字段")
@RestController
@RequestMapping("/dynamic/field")
@Validated
public class DynamicFieldController {

    @Resource
    private FieldDefinitionService fieldDefinitionService;

    @PostMapping("/create")
    @Operation(summary = "创建动态业务字段")
    @PreAuthorize("@ss.hasPermission('dynamic:field:create')")
    public CommonResult<Long> createDynamicField(@Valid @RequestBody DynamicFieldCreateReqVO createReqVO) {
        FieldDefinitionDO field = DynamicFieldConvert.INSTANCE.convert(createReqVO);
        return CommonResult.success(fieldDefinitionService.createField(field));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态业务字段")
    @PreAuthorize("@ss.hasPermission('dynamic:field:update')")
    public CommonResult<Boolean> updateDynamicField(@Valid @RequestBody DynamicFieldUpdateReqVO updateReqVO) {
        FieldDefinitionDO field = DynamicFieldConvert.INSTANCE.convert(updateReqVO);
        fieldDefinitionService.updateField(field);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态业务字段")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dynamic:field:delete')")
    public CommonResult<Boolean> deleteDynamicField(@RequestParam("id") Long id) {
        fieldDefinitionService.deleteField(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态业务字段")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dynamic:field:query')")
    public CommonResult<DynamicFieldRespVO> getDynamicField(@RequestParam("id") Long id) {
        FieldDefinitionDO field = fieldDefinitionService.getField(id);
        return CommonResult.success(DynamicFieldConvert.INSTANCE.convert(field));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态业务字段列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('dynamic:field:query')")
    public CommonResult<List<DynamicFieldRespVO>> getDynamicFieldList(@RequestParam("ids") List<Long> ids) {
        List<FieldDefinitionDO> list = fieldDefinitionService.getFieldList(ids);
        return CommonResult.success(DynamicFieldConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态业务字段分页")
    @PreAuthorize("@ss.hasPermission('dynamic:field:query')")
    public CommonResult<PageResult<DynamicFieldRespVO>> getDynamicFieldPage(@Valid DynamicFieldPageReqVO pageVO) {
        PageResult<FieldDefinitionDO> pageResult = fieldDefinitionService.getFieldPage(
                pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getName(), pageVO.getCode(),
                pageVO.getType(), pageVO.getModelId());
        return CommonResult.success(DynamicFieldConvert.INSTANCE.convertPage(pageResult));
    }
} 