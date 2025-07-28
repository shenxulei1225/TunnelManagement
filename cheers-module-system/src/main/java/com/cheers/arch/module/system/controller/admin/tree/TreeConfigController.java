package com.cheers.arch.module.system.controller.admin.tree;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigCreateReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigPageReqVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigRespVO;
import com.cheers.arch.module.system.controller.admin.tree.vo.TreeConfigUpdateReqVO;
import com.cheers.arch.module.system.service.tree.TreeConfigService;

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

@Tag(name = "管理后台 - 树形配置管理")
@RestController
@RequestMapping("/system/tree-config")
@Validated
public class TreeConfigController {

    @Resource
    private TreeConfigService treeConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建树形配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:create')")
    public CommonResult<Long> createTreeConfig(@Valid @RequestBody TreeConfigCreateReqVO createReqVO) {
        return success(treeConfigService.createTreeConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新树形配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:update')")
    public CommonResult<Boolean> updateTreeConfig(@Valid @RequestBody TreeConfigUpdateReqVO updateReqVO) {
        treeConfigService.updateTreeConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除树形配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:tree-config:delete')")
    public CommonResult<Boolean> deleteTreeConfig(@RequestParam("id") Long id) {
        treeConfigService.deleteTreeConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得树形配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:tree-config:query')")
    public CommonResult<TreeConfigRespVO> getTreeConfig(@RequestParam("id") Long id) {
        TreeConfigRespVO config = treeConfigService.getTreeConfig(id);
        return success(config);
    }

    @GetMapping("/get-by-scene")
    @Operation(summary = "根据场景获得树形配置")
    @Parameter(name = "scene", description = "使用场景", required = true, example = "field-management")
    @PreAuthorize("@ss.hasPermission('system:tree-config:query')")
    public CommonResult<TreeConfigRespVO> getTreeConfigByScene(@RequestParam("scene") String scene) {
        TreeConfigRespVO config = treeConfigService.getTreeConfigByScene(scene);
        return success(config);
    }

    @GetMapping("/page")
    @Operation(summary = "获得树形配置分页")
    @PreAuthorize("@ss.hasPermission('system:tree-config:query')")
    public CommonResult<PageResult<TreeConfigRespVO>> getTreeConfigPage(@Valid TreeConfigPageReqVO pageVO) {
        PageResult<TreeConfigRespVO> pageResult = treeConfigService.getTreeConfigPage(pageVO);
        return success(pageResult);
    }

    @PostMapping("/save-user-config")
    @Operation(summary = "保存用户个人配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:save')")
    public CommonResult<Boolean> saveUserTreeConfig(
            @Parameter(description = "场景标识", required = true) @RequestParam("scene") String scene,
            @Parameter(description = "配置名称", required = false) @RequestParam(value = "configName", required = false) String configName,
            @RequestBody String configJson) {
        treeConfigService.saveUserTreeConfig(scene, configName, configJson);
        return success(true);
    }

    @GetMapping("/load-user-config")
    @Operation(summary = "加载用户个人配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:query')")
    public CommonResult<TreeConfigRespVO> loadUserTreeConfig(
            @Parameter(description = "场景标识", required = true) @RequestParam("scene") String scene) {
        TreeConfigRespVO config = treeConfigService.loadUserTreeConfig(scene);
        return success(config);
    }

    @PostMapping("/save-global-config")
    @Operation(summary = "保存全局默认配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:admin')")
    public CommonResult<Boolean> saveGlobalTreeConfig(
            @Parameter(description = "场景标识", required = true) @RequestParam("scene") String scene,
            @Parameter(description = "配置名称", required = true) @RequestParam("configName") String configName,
            @RequestBody String configJson) {
        treeConfigService.saveGlobalTreeConfig(scene, configName, configJson);
        return success(true);
    }

    @GetMapping("/list-by-scene")
    @Operation(summary = "获取场景下的所有配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:query')")
    public CommonResult<List<TreeConfigRespVO>> getTreeConfigListByScene(
            @Parameter(description = "场景标识", required = true) @RequestParam("scene") String scene) {
        List<TreeConfigRespVO> configs = treeConfigService.getTreeConfigListByScene(scene);
        return success(configs);
    }

    @DeleteMapping("/clear-user-config")
    @Operation(summary = "清除用户个人配置")
    @PreAuthorize("@ss.hasPermission('system:tree-config:delete')")
    public CommonResult<Boolean> clearUserTreeConfig(
            @Parameter(description = "场景标识", required = true) @RequestParam("scene") String scene) {
        treeConfigService.clearUserTreeConfig(scene);
        return success(true);
    }

} 