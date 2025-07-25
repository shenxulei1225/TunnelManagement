package com.cheers.arch.module.system.dal.mysql.domain;

import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.cheers.arch.module.system.controller.admin.domain.vo.DomainFieldRelRespVO;
import com.cheers.arch.module.system.dal.dataobject.domain.DomainFieldRelDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 领域模型字段关联 Mapper
 *
 * @author cheers
 */
@Mapper
public interface DomainFieldRelMapper extends BaseMapperX<DomainFieldRelDO> {

    default List<DomainFieldRelDO> selectByDomainId(Long domainId) {
        return selectList(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getDomainId, domainId)
                .orderByAsc(DomainFieldRelDO::getSort));
    }

    default List<DomainFieldRelDO> selectByFieldId(Long fieldId) {
        return selectList(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getFieldId, fieldId));
    }

    default DomainFieldRelDO selectByDomainIdAndFieldId(Long domainId, Long fieldId) {
        return selectOne(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getDomainId, domainId)
                .eq(DomainFieldRelDO::getFieldId, fieldId));
    }

    default void deleteByDomainId(Long domainId) {
        delete(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getDomainId, domainId));
    }

    default void deleteByFieldId(Long fieldId) {
        delete(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getFieldId, fieldId));
    }

    /**
     * 查询指定Domain下的Field关联信息（带Field详情）
     */
    @Select("""
        SELECT 
            rel.id,
            rel.domain_id,
            d.name as domain_name,
            d.code as domain_code,
            rel.field_id,
            f.field_label as field_name,
            f.field_key as field_code,
            f.value_type as field_type,
            rel.required,
            rel.sort,
            rel.remark,
            rel.create_time
        FROM system_domain_field_rel rel
        LEFT JOIN system_domain d ON rel.domain_id = d.id AND d.deleted = 0
        LEFT JOIN system_field_def f ON rel.field_id = f.id AND f.deleted = 0
        WHERE rel.domain_id = #{domainId} 
        AND rel.deleted = 0
        ORDER BY rel.sort ASC
    """)
    List<DomainFieldRelRespVO> selectFieldsByDomainId(@Param("domainId") Long domainId);

    /**
     * 查询指定Field下的Domain关联信息（带Domain详情）
     */
    @Select("""
        SELECT 
            rel.id,
            rel.domain_id,
            d.name as domain_name,
            d.code as domain_code,
            rel.field_id,
            f.field_label as field_name,
            f.field_key as field_code,
            f.value_type as field_type,
            rel.required,
            rel.sort,
            rel.remark,
            rel.create_time
        FROM system_domain_field_rel rel
        LEFT JOIN system_domain d ON rel.domain_id = d.id AND d.deleted = 0
        LEFT JOIN system_field_def f ON rel.field_id = f.id AND f.deleted = 0
        WHERE rel.field_id = #{fieldId} 
        AND rel.deleted = 0
        ORDER BY d.sort ASC, d.create_time ASC
    """)
    List<DomainFieldRelRespVO> selectDomainsByFieldId(@Param("fieldId") Long fieldId);

    /**
     * 统计指定领域的直接字段数量
     */
    @Select("SELECT COUNT(*) FROM system_domain_field_rel WHERE domain_id = #{domainId} AND deleted = 0")
    Integer countFieldsByDomainId(@Param("domainId") Long domainId);

    /**
     * 批量统计多个领域的直接字段数量
     */
    @Select("""
        <script>
        SELECT domain_id, COUNT(*) as field_count 
        FROM system_domain_field_rel 
        WHERE domain_id IN 
        <foreach collection="domainIds" item="domainId" open="(" close=")" separator=",">
            #{domainId}
        </foreach>
        AND deleted = 0
        GROUP BY domain_id
        </script>
        """)
    List<Map<String, Object>> countFieldsByDomainIds(@Param("domainIds") Collection<Long> domainIds);

    /**
     * 批量删除领域的所有字段关联关系
     */
    default void deleteByDomainIds(Collection<Long> domainIds) {
        if (domainIds == null || domainIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .in(DomainFieldRelDO::getDomainId, domainIds));
    }

    /**
     * 批量删除字段的所有领域关联关系
     */
    default void deleteByFieldIds(Collection<Long> fieldIds) {
        if (fieldIds == null || fieldIds.isEmpty()) {
            return;
        }
        delete(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .in(DomainFieldRelDO::getFieldId, fieldIds));
    }

    // ================ 对标Category的批量关联功能 ================

    /**
     * 获取字段在领域中的配置映射
     */
    @Select("""
        SELECT domain_id, required, sort, remark 
        FROM system_domain_field_rel 
        WHERE field_id = #{fieldId} AND deleted = 0
        ORDER BY sort ASC
        """)
    List<Map<String, Object>> selectFieldConfigByFieldId(@Param("fieldId") Long fieldId);

    /**
     * 获取领域中字段的配置映射
     */
    @Select("""
        SELECT field_id, required, sort, remark 
        FROM system_domain_field_rel 
        WHERE domain_id = #{domainId} AND deleted = 0
        ORDER BY sort ASC
        """)
    List<Map<String, Object>> selectFieldConfigByDomainId(@Param("domainId") Long domainId);

    /**
     * 获取领域中下一个排序值
     */
    @Select("SELECT COALESCE(MAX(sort), 0) + 1 FROM system_domain_field_rel WHERE domain_id = #{domainId} AND deleted = 0")
    Integer selectNextSortInDomain(@Param("domainId") Long domainId);

    /**
     * 批量插入领域字段关联
     */
    default void batchInsert(Collection<DomainFieldRelDO> relations) {
        if (relations == null || relations.isEmpty()) {
            return;
        }
        // 使用MyBatis Plus的批量插入
        for (DomainFieldRelDO rel : relations) {
            insert(rel);
        }
    }

    /**
     * 批量更新排序
     */
    default void batchUpdateSort(Long domainId, Map<Long, Integer> fieldSortMap) {
        if (fieldSortMap == null || fieldSortMap.isEmpty()) {
            return;
        }
        
        for (Map.Entry<Long, Integer> entry : fieldSortMap.entrySet()) {
            update(null, new LambdaUpdateWrapper<DomainFieldRelDO>()
                    .eq(DomainFieldRelDO::getDomainId, domainId)
                    .eq(DomainFieldRelDO::getFieldId, entry.getKey())
                    .set(DomainFieldRelDO::getSort, entry.getValue()));
        }
    }

    /**
     * 检查关联是否存在
     */
    default boolean exists(Long fieldId, Long domainId) {
        return selectCount(new LambdaQueryWrapperX<DomainFieldRelDO>()
                .eq(DomainFieldRelDO::getFieldId, fieldId)
                .eq(DomainFieldRelDO::getDomainId, domainId)) > 0;
    }

} 