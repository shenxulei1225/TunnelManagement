package com.cheers.arch.module.system.controller.admin.tree;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeRespVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeNodeSaveReqVO;
import com.cheers.arch.module.system.service.tree.TreeDataService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "管理后台 - 通用树数据")
@RestController
@RequestMapping("/system/tree-data")
@Validated
public class TreeDataController {

    @Resource
    private TreeDataService treeDataService;

    @GetMapping("/data/{treeType}")
    @Operation(summary = "获取树形数据")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<List<TreeNodeRespVO>> getTreeData(@PathVariable("treeType") String treeType) {
        List<TreeNodeRespVO> treeData = treeDataService.getTreeData(treeType);
        return success(treeData);
    }

    @GetMapping("/node/{treeType}/{nodeId}")
    @Operation(summary = "获取树节点详情")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @Parameter(name = "nodeId", description = "节点ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:tree:query')")
    public CommonResult<TreeNodeRespVO> getTreeNode(@PathVariable("treeType") String treeType, 
                                                   @PathVariable("nodeId") Long nodeId) {
        TreeNodeRespVO node = treeDataService.getTreeNode(treeType, nodeId);
        return success(node);
    }

    @PostMapping("/node/{treeType}")
    @Operation(summary = "添加树节点")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @PreAuthorize("@ss.hasPermission('system:tree:create')")
    public CommonResult<Long> addTreeNode(@PathVariable("treeType") String treeType,
                                        @Valid @RequestBody TreeNodeSaveReqVO createReqVO) {
        Long nodeId = treeDataService.addTreeNode(treeType, createReqVO);
        return success(nodeId);
    }

    @PutMapping("/node/{treeType}/{nodeId}")
    @Operation(summary = "更新树节点")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @Parameter(name = "nodeId", description = "节点ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:tree:update')")
    public CommonResult<Boolean> updateTreeNode(@PathVariable("treeType") String treeType,
                                              @PathVariable("nodeId") Long nodeId,
                                              @Valid @RequestBody TreeNodeSaveReqVO updateReqVO) {
        treeDataService.updateTreeNode(treeType, nodeId, updateReqVO);
        return success(true);
    }

    @DeleteMapping("/node/{treeType}/{nodeId}")
    @Operation(summary = "删除树节点")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @Parameter(name = "nodeId", description = "节点ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:tree:delete')")
    public CommonResult<Boolean> deleteTreeNode(@PathVariable("treeType") String treeType,
                                              @PathVariable("nodeId") Long nodeId) {
        treeDataService.deleteTreeNode(treeType, nodeId);
        return success(true);
    }

    @PutMapping("/node/{treeType}/{nodeId}/move")
    @Operation(summary = "移动树节点")
    @Parameter(name = "treeType", description = "树类型", required = true, example = "field_category")
    @Parameter(name = "nodeId", description = "节点ID", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('system:tree:update')")
    public CommonResult<Boolean> moveTreeNode(@PathVariable("treeType") String treeType,
                                            @PathVariable("nodeId") Long nodeId,
                                            @RequestParam("targetParentId") Long targetParentId,
                                            @RequestParam("position") Integer position) {
        treeDataService.moveTreeNode(treeType, nodeId, targetParentId, position);
        return success(true);
    }
} 