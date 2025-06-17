package cn.iocoder.yudao.module.system.controller.admin.region;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionFieldDefDO;
import cn.iocoder.yudao.module.system.service.region.RegionFieldDefService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import jakarta.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 区域自定义字段定义")
@RestController
@RequestMapping("/system/region/field-def")
public class RegionFieldDefController {

    @Resource
    private RegionFieldDefService fieldDefService;

        @PostMapping("/create")
    @Operation(summary = "创建字段定义")
    public CommonResult<Long> createFieldDef(@Valid @RequestBody RegionFieldDefDO req) {
        return success(fieldDefService.createFieldDef(req));
    }

    @PutMapping("/update")
    @Operation(summary = "更新字段定义")
    public CommonResult<Boolean> updateFieldDef(@Valid @RequestBody RegionFieldDefDO req) {
        return success(fieldDefService.updateFieldDef(req));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除字段定义")
    public CommonResult<Boolean> deleteFieldDef(@RequestParam("id") Long id) {
        return success(fieldDefService.deleteFieldDef(id));
    }

    @GetMapping("/list")
    @Operation(summary = "获得字段定义列表")
    public CommonResult<List<RegionFieldDefDO>> getFieldDefList(@RequestParam(value = "regionType", required = false) Integer regionType) {
        return success(fieldDefService.getFieldDefs(regionType));
    }
}
