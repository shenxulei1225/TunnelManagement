package com.cheers.arch.module.uxdesigner.dal.mysql.workspace;

import com.cheers.arch.module.uxdesigner.dal.dataobject.workspace.WorkspaceProjectDO;
import com.cheers.arch.module.uxdesigner.controller.admin.workspace.vo.WorkspaceProjectPageReqVO;
import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作台项目 Mapper
 *
 * @author UX Designer
 */
@Mapper
public interface WorkspaceProjectMapper extends BaseMapperX<WorkspaceProjectDO> {

    default PageResult<WorkspaceProjectDO> selectPage(WorkspaceProjectPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkspaceProjectDO>()
                .likeIfPresent(WorkspaceProjectDO::getName, reqVO.getName())
                .eqIfPresent(WorkspaceProjectDO::getType, reqVO.getType())
                .eqIfPresent(WorkspaceProjectDO::getStarred, reqVO.getStarred())
                .eqIfPresent(WorkspaceProjectDO::getUserId, reqVO.getUserId())
                .eqIfPresent(WorkspaceProjectDO::getTeamId, reqVO.getTeamId())
                .eqIfPresent(WorkspaceProjectDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WorkspaceProjectDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkspaceProjectDO::getId));
    }

} 