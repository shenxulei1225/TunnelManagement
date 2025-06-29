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
        try {
            System.out.println("DEBUG Service: 开始查询项目分页，参数：" + pageReqVO);
            PageResult<WorkspaceProjectDO> result = projectMapper.selectPage(pageReqVO);
            System.out.println("DEBUG Service: 查询结果 - 总数：" + result.getTotal());
            if (result.getList() != null && !result.getList().isEmpty()) {
                System.out.println("DEBUG Service: 查询到 " + result.getList().size() + " 个项目");
                for (int i = 0; i < Math.min(result.getList().size(), 2); i++) {
                    WorkspaceProjectDO project = result.getList().get(i);
                    System.out.println("DEBUG Service: 项目" + (i+1) + " - ID:" + project.getId() + 
                                     ", Name:" + project.getName() + 
                                     ", Starred:" + project.getStarred() +
                                     ", Status:" + project.getStatus());
                }
            } else {
                System.out.println("DEBUG Service: 查询结果为空");
            }
            return result;
        } catch (Exception e) {
            System.err.println("DEBUG Service: 查询异常：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public Long duplicateProject(Long id) {
        // 校验存在
        WorkspaceProjectDO project = validateProjectExists(id);
        
        // 复制项目
        WorkspaceProjectDO newProject = BeanUtils.toBean(project, WorkspaceProjectDO.class);
        newProject.setId(null);
        newProject.setName(project.getName() + " 副本");
        newProject.setCreateTime(null);
        newProject.setUpdateTime(null);
        
        projectMapper.insert(newProject);
        return newProject.getId();
    }

    @Override
    public void starProject(Long id, Boolean starred) {
        // 校验存在
        validateProjectExists(id);
        
        // 更新收藏状态
        WorkspaceProjectDO updateObj = new WorkspaceProjectDO();
        updateObj.setId(id);
        updateObj.setStarred(starred);
        projectMapper.updateById(updateObj);
    }

    @Override
    public void moveProjectToTrash(Long id) {
        // 校验存在
        validateProjectExists(id);
        
        // 更新状态为回收站
        WorkspaceProjectDO updateObj = new WorkspaceProjectDO();
        updateObj.setId(id);
        updateObj.setStatus(0); // 0表示回收站
        projectMapper.updateById(updateObj);
    }

    @Override
    public void restoreProjectFromTrash(Long id) {
        // 校验存在
        validateProjectExists(id);
        
        // 恢复状态为正常
        WorkspaceProjectDO updateObj = new WorkspaceProjectDO();
        updateObj.setId(id);
        updateObj.setStatus(1); // 1表示正常
        projectMapper.updateById(updateObj);
    }

}