package com.cheers.arch.module.uxdesigner.controller.admin.workspace;

import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceFileCreateReqVO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceFilePageReqVO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceFileRespVO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceFileUpdateReqVO;
import com.cheers.arch.module.uxdesigner.dal.dataobject.workspace.WorkspaceFileDO;
import com.cheers.arch.module.uxdesigner.service.workspace.WorkspaceFileService;
import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.pojo.PageParam;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.framework.excel.core.util.ExcelUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 工作台文件")
@RestController
@RequestMapping("/uxdesigner/workspace/file")
@Validated
public class WorkspaceFileController {

    @Resource
    private WorkspaceFileService workspaceFileService;

    @PostMapping("/create")
    @Operation(summary = "创建工作台文件")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:create')")
    public CommonResult<Long> createWorkspaceFile(@Valid @RequestBody WorkspaceFileCreateReqVO createReqVO) {
        return success(workspaceFileService.createWorkspaceFile(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作台文件")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:update')")
    public CommonResult<Boolean> updateWorkspaceFile(@Valid @RequestBody WorkspaceFileUpdateReqVO updateReqVO) {
        workspaceFileService.updateWorkspaceFile(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作台文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:delete')")
    public CommonResult<Boolean> deleteWorkspaceFile(@RequestParam("id") Long id) {
        workspaceFileService.deleteWorkspaceFile(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作台文件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:query')")
    public CommonResult<WorkspaceFileRespVO> getWorkspaceFile(@RequestParam("id") Long id) {
        WorkspaceFileDO workspaceFile = workspaceFileService.getWorkspaceFile(id);
        return success(BeanUtils.toBean(workspaceFile, WorkspaceFileRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作台文件分页")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:query')")
    public CommonResult<PageResult<WorkspaceFileRespVO>> getWorkspaceFilePage(@Valid WorkspaceFilePageReqVO pageReqVO) {
        PageResult<WorkspaceFileDO> pageResult = workspaceFileService.getWorkspaceFilePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkspaceFileRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工作台文件 Excel")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:export')")
    public void exportWorkspaceFileExcel(@Valid WorkspaceFilePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WorkspaceFileDO> list = workspaceFileService.getWorkspaceFilePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "工作台文件.xls", "数据", WorkspaceFileRespVO.class,
                        BeanUtils.toBean(list, WorkspaceFileRespVO.class));
    }

    @PostMapping("/duplicate")
    @Operation(summary = "复制工作台文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:create')")
    public CommonResult<Long> duplicateWorkspaceFile(@RequestParam("id") Long id) {
        return success(workspaceFileService.duplicateWorkspaceFile(id));
    }

    @PutMapping("/star")
    @Operation(summary = "收藏/取消收藏工作台文件")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:update')")
    public CommonResult<Boolean> starWorkspaceFile(@RequestParam("id") Long id, 
                                                   @RequestParam("starred") Boolean starred) {
        workspaceFileService.starWorkspaceFile(id, starred);
        return success(true);
    }

    @PutMapping("/move-to-trash")
    @Operation(summary = "移动文件到回收站")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:delete')")
    public CommonResult<Boolean> moveToTrash(@RequestParam("id") Long id) {
        workspaceFileService.moveToTrash(id);
        return success(true);
    }

    @PutMapping("/restore-from-trash")
    @Operation(summary = "从回收站恢复文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-file:update')")
    public CommonResult<Boolean> restoreFromTrash(@RequestParam("id") Long id) {
        workspaceFileService.restoreFromTrash(id);
        return success(true);
    }

} 