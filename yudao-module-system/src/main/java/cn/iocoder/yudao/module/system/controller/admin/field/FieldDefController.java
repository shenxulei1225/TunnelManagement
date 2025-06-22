package cn.iocoder.yudao.module.system.controller.admin.field;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.*;
import cn.iocoder.yudao.module.system.dal.dataobject.field.FieldDefDO;
import cn.iocoder.yudao.module.system.service.field.FieldDefService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/system/field-def")
@RequiredArgsConstructor
@Validated
public class FieldDefController {

    private final FieldDefService fieldDefService;

    @PostMapping("/create")
    public CommonResult<Long> createFieldDef(@RequestBody @Validated FieldDefCreateReqVO reqVO) {
        return success(fieldDefService.createFieldDef(reqVO));
    }

    @PutMapping("/update")
    public CommonResult<Boolean> updateFieldDef(@RequestBody @Validated FieldDefUpdateReqVO reqVO) {
        return success(fieldDefService.updateFieldDef(reqVO));
    }

    @DeleteMapping("/delete")
    public CommonResult<Boolean> deleteFieldDef(@RequestParam("id") Long id) {
        return success(fieldDefService.deleteFieldDef(id));
    }

    @GetMapping("/get")
    public CommonResult<FieldDefDO> getFieldDef(@RequestParam("id") Long id) {
        return success(fieldDefService.getFieldDef(id));
    }

    @GetMapping("/list-by-category")
    public CommonResult<List<FieldDefDO>> getFieldDefListByCategory(@RequestParam(value = "categoryId", required = false) Long categoryId) {
        return success(fieldDefService.getFieldDefListByCategory(categoryId));
    }

    @GetMapping({"/list-by-bizType", "/list-by-biz-type"})
    public CommonResult<List<FieldDefDO>> getFieldDefListByBizType(@RequestParam("bizType") String bizType) {
        return success(fieldDefService.getFieldDefListByBizType(bizType));
    }
}
