package com.cheers.arch.module.system.service.field;

import java.util.List;

import com.cheers.arch.module.system.controller.admin.field.vo.CategoryCreateReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.CategoryUpdateReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.CategoryDO;

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

} 