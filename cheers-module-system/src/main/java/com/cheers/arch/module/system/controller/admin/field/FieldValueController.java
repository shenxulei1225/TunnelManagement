package com.cheers.arch.module.system.controller.admin.field;

import com.cheers.arch.framework.common.pojo.CommonResult;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueRespVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import com.cheers.arch.module.system.service.field.FieldValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.cheers.arch.framework.common.pojo.CommonResult.success;

@RestController
@RequestMapping("/system/field-value")
@RequiredArgsConstructor
@Validated
public class FieldValueController {

    private final FieldValueService fieldValueService;

    @PostMapping("/save")
    public CommonResult<Long> save(@RequestBody @Validated FieldValueSaveReqVO reqVO) {
        return success(fieldValueService.saveFieldValues(reqVO));
    }

    @GetMapping("/page")
    public CommonResult<PageResult<FieldValueRespVO>> page(FieldValuePageReqVO pageReqVO) {
        com.baomidou.mybatisplus.core.metadata.IPage<FieldValueRespVO> page = fieldValueService.getFieldValuePage(pageReqVO);
        return success(new PageResult<>(page.getRecords(), page.getTotal()));
    }
}
