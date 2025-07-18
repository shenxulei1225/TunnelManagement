package com.cheers.arch.module.dynamic.controller.admin.businessmodule;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.dynamic.controller.admin.businessmodule.vo.DynamicBusinessModuleTreeVO;
import com.cheers.arch.module.dynamic.controller.admin.businessmodule.vo.DynamicBusinessModuleDragReqVO;
import com.cheers.arch.module.dynamic.dal.dataobject.businessmodule.DynamicBusinessModuleDO;
import com.cheers.arch.module.dynamic.service.businessmodule.DynamicBusinessModuleService;
import com.cheers.arch.module.dynamic.convert.businessmodule.DynamicBusinessModuleConvert;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 动态业务分组/系统（多级business_module）")
@RestController
@RequestMapping("/dynamic-business/business-module")
public class DynamicBusinessModuleController {

    @Resource
    private DynamicBusinessModuleService dynamicBusinessModuleService;

    @GetMapping("/tree")
    @Operation(summary = "获得业务分组树")
    public CommonResult<List<DynamicBusinessModuleTreeVO>> getBusinessModuleTree(@RequestParam(value = "bizType", required = false) String bizType) {
        List<DynamicBusinessModuleDO> list = dynamicBusinessModuleService.getBusinessModuleTree(bizType);
        return success(DynamicBusinessModuleConvert.INSTANCE.buildTree(list));
    }

    @GetMapping("/sub-tree")
    @Operation(summary = "获得指定节点下的子树")
    public CommonResult<List<DynamicBusinessModuleTreeVO>> getBusinessModuleSubTree(@RequestParam("parentId") Long parentId) {
        List<DynamicBusinessModuleDO> list = dynamicBusinessModuleService.getBusinessModuleSubTree(parentId);
        return success(DynamicBusinessModuleConvert.INSTANCE.buildTree(list));
    }

    @PostMapping("/drag")
    @Operation(summary = "拖拽调整树结构")
    public CommonResult<Boolean> dragBusinessModule(@Valid @RequestBody DynamicBusinessModuleDragReqVO req) {
        return success(dynamicBusinessModuleService.dragBusinessModule(req.getDragId(), req.getTargetParentId(), req.getPosition(), req.getTargetId()));
    }

    @PostMapping("/create")
    @Operation(summary = "创建业务分组")
    public CommonResult<Long> createBusinessModule(@Valid @RequestBody DynamicBusinessModuleDO req) {
        return success(dynamicBusinessModuleService.createBusinessModule(req));
    }

    @PutMapping("/update")
    @Operation(summary = "更新业务分组")
    public CommonResult<Boolean> updateBusinessModule(@Valid @RequestBody DynamicBusinessModuleDO req) {
        return success(dynamicBusinessModuleService.updateBusinessModule(req));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除业务分组")
    public CommonResult<Boolean> deleteBusinessModule(@RequestParam("id") Long id) {
        return success(dynamicBusinessModuleService.deleteBusinessModule(id));
    }
}
