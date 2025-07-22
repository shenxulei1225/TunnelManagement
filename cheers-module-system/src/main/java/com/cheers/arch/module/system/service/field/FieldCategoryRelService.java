package com.cheers.arch.module.system.service.field;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelBatchCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelPageReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryRelUpdateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldCategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldCategoryRelDO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;

/**
 * 字段分类关联 Service 接口
 *
 * @author cheers
 */
public interface FieldCategoryRelService {

    /**
     * 创建字段分类关联
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createFieldCategoryRel(@Valid FieldCategoryRelCreateReqVO createReqVO);

    /**
     * 便捷方法：创建字段分类关联
     */
    default Long createFieldCategoryRel(Long fieldId, Long categoryId) {
        FieldCategoryRelCreateReqVO createReqVO = new FieldCategoryRelCreateReqVO();
        createReqVO.setFieldId(fieldId);
        createReqVO.setCategoryId(categoryId);
        return createFieldCategoryRel(createReqVO);
    }

    /**
     * 便捷方法：创建字段分类关联（带排序）
     */
    default Long createFieldCategoryRel(Long fieldId, Long categoryId, Integer sort) {
        FieldCategoryRelCreateReqVO createReqVO = new FieldCategoryRelCreateReqVO();
        createReqVO.setFieldId(fieldId);
        createReqVO.setCategoryId(categoryId);
        createReqVO.setSort(sort);
        return createFieldCategoryRel(createReqVO);
    }

    /**
     * 更新字段分类关联
     *
     * @param updateReqVO 更新信息
     */
    void updateFieldCategoryRel(@Valid FieldCategoryRelUpdateReqVO updateReqVO);

    /**
     * 更新字段分类关联的排序
     *
     * @param fieldId    字段ID
     * @param categoryId 分类ID
     * @param sort       排序值
     */
    void updateFieldCategoryRelSort(Long fieldId, Long categoryId, Integer sort);

    /**
     * 删除字段分类关联
     *
     * @param id 编号
     */
    void deleteFieldCategoryRel(Long id);

    /**
     * 删除字段分类关联
     *
     * @param fieldId    字段ID
     * @param categoryId 分类ID
     */
    void deleteFieldCategoryRel(Long fieldId, Long categoryId);

    /**
     * 删除字段的所有分类关联
     *
     * @param fieldId 字段ID
     */
    void deleteFieldCategoryRelsByFieldId(Long fieldId);

    /**
     * 删除分类的所有字段关联
     *
     * @param categoryId 分类ID
     */
    void deleteFieldCategoryRelsByCategoryId(Long categoryId);

    /**
     * 获得字段分类关联
     *
     * @param id 编号
     * @return 字段分类关联
     */
    FieldCategoryRelDO getFieldCategoryRel(Long id);

    /**
     * 获得字段分类关联分页
     *
     * @param pageReqVO 分页查询
     * @return 字段分类关联分页
     */
    PageResult<FieldCategoryRelDO> getFieldCategoryRelPage(FieldCategoryRelPageReqVO pageReqVO);

    /**
     * 检查字段分类关联是否存在
     *
     * @param fieldId    字段ID
     * @param categoryId 分类ID
     * @return 是否存在
     */
    boolean existsFieldCategoryRel(Long fieldId, Long categoryId);

    /**
     * 根据分类ID获取字段列表
     *
     * @param categoryId 分类ID
     * @return 字段列表
     */
    List<FieldDO> getFieldsByCategoryId(Long categoryId);

    /**
     * 根据字段ID获取分类ID列表
     *
     * @param fieldId 字段ID
     * @return 分类ID列表
     */
    List<Long> getCategoryIdsByFieldId(Long fieldId);

    /**
     * 获取字段分类关联记录
     *
     * @param fieldIds 字段ID列表，为空时查询所有
     * @return 字段分类关联列表
     */
    List<FieldCategoryRelDO> getFieldCategoryRels(List<Long> fieldIds);

    /**
     * 获取分类中字段的排序映射
     *
     * @param categoryId 分类ID
     * @return 字段ID -> 排序值的映射
     */
    Map<Long, Integer> getFieldSortMapInCategory(Long categoryId);

    /**
     * 批量创建字段分类关联
     *
     * @param fieldId     字段ID
     * @param categoryIds 分类ID列表
     */
    void batchCreateFieldCategoryRels(Long fieldId, List<Long> categoryIds);

    /**
     * 批量创建字段分类关联（带排序映射）
     *
     * @param fieldId            字段ID
     * @param categoryIdSortMap  分类ID -> 排序值的映射
     */
    void batchCreateFieldCategoryRels(Long fieldId, Map<Long, Integer> categoryIdSortMap);

    /**
     * 替换字段的所有分类关联
     *
     * @param fieldId     字段ID
     * @param categoryIds 新的分类ID列表
     */
    void replaceFieldCategoryRels(Long fieldId, List<Long> categoryIds);

    /**
     * 替换字段的所有分类关联（带排序映射）
     *
     * @param fieldId            字段ID
     * @param categoryIdSortMap  分类ID -> 排序值的映射
     */
    void replaceFieldCategoryRels(Long fieldId, Map<Long, Integer> categoryIdSortMap);

    /**
     * 批量更新字段在分类中的排序
     *
     * @param categoryId      分类ID
     * @param fieldIdSortMap  字段ID -> 排序值的映射
     */
    void batchUpdateFieldSortInCategory(Long categoryId, Map<Long, Integer> fieldIdSortMap);

    /**
     * 获取分类中的下一个排序值
     *
     * @param categoryId 分类ID
     * @return 下一个排序值
     */
    Integer getNextSortInCategory(Long categoryId);

    // ==================== 业务操作接口 ====================

    /**
     * 关联字段到分类
     *
     * @param data 关联信息
     * @return 是否成功
     */
    Boolean linkFieldCategory(FieldCategoryUpdateReqVO data);

    /**
     * 更新字段的分类关联
     *
     * @param data 更新信息
     * @return 是否成功
     */
    Boolean updateFieldCategory(FieldCategoryUpdateReqVO data);

    /**
     * 解除字段与分类的关联
     *
     * @param fieldId    字段ID
     * @param categoryId 分类ID
     * @return 是否成功
     */
    Boolean unlinkFieldCategory(Long fieldId, Long categoryId);

    /**
     * 解除字段与所有分类的关联
     *
     * @param fieldId 字段ID
     * @return 是否成功
     */
    Boolean unlinkAllFieldCategories(Long fieldId);

    /**
     * 批量更新字段的分类关联
     *
     * @param data 批量更新信息
     * @return 是否成功
     */
    Boolean batchUpdateFieldCategory(FieldCategoryRelBatchCreateReqVO data);

} 