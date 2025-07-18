package com.cheers.arch.module.dynamic.dal.mysql.behavior;

import java.util.List;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.dynamic.dal.dataobject.behavior.DynamicUserBehaviorDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 动态用户行为 Mapper
 */
@Mapper
public interface DynamicUserBehaviorMapper extends BaseMapperX<DynamicUserBehaviorDO> {

    /**
     * 根据用户ID和业务类型查询用户行为
     */
    default List<DynamicUserBehaviorDO> selectByUserIdAndBusinessType(Long userId, String businessType) {
        return selectList(new LambdaQueryWrapperX<DynamicUserBehaviorDO>()
                .eq(DynamicUserBehaviorDO::getUserId, userId)
                .eq(DynamicUserBehaviorDO::getBusinessType, businessType));
    }

    /**
     * 根据业务类型查询用户行为统计
     */
    default List<DynamicUserBehaviorDO> selectByBusinessType(String businessType) {
        return selectList(DynamicUserBehaviorDO::getBusinessType, businessType);
    }
} 