package com.cheers.arch.framework.category.service;

import com.cheers.arch.framework.category.dal.dataobject.CategoryDO;
import com.cheers.arch.framework.category.service.vo.CategoryCreateReqVO;
import com.cheers.arch.framework.category.service.vo.CategoryUpdateReqVO;

import java.util.List;

/**
 * 分类 Service 接口
 */
public interface CategoryService {

    /**
     * 创建分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createCategory(CategoryCreateReqVO createReqVO);

    /**
     * 更新分类
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
     * @param businessType 业务类型
     * @return 分类树
     */
    List<CategoryDO> getCategoryTree(String businessType);

    /**
     * 获得分类列表
     *
     * @param parentId 父分类编号
     * @param businessType 业务类型
     * @return 分类列表
     */
    List<CategoryDO> getCategoryListByParentId(Long parentId, String businessType);

    /**
     * 检查分类是否可以删除
     *
     * @param id 分类编号
     * @return 是否可以删除
     */
    boolean canDeleteCategory(Long id);

} 