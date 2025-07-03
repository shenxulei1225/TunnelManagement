package com.cheers.uxdesigner.service.workspace;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectCreateReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectPageReqVO;
import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectUpdateReqVO;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;
import com.cheers.uxdesigner.dal.mysql.workspace.WorkspaceProjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 工作台项目 Service 实现类
 *
 * @author UX Designer
 */
@Service
public class WorkspaceProjectServiceImpl implements WorkspaceProjectService {

    @Resource
    private WorkspaceProjectMapper projectMapper;

    @Override
    public Long createProject(WorkspaceProjectCreateReqVO createReqVO) {
        // 插入
        WorkspaceProjectDO project = BeanUtils.toBean(createReqVO, WorkspaceProjectDO.class);
        projectMapper.insert(project);
        // 返回
        return project.getId();
    }

    @Override
    public void updateProject(WorkspaceProjectUpdateReqVO updateReqVO) {
        // 校验存在
        validateProjectExists(updateReqVO.getId());
        // 更新
        WorkspaceProjectDO updateObj = BeanUtils.toBean(updateReqVO, WorkspaceProjectDO.class);
        projectMapper.updateById(updateObj);
    }

    @Override
    public void deleteProject(Long id) {
        // 校验存在
        validateProjectExists(id);
        // 删除
        projectMapper.deleteById(id);
    }

    private WorkspaceProjectDO validateProjectExists(Long id) {
        WorkspaceProjectDO project = projectMapper.selectById(id);
        if (project == null) {
            throw new RuntimeException("工作台项目不存在");
        }
        return project;
    }

    @Override
    public WorkspaceProjectDO getProject(Long id) {
        return projectMapper.selectById(id);
    }

    @Override
    public PageResult<WorkspaceProjectDO> getProjectPage(WorkspaceProjectPageReqVO pageReqVO) {
        return projectMapper.selectPage(pageReqVO);
    }

    @Override
    public Long duplicateProject(Long id) {
        // 校验项目存在
        WorkspaceProjectDO originalProject = validateProjectExists(id);
        
        // 创建副本
        WorkspaceProjectDO duplicateProject = BeanUtils.toBean(originalProject, WorkspaceProjectDO.class);
        duplicateProject.setId(null); // 清空ID，让数据库自动生成
        duplicateProject.setName(originalProject.getName() + " (副本)");
        duplicateProject.setStarred(false); // 副本默认不收藏
        duplicateProject.setCreateTime(LocalDateTime.now());
        duplicateProject.setUpdateTime(LocalDateTime.now());
        
        // 插入副本
        projectMapper.insert(duplicateProject);
        
        return duplicateProject.getId();
    }

    @Override
    public void starProject(Long id, Boolean starred) {
        // 校验存在
        validateProjectExists(id);
        
        // 更新收藏状态
        WorkspaceProjectDO updateObj = new WorkspaceProjectDO();
        updateObj.setId(id);
        updateObj.setStarred(starred);
        updateObj.setUpdateTime(LocalDateTime.now());
        
        projectMapper.updateById(updateObj);
    }


}