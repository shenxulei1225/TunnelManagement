package com.cheers.arch.module.uxdesigner.service.workspace;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectCreateReqVO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectPageReqVO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectUpdateReqVO;
import com.cheers.arch.module.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;

/**
 * 工作台项目 Service 接口
 *
 * @author UX Designer
 */
public interface WorkspaceProjectService {

    /**
     * 创建工作台项目
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProject(WorkspaceProjectCreateReqVO createReqVO);

    /**
     * 更新工作台项目
     *
     * @param updateReqVO 更新信息
     */
    void updateProject(WorkspaceProjectUpdateReqVO updateReqVO);

    /**
     * 删除工作台项目
     *
     * @param id 编号
     */
    void deleteProject(Long id);

    /**
     * 获得工作台项目
     *
     * @param id 编号
     * @return 工作台项目
     */
    WorkspaceProjectDO getProject(Long id);

    /**
     * 获得工作台项目分页
     *
     * @param pageReqVO 分页查询
     * @return 工作台项目分页
     */
    PageResult<WorkspaceProjectDO> getProjectPage(WorkspaceProjectPageReqVO pageReqVO);

    /**
     * 复制工作台项目
     * 用于设计版本管理、模板创建、实验性设计等场景
     *
     * @param id 项目编号
     * @return 新项目编号
     */
    Long duplicateProject(Long id);

    /**
     * 收藏/取消收藏工作台项目
     *
     * @param id 项目编号
     * @param starred 是否收藏
     */
    void starProject(Long id, Boolean starred);

} 