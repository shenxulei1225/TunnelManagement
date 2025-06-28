package com.cheers.uxdesigner.service.workspace;

import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFileCreateReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFilePageReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFileUpdateReqVO;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceFileDO;
import com.cheers.uxdesigner.dal.mysql.workspace.WorkspaceFileMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 工作台文件 Service 实现类
 *
 * @author UX Designer
 */
@Service
@Validated
public class WorkspaceFileServiceImpl implements WorkspaceFileService {

    @Resource
    private WorkspaceFileMapper workspaceFileMapper;

    @Override
    public Long createWorkspaceFile(WorkspaceFileCreateReqVO createReqVO) {
        // 插入
        WorkspaceFileDO workspaceFile = WorkspaceFileDO.builder()
                .name(createReqVO.getName())
                .type(createReqVO.getType())
                .thumbnail(createReqVO.getThumbnail())
                .content(createReqVO.getContent())
                .category(createReqVO.getCategory())
                .starred(createReqVO.getStarred())
                .projectId(createReqVO.getProjectId())
                .userId(SecurityFrameworkUtils.getLoginUserId())
                .teamId(createReqVO.getTeamId())
                .status(1) // 默认正常状态
                .remark(createReqVO.getRemark())
                .build();
        workspaceFileMapper.insert(workspaceFile);
        // 返回
        return workspaceFile.getId();
    }

    @Override
    public void updateWorkspaceFile(WorkspaceFileUpdateReqVO updateReqVO) {
        // 校验存在
        validateWorkspaceFileExists(updateReqVO.getId());
        // 更新
        WorkspaceFileDO updateObj = WorkspaceFileDO.builder()
                .id(updateReqVO.getId())
                .name(updateReqVO.getName())
                .thumbnail(updateReqVO.getThumbnail())
                .content(updateReqVO.getContent())
                .category(updateReqVO.getCategory())
                .starred(updateReqVO.getStarred())
                .projectId(updateReqVO.getProjectId())
                .remark(updateReqVO.getRemark())
                .build();
        workspaceFileMapper.updateById(updateObj);
    }

    @Override
    public void deleteWorkspaceFile(Long id) {
        // 校验存在
        validateWorkspaceFileExists(id);
        // 删除
        workspaceFileMapper.deleteById(id);
    }

    private void validateWorkspaceFileExists(Long id) {
        if (workspaceFileMapper.selectById(id) == null) {
            throw new ServiceException(404, "工作台文件不存在");
        }
    }

    @Override
    public WorkspaceFileDO getWorkspaceFile(Long id) {
        return workspaceFileMapper.selectById(id);
    }

    @Override
    public PageResult<WorkspaceFileDO> getWorkspaceFilePage(WorkspaceFilePageReqVO pageReqVO) {
        return workspaceFileMapper.selectPage(pageReqVO);
    }

    @Override
    public Long duplicateWorkspaceFile(Long id) {
        // 校验存在
        WorkspaceFileDO original = validateWorkspaceFileExists2(id);
        
        // 创建副本
        WorkspaceFileDO duplicate = WorkspaceFileDO.builder()
                .name(original.getName() + " 副本")
                .type(original.getType())
                .thumbnail(original.getThumbnail())
                .content(original.getContent())
                .category(original.getCategory())
                .starred(false) // 副本默认不收藏
                .projectId(original.getProjectId())
                .userId(SecurityFrameworkUtils.getLoginUserId())
                .teamId(original.getTeamId())
                .status(1)
                .remark(original.getRemark())
                .build();
        workspaceFileMapper.insert(duplicate);
        return duplicate.getId();
    }

    @Override
    public void starWorkspaceFile(Long id, Boolean starred) {
        // 校验存在
        validateWorkspaceFileExists(id);
        // 更新收藏状态
        WorkspaceFileDO updateObj = WorkspaceFileDO.builder()
                .id(id)
                .starred(starred)
                .build();
        workspaceFileMapper.updateById(updateObj);
    }

    @Override
    public void moveToTrash(Long id) {
        // 校验存在
        validateWorkspaceFileExists(id);
        // 更新状态为回收站
        WorkspaceFileDO updateObj = WorkspaceFileDO.builder()
                .id(id)
                .status(0) // 0表示回收站
                .build();
        workspaceFileMapper.updateById(updateObj);
    }

    @Override
    public void restoreFromTrash(Long id) {
        // 校验存在
        validateWorkspaceFileExists(id);
        // 恢复状态
        WorkspaceFileDO updateObj = WorkspaceFileDO.builder()
                .id(id)
                .status(1) // 1表示正常
                .build();
        workspaceFileMapper.updateById(updateObj);
    }

    private WorkspaceFileDO validateWorkspaceFileExists2(Long id) {
        WorkspaceFileDO file = workspaceFileMapper.selectById(id);
        if (file == null) {
            throw new ServiceException(404, "工作台文件不存在");
        }
        return file;
    }

} 