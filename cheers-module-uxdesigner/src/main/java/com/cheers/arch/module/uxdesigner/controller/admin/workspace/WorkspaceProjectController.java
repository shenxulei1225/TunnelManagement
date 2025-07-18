package com.cheers.arch.module.uxdesigner.controller.admin.workspace;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.common.util.object.BeanUtils;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.*;
import com.cheers.arch.module.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;
import com.cheers.arch.module.uxdesigner.service.workspace.WorkspaceProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 工作台项目")
@RestController
@RequestMapping("/uxdesigner/workspace/project")
@Validated
public class WorkspaceProjectController {

    @Resource
    private WorkspaceProjectService projectService;

    @PostMapping("/create")
    @Operation(summary = "创建工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:create')")
    public CommonResult<Long> createProject(@Valid @RequestBody WorkspaceProjectCreateReqVO createReqVO) {
        return success(projectService.createProject(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:update')")
    public CommonResult<Boolean> updateProject(@Valid @RequestBody WorkspaceProjectUpdateReqVO updateReqVO) {
        projectService.updateProject(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:delete')")
    public CommonResult<Boolean> deleteProject(@RequestParam("id") Long id) {
        projectService.deleteProject(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作台项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:query')")
    public CommonResult<WorkspaceProjectRespVO> getProject(@RequestParam("id") Long id) {
        WorkspaceProjectDO project = projectService.getProject(id);
        return success(BeanUtils.toBean(project, WorkspaceProjectRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作台项目分页")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:query')")
    public CommonResult<PageResult<WorkspaceProjectRespVO>> getProjectPage(@Valid WorkspaceProjectPageReqVO pageReqVO) {
        PageResult<WorkspaceProjectDO> pageResult = projectService.getProjectPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkspaceProjectRespVO.class));
    }

    @PostMapping("/duplicate")
    @Operation(summary = "复制项目", description = "用于设计版本管理、模板创建、实验性设计等场景")
    @Parameter(name = "id", description = "项目编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:create')")
    public CommonResult<Long> duplicateProject(@RequestParam("id") Long id) {
        return success(projectService.duplicateProject(id));
    }

    @PutMapping("/star")
    @Operation(summary = "收藏/取消收藏工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:update')")
    public CommonResult<Boolean> starProject(@RequestParam("id") Long id, 
                                           @RequestParam("starred") Boolean starred) {
        projectService.starProject(id, starred);
        return success(true);
    }
} 