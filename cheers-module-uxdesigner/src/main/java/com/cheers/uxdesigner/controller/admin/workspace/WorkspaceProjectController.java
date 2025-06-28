package com.cheers.uxdesigner.controller.admin.workspace;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.cheers.uxdesigner.controller.admin.workspace.vo.*;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;
import com.cheers.uxdesigner.service.workspace.WorkspaceProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 工作台项目")
@RestController
@RequestMapping("/uxdesigner/workspace/project")
public class WorkspaceProjectController {

    @Resource
    private WorkspaceProjectService projectService;

    @PostMapping("/create")
    @Operation(summary = "创建工作台项目")
    public CommonResult<Long> createProject(@Valid @RequestBody WorkspaceProjectCreateReqVO createReqVO) {
        return success(projectService.createProject(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作台项目")
    public CommonResult<Boolean> updateProject(@Valid @RequestBody WorkspaceProjectUpdateReqVO updateReqVO) {
        projectService.updateProject(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteProject(@RequestParam("id") Long id) {
        projectService.deleteProject(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作台项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WorkspaceProjectRespVO> getProject(@RequestParam("id") Long id) {
        WorkspaceProjectDO project = projectService.getProject(id);
        return success(BeanUtils.toBean(project, WorkspaceProjectRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作台项目分页")
    public CommonResult<PageResult<WorkspaceProjectRespVO>> getProjectPage(@Valid WorkspaceProjectPageReqVO pageReqVO) {
        PageResult<WorkspaceProjectDO> pageResult = projectService.getProjectPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WorkspaceProjectRespVO.class));
    }

    @PostMapping("/duplicate")
    @Operation(summary = "复制工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Long> duplicateProject(@RequestParam("id") Long id) {
        return success(projectService.duplicateProject(id));
    }

    @PutMapping("/star")
    @Operation(summary = "收藏/取消收藏工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    @Parameter(name = "starred", description = "是否收藏", required = true)
    public CommonResult<Boolean> starProject(@RequestParam("id") Long id, @RequestParam("starred") Boolean starred) {
        projectService.starProject(id, starred);
        return success(true);
    }

    @PutMapping("/move-to-trash")
    @Operation(summary = "移动项目到回收站")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> moveProjectToTrash(@RequestParam("id") Long id) {
        projectService.moveProjectToTrash(id);
        return success(true);
    }

    @PutMapping("/restore-from-trash")
    @Operation(summary = "从回收站恢复项目")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> restoreProjectFromTrash(@RequestParam("id") Long id) {
        projectService.restoreProjectFromTrash(id);
        return success(true);
    }

} 