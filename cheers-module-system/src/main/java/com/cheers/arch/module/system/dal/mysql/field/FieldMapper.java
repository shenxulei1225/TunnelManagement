package com.cheers.arch.module.system.dal.mysql.field;

import java.util.List;

import com.cheers.arch.framework.common.pojo.PageResult;
import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldExportReqVO;
import com.cheers.arch.module.system.controller.admin.field.vo.FieldPageReqVO;
import com.cheers.arch.module.system.dal.dataobject.field.FieldDO;

import org.apache.ibatis.annotations.Mapper;

/**
 * 字段 Mapper
 */
@Mapper
public interface FieldMapper extends BaseMapperX<FieldDO> {

    /**
     * 获得字段分页
     *
     * @param pageReqVO 查询条件
     * @return 字段分页
     */
    default PageResult<FieldDO> selectFieldPage(FieldPageReqVO pageReqVO) {
        LambdaQueryWrapperX<FieldDO> queryWrapper = new LambdaQueryWrapperX<FieldDO>()
                .likeIfPresent(FieldDO::getFieldCode, pageReqVO.getFieldCode())
                .likeIfPresent(FieldDO::getFieldName, pageReqVO.getFieldName())
                .likeIfPresent(FieldDO::getDisplay, pageReqVO.getDisplay())
                .eqIfPresent(FieldDO::getFieldType, pageReqVO.getFieldType())
                .eqIfPresent(FieldDO::getIsCustom, pageReqVO.getIsCustom())
                .eqIfPresent(FieldDO::getStatus, pageReqVO.getStatus())
                .betweenIfPresent(FieldDO::getCreateTime, pageReqVO.getCreateTimeBegin(), pageReqVO.getCreateTimeEnd());
        
        // 如果指定了分类ID，需要通过关联表查询
        if (pageReqVO.getCategoryId() != null) {
            queryWrapper.exists("SELECT 1 FROM system_field_category_rel r WHERE r.field_id = system_field.id AND r.category_id = " + pageReqVO.getCategoryId() + " AND r.deleted = 0");
        }
        
        return selectPage(pageReqVO, queryWrapper
                .orderByAsc(FieldDO::getSort)
                .orderByDesc(FieldDO::getId));
    }

    /**
     * 获得字段列表
     *
     * @param exportReqVO 查询条件
     * @return 字段列表
     */
    default List<FieldDO> selectFieldList(FieldExportReqVO exportReqVO) {
        return selectList(new LambdaQueryWrapperX<FieldDO>()
                .likeIfPresent(FieldDO::getFieldCode, exportReqVO.getFieldCode())
                .likeIfPresent(FieldDO::getFieldName, exportReqVO.getFieldName())
                .likeIfPresent(FieldDO::getDisplay, exportReqVO.getDisplay())
                .eqIfPresent(FieldDO::getFieldType, exportReqVO.getFieldType())
                .eqIfPresent(FieldDO::getIsCustom, exportReqVO.getIsCustom())
                .eqIfPresent(FieldDO::getStatus, exportReqVO.getStatus())
                .betweenIfPresent(FieldDO::getCreateTime, exportReqVO.getCreateTimeBegin(), exportReqVO.getCreateTimeEnd())
                .orderByAsc(FieldDO::getSort)
                .orderByDesc(FieldDO::getId));
    }

    /**
     * 根据ID列表批量查询字段
     *
     * @param ids ID列表
     * @return 字段列表
     */
    default List<FieldDO> selectFieldListByIds(java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return java.util.Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<FieldDO>()
                .in(FieldDO::getId, ids));
    }

} 