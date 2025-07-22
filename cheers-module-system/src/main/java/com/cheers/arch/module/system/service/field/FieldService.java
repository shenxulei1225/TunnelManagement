package com.cheers.arch.module.system.service.field;

import java.util.List;

import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldExportReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;

/**
 * 字段 Service 接口
 */
public interface FieldService {

    /**
     * 创建字段
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createField(@Valid FieldCreateReqVO createReqVO);

    /**
     * 更新字段
     *
     * @param updateReqVO 更新信息
     */
    void updateField(@Valid FieldUpdateReqVO updateReqVO);

    /**
     * 删除字段
     *
     * @param id 编号
     */
    void deleteField(Long id);

    /**
     * 获得字段
     *
     * @param id 编号
     * @return 字段
     */
    FieldDO getField(Long id);

    /**
     * 获得字段列表
     *
     * @param ids 编号
     * @return 字段列表
     */
    List<FieldDO> getFieldList(List<Long> ids);

    /**
     * 获得所有字段列表
     *
     * @return 字段列表
     */
    List<FieldDO> getAllFieldList();

    /**
     * 获得字段分页
     *
     * @param pageReqVO 分页查询
     * @return 字段分页
     */
    PageResult<FieldDO> getFieldPage(FieldPageReqVO pageReqVO);

    /**
     * 获得字段列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 字段列表
     */
    List<FieldDO> getFieldList(FieldExportReqVO exportReqVO);

    /**
     * 根据分类ID获取字段列表
     *
     * @param categoryId 分类ID
     * @return 字段列表
     */
    List<FieldDO> getFieldsByCategory(Long categoryId);

    /**
     * 根据多个分类ID批量获取字段列表
     *
     * @param categoryIds 分类ID列表
     * @return 字段列表
     */
    List<FieldDO> getFieldsByCategoryIds(List<Long> categoryIds);

    /**
     * 批量更新字段
     * 用于分组删除或字段批量操作时调用
     *
     * @param fieldIds 字段ID列表
     * @param updateAction 更新操作类型
     * @param updateValue 更新值（可选）
     */
    void batchUpdateFields(List<Long> fieldIds, String updateAction, Object updateValue);

} 