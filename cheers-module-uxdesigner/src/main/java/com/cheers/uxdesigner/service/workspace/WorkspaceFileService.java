package com.cheers.uxdesigner.service.workspace;

import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFileCreateReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFilePageReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFileUpdateReqVO;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceFileDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import jakarta.validation.Valid;

/**
 * 工作台文件 Service 接口
 *
 * @author UX Designer
 */
public interface WorkspaceFileService {

    /**
     * 创建工作台文件
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createWorkspaceFile(@Valid WorkspaceFileCreateReqVO createReqVO);

    /**
     * 更新工作台文件
     *
     * @param updateReqVO 更新信息
     */
    void updateWorkspaceFile(@Valid WorkspaceFileUpdateReqVO updateReqVO);

    /**
     * 删除工作台文件
     *
     * @param id 编号
     */
    void deleteWorkspaceFile(Long id);

    /**
     * 获得工作台文件
     *
     * @param id 编号
     * @return 工作台文件
     */
    WorkspaceFileDO getWorkspaceFile(Long id);

    /**
     * 获得工作台文件分页
     *
     * @param pageReqVO 分页查询
     * @return 工作台文件分页
     */
    PageResult<WorkspaceFileDO> getWorkspaceFilePage(WorkspaceFilePageReqVO pageReqVO);

    /**
     * 复制工作台文件
     *
     * @param id 编号
     * @return 新文件编号
     */
    Long duplicateWorkspaceFile(Long id);

    /**
     * 收藏/取消收藏工作台文件
     *
     * @param id 编号
     * @param starred 是否收藏
     */
    void starWorkspaceFile(Long id, Boolean starred);

    /**
     * 移动文件到回收站
     *
     * @param id 编号
     */
    void moveToTrash(Long id);

    /**
     * 从回收站恢复文件
     *
     * @param id 编号
     */
    void restoreFromTrash(Long id);

} 