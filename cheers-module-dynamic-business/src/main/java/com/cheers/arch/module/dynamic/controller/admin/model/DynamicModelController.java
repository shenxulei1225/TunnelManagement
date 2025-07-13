package com.cheers.arch.module.dynamic.controller.admin.model;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.directory.constants.DirectoryConstants;
import com.cheers.arch.framework.directory.service.DirectoryService;
import com.cheers.arch.module.dynamic.controller.admin.model.vo.*;
import com.cheers.arch.module.dynamic.convert.model.DynamicModelConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.model.BusinessModelDO;
import com.cheers.arch.module.dynamic.service.model.BusinessModelService;
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

@Tag(name = "管理后台 - 动态业务模型")
@RestController
@RequestMapping("/dynamic/model")
@Validated
public class DynamicModelController {

    @Resource
    private BusinessModelService businessModelService;

    @Resource
    private DirectoryService directoryService;

    @PostMapping("/create")
    @Operation(summary = "创建动态业务模型")
    @PreAuthorize("@ss.hasPermission('dynamic:model:create')")
    public CommonResult<Long> createDynamicModel(@Valid @RequestBody DynamicModelCreateReqVO createReqVO) {
        BusinessModelDO model = DynamicModelConvert.INSTANCE.convert(createReqVO);
        return CommonResult.success(businessModelService.createModel(model));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态业务模型")
    @PreAuthorize("@ss.hasPermission('dynamic:model:update')")
    public CommonResult<Boolean> updateDynamicModel(@Valid @RequestBody DynamicModelUpdateReqVO updateReqVO) {
        BusinessModelDO model = DynamicModelConvert.INSTANCE.convert(updateReqVO);
        businessModelService.updateModel(model);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态业务模型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dynamic:model:delete')")
    public CommonResult<Boolean> deleteDynamicModel(@RequestParam("id") Long id) {
        businessModelService.deleteModel(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态业务模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dynamic:model:query')")
    public CommonResult<DynamicModelRespVO> getDynamicModel(@RequestParam("id") Long id) {
        BusinessModelDO model = businessModelService.getModel(id);
        return CommonResult.success(DynamicModelConvert.INSTANCE.convert(model));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态业务模型列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('dynamic:model:query')")
    public CommonResult<List<DynamicModelRespVO>> getDynamicModelList(@RequestParam("ids") List<Long> ids) {
        List<BusinessModelDO> list = businessModelService.getModelList(ids);
        return CommonResult.success(DynamicModelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态业务模型分页")
    @PreAuthorize("@ss.hasPermission('dynamic:model:query')")
    public CommonResult<PageResult<DynamicModelRespVO>> getDynamicModelPage(@Valid DynamicModelPageReqVO pageVO) {
        PageResult<BusinessModelDO> pageResult = businessModelService.getModelPage(
                pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getName(), pageVO.getStatus());
        return CommonResult.success(DynamicModelConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/page-by-directory")
    @Operation(summary = "获得动态业务模型分页（按目录）")
    @PreAuthorize("@ss.hasPermission('dynamic:model:query')")
    public CommonResult<PageResult<DynamicModelRespVO>> getDynamicModelPageByDirectory(@Valid DynamicModelPageReqVO pageVO) {
        PageResult<BusinessModelDO> pageResult = businessModelService.getModelPageByDirectory(
                pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getName(), pageVO.getStatus(), pageVO.getDirectoryId());
        return CommonResult.success(DynamicModelConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/batch-update-sort")
    @Operation(summary = "批量更新动态业务模型排序")
    @PreAuthorize("@ss.hasPermission('dynamic:model:update')")
    public CommonResult<Boolean> batchUpdateSort(@RequestBody List<Map<String, Object>> sortList) {
        businessModelService.batchUpdateSort(sortList);
        return CommonResult.success(true);
    }

    @GetMapping("/directory-tree")
    @Operation(summary = "获得动态业务模型目录树")
    @PreAuthorize("@ss.hasPermission('dynamic:model:query')")
    public CommonResult<List<com.cheers.arch.framework.directory.dal.dataobject.DirectoryDO>> getDirectoryTree() {
        return CommonResult.success(directoryService.getDirectoryTree(DirectoryConstants.BusinessType.DYNAMIC_MODEL));
    }
} 