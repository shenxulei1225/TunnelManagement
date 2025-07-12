package com.cheers.arch.module.dynamic.controller.permission;

import com.cheers.arch.common.pojo.CommonResult;
import com.cheers.arch.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.permission.vo.*;
import com.cheers.arch.module.dynamic.convert.permission.DynamicPermissionConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;
import com.cheers.arch.module.dynamic.service.permission.DynamicPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.Collection;
import java.util.List;

import static com.cheers.arch.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 动态权限")
@RestController
@RequestMapping("/dynamic/permission")
public class DynamicPermissionController {

    @Resource
    private DynamicPermissionService dynamicPermissionService;

    @PostMapping("/create")
    @Operation(summary = "创建动态权限")
    public CommonResult<Long> createDynamicPermission(@Valid @RequestBody DynamicPermissionCreateReqVO createReqVO) {
        return success(dynamicPermissionService.createDynamicPermission(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态权限")
    public CommonResult<Boolean> updateDynamicPermission(@Valid @RequestBody DynamicPermissionUpdateReqVO updateReqVO) {
        dynamicPermissionService.updateDynamicPermission(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态权限")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDynamicPermission(@RequestParam("id") Long id) {
        dynamicPermissionService.deleteDynamicPermission(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态权限")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DynamicPermissionRespVO> getDynamicPermission(@RequestParam("id") Long id) {
        DynamicPermissionDO dynamicPermission = dynamicPermissionService.getDynamicPermission(id);
        return success(DynamicPermissionConvert.INSTANCE.convert(dynamicPermission));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态权限列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<DynamicPermissionRespVO>> getDynamicPermissionList(@RequestParam("ids") Collection<Long> ids) {
        List<DynamicPermissionDO> list = dynamicPermissionService.getDynamicPermissionList(ids);
        return success(DynamicPermissionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态权限分页")
    public CommonResult<PageResult<DynamicPermissionRespVO>> getDynamicPermissionPage(@Valid DynamicPermissionPageReqVO pageVO) {
        PageResult<DynamicPermissionDO> pageResult = dynamicPermissionService.getDynamicPermissionPage(pageVO);
        return success(DynamicPermissionConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/check")
    @Operation(summary = "检查动态权限")
    public CommonResult<Boolean> checkDynamicPermission(@Valid @RequestBody DynamicPermissionCheckReqVO checkReqVO) {
        return success(dynamicPermissionService.checkDynamicPermission(checkReqVO));
    }

} 