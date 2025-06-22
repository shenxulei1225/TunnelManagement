package cn.iocoder.yudao.module.system.service.field;

import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValueSaveReqVO;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValueRespVO;
import cn.iocoder.yudao.module.system.controller.admin.field.vo.FieldValuePageReqVO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 动态字段值 Service
 */
public interface FieldValueService {

    /** 保存（新增或更新）一条业务记录的所有字段值 */
    Long saveFieldValues(FieldValueSaveReqVO reqVO);

    /** 分页查询业务数据 */
    IPage<FieldValueRespVO> getFieldValuePage(FieldValuePageReqVO pageReqVO);
}
