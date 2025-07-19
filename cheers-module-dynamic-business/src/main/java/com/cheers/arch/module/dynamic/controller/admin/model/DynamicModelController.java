package com.cheers.arch.module.dynamic.controller.admin.model;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import com.cheers.arch.module.system.service.directory.DirectoryService;
import com.cheers.arch.module.dynamic.controller.admin.model.vo.*;
import com.cheers.arch.module.dynamic.convert.model.DynamicModelConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;
import com.cheers.arch.module.dynamic.service.model.DynamicBusinessModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 动态业务模型")
@RestController
@RequestMapping("/dynamic-business/model")
@Validated
public class DynamicModelController {

    @Resource
    private DynamicBusinessModelService dynamicBusinessModelService;

    @Resource
    private DirectoryService directoryService;

    @PostMapping("/create")
    @Operation(summary = "创建动态业务模型")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:create')")
    public CommonResult<Long> createModel(@Valid @RequestBody DynamicModelCreateReqVO createReqVO) {
        DynamicBusinessModelDO model = DynamicModelConvert.INSTANCE.convert(createReqVO);
        return success(dynamicBusinessModelService.createModel(model));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态业务模型")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:update')")
    public CommonResult<Boolean> updateModel(@Valid @RequestBody DynamicModelUpdateReqVO updateReqVO) {
        DynamicBusinessModelDO model = DynamicModelConvert.INSTANCE.convert(updateReqVO);
        dynamicBusinessModelService.updateModel(model);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态业务模型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:delete')")
    public CommonResult<Boolean> deleteModel(@RequestParam("id") Long id) {
        dynamicBusinessModelService.deleteModel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态业务模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:query')")
    public CommonResult<DynamicModelRespVO> getModel(@RequestParam("id") Long id) {
        DynamicBusinessModelDO model = dynamicBusinessModelService.getModel(id);
        return success(DynamicModelConvert.INSTANCE.convert(model));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态业务模型列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "[1024, 2048]")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:query')")
    public CommonResult<List<DynamicModelRespVO>> getModelList(@RequestParam("ids") List<Long> ids) {
        List<DynamicBusinessModelDO> list = dynamicBusinessModelService.getModelList(ids);
        return success(DynamicModelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态业务模型分页")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:query')")
    public CommonResult<PageResult<DynamicModelRespVO>> getModelPage(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                    @RequestParam(value = "name", required = false) String name,
                                                                    @RequestParam(value = "status", required = false) Integer status) {
        PageResult<DynamicBusinessModelDO> pageResult = dynamicBusinessModelService.getModelPage(
                pageNo, pageSize, name, status);
        return success(DynamicModelConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/page-by-directory")
    @Operation(summary = "获得动态业务模型分页（按目录）")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:query')")
    public CommonResult<PageResult<DynamicModelRespVO>> getModelPageByDirectory(@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                                                               @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                                                               @RequestParam(value = "name", required = false) String name,
                                                                               @RequestParam(value = "status", required = false) Integer status,
                                                                               @RequestParam(value = "directoryId", required = false) Long directoryId) {
        PageResult<DynamicBusinessModelDO> pageResult = dynamicBusinessModelService.getModelPageByDirectory(
                pageNo, pageSize, name, status, directoryId);
        return success(DynamicModelConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/batch-update-sort")
    @Operation(summary = "批量更新动态业务模型排序")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:update')")
    public CommonResult<Boolean> batchUpdateSort(@RequestBody List<Map<String, Object>> sortList) {
        dynamicBusinessModelService.batchUpdateSort(sortList);
        return success(true);
    }

    @GetMapping("/directory-tree")
    @Operation(summary = "获得动态业务模型目录树")
    @PreAuthorize("@ss.hasPermission('dynamic-business:model:query')")
    public CommonResult<List<DirectoryDO>> getDirectoryTree() {
        return CommonResult.success(directoryService.getDirectoryTree("dynamic_model"));
    }
} 