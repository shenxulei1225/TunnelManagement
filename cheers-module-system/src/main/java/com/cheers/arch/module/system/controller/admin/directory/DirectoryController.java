package com.cheers.arch.module.system.controller.admin.directory;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.dal.dataobject.directory.DirectoryDO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryRespVO;
import com.cheers.arch.module.system.controller.admin.directory.vo.DirectoryUpdateReqVO;
import com.cheers.arch.module.system.convert.directory.DirectoryConvert;
import com.cheers.arch.module.system.service.directory.DirectoryService;

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

@Tag(name = "管理后台 - 通用目录")
@RestController
@RequestMapping("/directory")
@Validated
public class DirectoryController {

    @Resource
    private DirectoryService directoryService;

    @PostMapping("/create")
    @Operation(summary = "创建目录")
    @PreAuthorize("@ss.hasPermission('directory:create')")
    public CommonResult<Long> createDirectory(@Valid @RequestBody DirectoryCreateReqVO createReqVO) {
        return CommonResult.success(directoryService.createDirectory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新目录")
    @PreAuthorize("@ss.hasPermission('directory:update')")
    public CommonResult<Boolean> updateDirectory(@Valid @RequestBody DirectoryUpdateReqVO updateReqVO) {
        directoryService.updateDirectory(updateReqVO);
        return CommonResult.success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除目录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('directory:delete')")
    public CommonResult<Boolean> deleteDirectory(@RequestParam("id") Long id) {
        directoryService.deleteDirectory(id);
        return CommonResult.success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得目录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('directory:query')")
    public CommonResult<DirectoryRespVO> getDirectory(@RequestParam("id") Long id) {
        DirectoryDO directory = directoryService.getDirectory(id);
        return CommonResult.success(DirectoryConvert.INSTANCE.convert(directory));
    }

    @GetMapping("/list")
    @Operation(summary = "获得目录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('directory:query')")
    public CommonResult<List<DirectoryRespVO>> getDirectoryList(@RequestParam("ids") List<Long> ids) {
        List<DirectoryDO> list = directoryService.getDirectoryList(ids);
        return CommonResult.success(DirectoryConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/tree")
    @Operation(summary = "获得目录树")
    @Parameter(name = "businessType", description = "业务类型", required = true, example = "dynamic_model")
    @PreAuthorize("@ss.hasPermission('directory:query')")
    public CommonResult<List<DirectoryRespVO>> getDirectoryTree(@RequestParam("businessType") String businessType) {
        List<DirectoryDO> tree = directoryService.getDirectoryTree(businessType);
        return CommonResult.success(DirectoryConvert.INSTANCE.convertList(tree));
    }

    @GetMapping("/children")
    @Operation(summary = "获得子目录列表")
    @Parameter(name = "businessType", description = "业务类型", required = true, example = "dynamic_model")
    @Parameter(name = "parentId", description = "父目录ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('directory:query')")
    public CommonResult<List<DirectoryRespVO>> getChildDirectories(@RequestParam("businessType") String businessType, @RequestParam("parentId") Long parentId) {
        List<DirectoryDO> children = directoryService.getChildDirectories(businessType, parentId);
        return CommonResult.success(DirectoryConvert.INSTANCE.convertList(children));
    }

    @PutMapping("/move")
    @Operation(summary = "移动目录")
    @PreAuthorize("@ss.hasPermission('directory:update')")
    public CommonResult<Boolean> moveDirectory(@RequestParam("id") Long id, @RequestParam("newParentId") Long newParentId) {
        boolean result = directoryService.moveDirectory(id, newParentId);
        return CommonResult.success(result);
    }

    @PutMapping("/sort")
    @Operation(summary = "调整目录排序")
    @PreAuthorize("@ss.hasPermission('directory:update')")
    public CommonResult<Boolean> updateSort(@RequestParam("id") Long id, @RequestParam("sort") Integer sort) {
        boolean result = directoryService.updateSort(id, sort);
        return CommonResult.success(true);
    }
} 