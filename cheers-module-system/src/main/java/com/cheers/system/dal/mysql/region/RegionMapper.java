package com.cheers.system.dal.mysql.region;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.system.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 区域 Mapper
 *
 * @author 芋道源码
 */
@Mapper
@Component("cheersRegionMapper")
public interface RegionMapper extends BaseMapperX<RegionDO> {

    default List<RegionDO> selectListByConditions(String name, Long parentId, Integer sort, Integer status) {
        return selectList(new LambdaQueryWrapperX<RegionDO>()
                .likeIfPresent(RegionDO::getName, name)
                .eqIfPresent(RegionDO::getParentId, parentId)
                .eqIfPresent(RegionDO::getSort, sort)
                .eqIfPresent(RegionDO::getStatus, status)
                .orderByAsc(RegionDO::getSort));
    }

    default RegionDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(RegionDO::getParentId, parentId, RegionDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(RegionDO::getParentId, parentId);
    }

} 