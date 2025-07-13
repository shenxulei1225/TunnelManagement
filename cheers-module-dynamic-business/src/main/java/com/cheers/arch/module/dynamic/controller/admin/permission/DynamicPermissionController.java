package com.cheers.arch.module.dynamic.controller.admin.permission;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.dynamic.controller.admin.permission.vo.*;
import com.cheers.arch.module.dynamic.convert.permission.DynamicPermissionConvert;
import com.cheers.arch.module.dynamic.dal.dataobject.permission.DynamicPermissionDO;
import com.cheers.arch.module.dynamic.service.permission.DynamicPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@Tag(name = "管理后台 - 动态业务权限")
@RestController
@RequestMapping("/dynamic/permission")
@Validated
public class DynamicPermissionController {

    @Resource
    private DynamicPermissionService dynamicPermissionService;

    @PostMapping("/create")
    @Operation(summary = "创建动态业务权限")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:create')")
    public CommonResult<Long> createDynamicPermission(@Valid @RequestBody DynamicPermissionCreateReqVO createReqVO) {
        DynamicPermissionDO permission = DynamicPermissionConvert.INSTANCE.convert(createReqVO);
        return CommonResult.success(dynamicPermissionService.createPermission(permission));
    }

    @PutMapping("/update")
    @Operation(summary = "更新动态业务权限")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:update')")
    public CommonResult<Boolean> updateDynamicPermission(@Valid @RequestBody DynamicPermissionUpdateReqVO updateReqVO) {
        DynamicPermissionDO permission = DynamicPermissionConvert.INSTANCE.convert(updateReqVO);
        dynamicPermissionService.updatePermission(permission);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除动态业务权限")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dynamic:permission:delete')")
    public CommonResult<Boolean> deleteDynamicPermission(@RequestParam("id") Long id) {
        dynamicPermissionService.deletePermission(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得动态业务权限")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:query')")
    public CommonResult<DynamicPermissionRespVO> getDynamicPermission(@RequestParam("id") Long id) {
        DynamicPermissionDO permission = dynamicPermissionService.getPermission(id);
        return CommonResult.success(DynamicPermissionConvert.INSTANCE.convert(permission));
    }

    @GetMapping("/list")
    @Operation(summary = "获得动态业务权限列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:query')")
    public CommonResult<List<DynamicPermissionRespVO>> getDynamicPermissionList(@RequestParam("ids") List<Long> ids) {
        List<DynamicPermissionDO> list = dynamicPermissionService.getPermissionList(null, null, null, null, null);
        return CommonResult.success(DynamicPermissionConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得动态业务权限分页")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:query')")
    public CommonResult<PageResult<DynamicPermissionRespVO>> getDynamicPermissionPage(@Valid DynamicPermissionPageReqVO pageVO) {
        PageResult<DynamicPermissionDO> pageResult = dynamicPermissionService.getPermissionPage(
                pageVO.getPageNo(), pageVO.getPageSize(), pageVO.getModelCode(), pageVO.getType(),
                pageVO.getTarget(), pageVO.getUserId(), pageVO.getRoleId(), pageVO.getDeptId());
        return CommonResult.success(DynamicPermissionConvert.INSTANCE.convertPage(pageResult));
    }

    @PostMapping("/check")
    @Operation(summary = "检查动态业务权限")
    @PreAuthorize("@ss.hasPermission('dynamic:permission:check')")
    public CommonResult<Boolean> checkDynamicPermission(@Valid @RequestBody DynamicPermissionCheckReqVO checkReqVO) {
        boolean hasPermission = dynamicPermissionService.hasPermission(
                checkReqVO.getModelCode(), checkReqVO.getType(), checkReqVO.getTarget(), checkReqVO.getLevel());
        return CommonResult.success(hasPermission);
    }

} 