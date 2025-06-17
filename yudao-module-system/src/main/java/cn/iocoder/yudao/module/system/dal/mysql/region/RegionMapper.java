package cn.iocoder.yudao.module.system.dal.mysql.region;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.system.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.system.controller.admin.region.vo.*;

/**
 * 区域 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RegionMapper extends BaseMapperX<RegionDO> {

    default List<RegionDO> selectList(RegionListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RegionDO>()
                .likeIfPresent(RegionDO::getName, reqVO.getName())
                .eqIfPresent(RegionDO::getParentId, reqVO.getParentId())
                .eqIfPresent(RegionDO::getSort, reqVO.getSort())
                .eqIfPresent(RegionDO::getLeaderUserId, reqVO.getLeaderUserId())
                .eqIfPresent(RegionDO::getPhone, reqVO.getPhone())
                .eqIfPresent(RegionDO::getEmail, reqVO.getEmail())
                .eqIfPresent(RegionDO::getStatus, reqVO.getStatus())
                .eqIfPresent(RegionDO::getType, reqVO.getType())
                .likeIfPresent(RegionDO::getRoadSection, reqVO.getRoadSection())
                .likeIfPresent(RegionDO::getStartPosition, reqVO.getStartPosition())
                .likeIfPresent(RegionDO::getEndPosition, reqVO.getEndPosition())
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