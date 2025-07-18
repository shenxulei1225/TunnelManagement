package com.cheers.arch.module.dynamic.dal.mysql.record;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cheers.arch.framework.mybatis.core.mapper.BaseMapperX;
import com.cheers.arch.framework.trees.mapper.TreeMapper;
import com.cheers.arch.module.dynamic.dal.dataobject.record.DynamicBusinessRecordDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 动态业务记录 Mapper
 * 继承TreeMapper，支持树形结构操作
 */
@Mapper
public interface DynamicBusinessRecordMapper extends BaseMapperX<DynamicBusinessRecordDO>, TreeMapper<DynamicBusinessRecordDO> {

    /**
     * 根据模型编码查询记录列表
     *
     * @param modelCode 模型编码
     * @return 记录列表
     */
    List<DynamicBusinessRecordDO> selectListByModelCode(@Param("modelCode") String modelCode);

    /**
     * 根据模型编码和状态查询记录列表
     *
     * @param modelCode 模型编码
     * @param status 状态
     * @return 记录列表
     */
    List<DynamicBusinessRecordDO> selectListByModelCodeAndStatus(@Param("modelCode") String modelCode, @Param("status") Integer status);

    /**
     * 根据模型编码统计记录数量
     *
     * @param modelCode 模型编码
     * @return 记录数量
     */
    Long selectCountByModelCode(@Param("modelCode") String modelCode);

    /**
     * 根据模型编码和状态统计记录数量
     *
     * @param modelCode 模型编码
     * @param status 状态
     * @return 记录数量
     */
    Long selectCountByModelCodeAndStatus(@Param("modelCode") String modelCode, @Param("status") Integer status);

    /**
     * 批量插入记录
     *
     * @param records 记录列表
     * @return 插入数量
     */
    int batchInsert(@Param("records") List<DynamicBusinessRecordDO> records);

    /**
     * 批量更新记录
     *
     * @param records 记录列表
     * @return 更新数量
     */
    int batchUpdate(@Param("records") List<DynamicBusinessRecordDO> records);

    /**
     * 根据模型编码和父记录ID查询子记录
     *
     * @param modelCode 模型编码
     * @param parentId 父记录ID
     * @return 子记录列表
     */
    List<DynamicBusinessRecordDO> selectChildrenByModelCodeAndParentId(@Param("modelCode") String modelCode, @Param("parentId") Long parentId);

    /**
     * 根据模型编码和记录ID查询所有子记录
     *
     * @param modelCode 模型编码
     * @param recordId 记录ID
     * @return 所有子记录列表
     */
    List<DynamicBusinessRecordDO> selectAllChildrenByModelCodeAndRecordId(@Param("modelCode") String modelCode, @Param("recordId") Long recordId);

    /**
     * 根据模型编码和记录ID查询所有父记录
     *
     * @param modelCode 模型编码
     * @param recordId 记录ID
     * @return 所有父记录列表
     */
    List<DynamicBusinessRecordDO> selectAllParentsByModelCodeAndRecordId(@Param("modelCode") String modelCode, @Param("recordId") Long recordId);

    /**
     * 根据模型编码查询根记录
     *
     * @param modelCode 模型编码
     * @return 根记录列表
     */
    List<DynamicBusinessRecordDO> selectRootRecordsByModelCode(@Param("modelCode") String modelCode);

    /**
     * 根据模型编码查询树形结构
     *
     * @param modelCode 模型编码
     * @return 树形记录列表
     */
    List<DynamicBusinessRecordDO> selectTreeByModelCode(@Param("modelCode") String modelCode);
} 