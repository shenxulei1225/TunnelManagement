package cn.iocoder.yudao.module.system.controller.admin.field;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValueRespVO;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import cn.iocoder.yudao.module.system.service.field.FieldValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

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
