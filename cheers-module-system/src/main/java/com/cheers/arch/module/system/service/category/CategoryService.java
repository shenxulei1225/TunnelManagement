package com.cheers.arch.module.system.service.category;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.category.vo.CategoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.category.vo.CategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryBizTypeRelDO;
import com.cheers.arch.module.system.dal.dataobject.category.CategoryDO;

/**
 * 分类 Service 接口
 */
public interface CategoryService {

    /**
     * 创建分类（推荐使用，安全）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCategory(CategoryCreateReqVO createReqVO);

    /**
     * 更新分类（推荐使用，安全）
     *
     * @param updateReqVO 更新信息
     */
    void updateCategory(CategoryUpdateReqVO updateReqVO);

    /**
     * 删除分类
     *
     * @param id 编号
     */
    void deleteCategory(Long id);

    /**
     * 获得分类
     *
     * @param id 编号
     * @return 分类
     */
    CategoryDO getCategory(Long id);

    /**
     * 获得分类列表
     *
     * @param ids 编号
     * @return 分类列表
     */
    List<CategoryDO> getCategoryList(List<Long> ids);

    /**
     * 获得分类树
     *
     * @return 分类树
     */
    List<CategoryDO> getCategoryTree();

    /**
     * 获得分类列表
     *
     * @param parentId 父分类编号
     * @return 分类列表
     */
    List<CategoryDO> getCategoryListByParentId(Long parentId);

    /**
     * 检查分类是否可以删除
     *
     * @param id 分类编号
     * @return 是否可以删除
     */
    boolean canDeleteCategory(Long id);

    // ========== 内部方法（已确保 tenantId 安全） ==========

    /**
     * 创建分类（内部使用，已设置 tenantId）
     *
     * @param bean 分类信息
     * @return 编号
     */
    Long createCategory(CategoryDO bean);

    /**
     * 更新分类（内部使用，已设置 tenantId）
     *
     * @param bean 分类信息
     * @return 是否成功
     */
    Boolean updateCategory(CategoryDO bean);

    // ========== 业务关联方法 ==========

    /**
     * 创建分类业务类型关联关系
     *
     * @param categoryId 分类ID
     * @param businessType 业务类型编码
     * @param businessId 业务对象ID
     * @param sort 排序
     * @param required 是否必填
     * @return 关联关系ID
     */
    Long createCategoryBizTypeRel(Long categoryId, String businessType, Long businessId, Integer sort, Boolean required);

    /**
     * 删除分类业务类型关联关系
     *
     * @param id 关联关系ID
     */
    void deleteCategoryBizTypeRel(Long id);

    /**
     * 根据业务类型和业务对象ID获取分类关联关系
     *
     * @param businessType 业务类型
     * @param businessId 业务对象ID
     * @return 分类关联关系列表
     */
    List<CategoryBizTypeRelDO> getCategoryBizTypeRelList(String businessType, Long businessId);

    /**
     * 根据业务类型获取分类树
     *
     * @param businessType 业务类型
     * @return 分类树
     */
    List<CategoryDO> getCategoryTreeByBusinessType(String businessType);

} 