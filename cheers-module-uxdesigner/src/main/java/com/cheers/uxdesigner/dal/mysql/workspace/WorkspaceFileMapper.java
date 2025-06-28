package com.cheers.uxdesigner.dal.mysql.workspace;

import com.cheers.uxdesigner.controller.admin.workspace.vo.WorkspaceFilePageReqVO;
import com.cheers.uxdesigner.dal.dataobject.workspace.WorkspaceFileDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作台文件 Mapper
 *
 * @author UX Designer
 */
@Mapper
public interface WorkspaceFileMapper extends BaseMapperX<WorkspaceFileDO> {

    default PageResult<WorkspaceFileDO> selectPage(WorkspaceFilePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkspaceFileDO>()
                .likeIfPresent(WorkspaceFileDO::getName, reqVO.getName())
                .eqIfPresent(WorkspaceFileDO::getType, reqVO.getType())
                .eqIfPresent(WorkspaceFileDO::getCategory, reqVO.getCategory())
                .eqIfPresent(WorkspaceFileDO::getStarred, reqVO.getStarred())
                .eqIfPresent(WorkspaceFileDO::getUserId, reqVO.getUserId())
                .eqIfPresent(WorkspaceFileDO::getTeamId, reqVO.getTeamId())
                .eqIfPresent(WorkspaceFileDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WorkspaceFileDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkspaceFileDO::getId));
    }

} 