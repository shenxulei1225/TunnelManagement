package com.cheers.arch.module.system.dal.mysql.region;

import java.util.*;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.module.system.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.cheers.arch.module.system.controller.admin.region.vo.*;

/**
 * 区域 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@ConditionalOnProperty(prefix = "system.region.mapper", name = "enabled", havingValue = "true", matchIfMissing = false)
public interface RegionMapper extends BaseMapperX<RegionDO> {

    default List<RegionDO> selectList(RegionListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RegionDO>()
                .likeIfPresent(RegionDO::getName, reqVO.getName())
                .eqIfPresent(RegionDO::getParentId, reqVO.getParentId())
                .eqIfPresent(RegionDO::getSort, reqVO.getSort())
                .eqIfPresent(RegionDO::getLeaderUserId, reqVO.getLeaderUserId())
                .eqIfPresent(RegionDO::getStatus, reqVO.getStatus())
                

                .betweenIfPresent(RegionDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(RegionDO::getSort));
    }

    default RegionDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(RegionDO::getParentId, parentId, RegionDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(RegionDO::getParentId, parentId);
    }

}