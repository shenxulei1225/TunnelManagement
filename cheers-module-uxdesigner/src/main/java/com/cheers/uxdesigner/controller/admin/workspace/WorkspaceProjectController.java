package com.cheers.uxdesigner.controller.admin.workspace;

import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectCreateReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectPageReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectRespVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectUpdateReqVO;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;
import com.cheers.uxdesigner.service.workspace.WorkspaceProjectService;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 工作台项目")
@RestController
@RequestMapping("/uxdesigner/workspace/project")
@Validated
public class WorkspaceProjectController {

    @Resource
    private WorkspaceProjectService workspaceProjectService;

    @PostMapping("/create")
    @Operation(summary = "创建工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:create')")
    public CommonResult<Long> createWorkspaceProject(@Valid @RequestBody WorkspaceProjectCreateReqVO createReqVO) {
        return success(workspaceProjectService.createProject(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:update')")
    public CommonResult<Boolean> updateWorkspaceProject(@Valid @RequestBody WorkspaceProjectUpdateReqVO updateReqVO) {
        workspaceProjectService.updateProject(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:delete')")
    public CommonResult<Boolean> deleteWorkspaceProject(@RequestParam("id") Long id) {
        workspaceProjectService.deleteProject(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工作台项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:query')")
    public CommonResult<WorkspaceProjectRespVO> getWorkspaceProject(@RequestParam("id") Long id) {
        WorkspaceProjectDO project = workspaceProjectService.getProject(id);
        return success(BeanUtils.toBean(project, WorkspaceProjectRespVO.class));
    }

    @GetMapping("/test")
    @Operation(summary = "测试接口")
    public CommonResult<String> test() {
        try {
            // 直接查询数据库，不经过权限验证
            PageResult<WorkspaceProjectDO> pageResult = workspaceProjectService.getProjectPage(new WorkspaceProjectPageReqVO());
            return success("测试成功，查询到 " + pageResult.getTotal() + " 个项目");
        } catch (Exception e) {
            return success("测试失败：" + e.getMessage());
        }
    }

    @GetMapping("/page")
    @Operation(summary = "获得工作台项目分页")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:query')")
    public CommonResult<PageResult<WorkspaceProjectRespVO>> getWorkspaceProjectPage(@Valid WorkspaceProjectPageReqVO pageReqVO) {
        try {
            System.out.println("DEBUG: 开始查询项目分页，参数：" + pageReqVO);
            PageResult<WorkspaceProjectDO> pageResult = workspaceProjectService.getProjectPage(pageReqVO);
            System.out.println("DEBUG: 查询结果 - 总数：" + pageResult.getTotal() + "，列表大小：" + (pageResult.getList() != null ? pageResult.getList().size() : "null"));
            if (pageResult.getList() != null && !pageResult.getList().isEmpty()) {
                System.out.println("DEBUG: 第一个项目：" + pageResult.getList().get(0));
            }
            PageResult<WorkspaceProjectRespVO> result = BeanUtils.toBean(pageResult, WorkspaceProjectRespVO.class);
            System.out.println("DEBUG: 转换后结果 - 总数：" + result.getTotal() + "，列表大小：" + (result.getList() != null ? result.getList().size() : "null"));
            return success(result);
        } catch (Exception e) {
            System.err.println("DEBUG: 查询项目分页异常：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工作台项目 Excel")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:export')")
    public void exportWorkspaceProjectExcel(@Valid WorkspaceProjectPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(Integer.MAX_VALUE); // 导出所有数据
        List<WorkspaceProjectDO> list = workspaceProjectService.getProjectPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "工作台项目.xls", "数据", WorkspaceProjectRespVO.class,
                        BeanUtils.toBean(list, WorkspaceProjectRespVO.class));
    }

    @PostMapping("/duplicate")
    @Operation(summary = "复制工作台项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:create')")
    public CommonResult<Long> duplicateWorkspaceProject(@RequestParam("id") Long id) {
        return success(workspaceProjectService.duplicateProject(id));
    }

    @PutMapping("/star")
    @Operation(summary = "收藏/取消收藏工作台项目")
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:update')")
    public CommonResult<Boolean> starWorkspaceProject(@RequestParam("id") Long id, 
                                                     @RequestParam("starred") Boolean starred) {
        workspaceProjectService.starProject(id, starred);
        return success(true);
    }

    @PutMapping("/move-to-trash")
    @Operation(summary = "移动项目到回收站")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:delete')")
    public CommonResult<Boolean> moveProjectToTrash(@RequestParam("id") Long id) {
        workspaceProjectService.moveProjectToTrash(id);
        return success(true);
    }

    @PutMapping("/restore-from-trash")
    @Operation(summary = "从回收站恢复项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('uxdesigner:workspace-project:update')")
    public CommonResult<Boolean> restoreProjectFromTrash(@RequestParam("id") Long id) {
        workspaceProjectService.restoreProjectFromTrash(id);
        return success(true);
    }
} 