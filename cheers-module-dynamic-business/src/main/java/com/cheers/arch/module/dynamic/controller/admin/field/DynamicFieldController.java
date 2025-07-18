package com.cheers.arch.module.dynamic.controller.admin.field;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldCreateReqVO;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldRespVO;
import com.cheers.arch.module.dynamic.controller.admin.field.vo.DynamicFieldUpdateReqVO;
import com.cheers.arch.module.dynamic.convert.field.DynamicFieldConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;
import com.cheers.arch.module.dynamic.service.field.DynamicFieldDefinitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 动态字段定义")
@RestController
@RequestMapping("/dynamic-business/field")
@Validated
public class DynamicFieldController {

    @Resource
    private DynamicFieldDefinitionService dynamicFieldDefinitionService;

    @PostMapping("/create")
    @Operation(summary = "创建动态字段定义")
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:create')")
    public CommonResult<Long> createField(@Valid @RequestBody DynamicFieldCreateReqVO createReqVO) {
        DynamicFieldDefinitionDO field = DynamicFieldConvert.INSTANCE.convert(createReqVO);
        return success(dynamicFieldDefinitionService.createField(field));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态字段定义")
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:update')")
    public CommonResult<Boolean> updateField(@Valid @RequestBody DynamicFieldUpdateReqVO updateReqVO) {
        DynamicFieldDefinitionDO field = DynamicFieldConvert.INSTANCE.convert(updateReqVO);
        dynamicFieldDefinitionService.updateField(field);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态字段定义")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:delete')")
    public CommonResult<Boolean> deleteField(@RequestParam("id") Long id) {
        dynamicFieldDefinitionService.deleteField(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态字段定义")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:query')")
    public CommonResult<DynamicFieldRespVO> getField(@RequestParam("id") Long id) {
        DynamicFieldDefinitionDO field = dynamicFieldDefinitionService.getField(id);
        return success(DynamicFieldConvert.INSTANCE.convert(field));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态字段定义列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "[1024, 2048]")
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:query')")
    public CommonResult<List<DynamicFieldRespVO>> getFieldList(@RequestParam("ids") List<Long> ids) {
        List<DynamicFieldDefinitionDO> list = dynamicFieldDefinitionService.getFieldList(ids);
        return success(DynamicFieldConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态字段定义分页")
    @PreAuthorize("@ss.hasPermission('dynamic-business:field:query')")
    public CommonResult<PageResult<DynamicFieldRespVO>> getFieldPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "name", required = false) String name,
                                                                    @RequestParam(value = "code", required = false) String code,
                                                                    @RequestParam(value = "type", required = false) String type,
                                                                    @RequestParam(value = "modelId", required = false) Long modelId) {
        PageResult<DynamicFieldDefinitionDO> pageResult = dynamicFieldDefinitionService.getFieldPage(
                pageNo, pageSize, name, code, type, modelId);
        return success(DynamicFieldConvert.INSTANCE.convertPage(pageResult));
    }
} 