package com.cheers.arch.module.system.controller.admin.field;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

import java.util.List;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldDefUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDefDO;
import com.cheers.arch.module.system.service.field.FieldDefService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

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

    @GetMapping("/list-by-semantic-directory")
    public CommonResult<List<FieldDefDO>> getFieldDefListBySemanticDirectory(
            @RequestParam(value = "semanticDirectoryId", required = false) Long semanticDirectoryId) {
        return success(fieldDefService.getFieldDefListBySemanticDirectory(semanticDirectoryId));
    }

    @PutMapping("/update-semantic-directory")
    public CommonResult<Boolean> updateFieldSemanticDirectory(
            @RequestParam("fieldId") Long fieldId,
            @RequestParam(value = "semanticDirectoryId", required = false) Long semanticDirectoryId) {
        return success(fieldDefService.updateFieldSemanticDirectory(fieldId, semanticDirectoryId));
    }

    @PutMapping("/batch-update-semantic-directory")
    public CommonResult<Integer> batchUpdateFieldSemanticDirectory(
            @RequestParam("fieldIds") List<Long> fieldIds,
            @RequestParam(value = "semanticDirectoryId", required = false) Long semanticDirectoryId) {
        return success(fieldDefService.batchUpdateFieldSemanticDirectory(fieldIds, semanticDirectoryId));
    }

    @GetMapping("/semantic-directories")
    public CommonResult<List<Object>> getSemanticDirectories() {
        return success(fieldDefService.getSemanticDirectories());
    }
}
