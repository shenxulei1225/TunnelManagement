package com.cheers.arch.module.dynamic.service.table;

import com.cheers.arch.module.dynamic.dal.dataobject.field.DynamicFieldDefinitionDO;
import com.cheers.arch.module.dynamic.dal.dataobject.model.DynamicBusinessModelDO;

import java.util.List;

/**
 * 动态表服务接口
 * 负责动态创建、更新、删除数据库表
 */
public interface DynamicTableService {

    /**
     * 创建数据表
     *
     * @param model 业务模型
     * @param fields 字段定义列表
     */
    void createTable(DynamicBusinessModelDO model, List<DynamicFieldDefinitionDO> fields);

    /**
     * 更新数据表结构
     *
     * @param model 业务模型
     * @param fields 新的字段定义列表
     * @param oldFields 旧的字段定义列表
     */
    void updateTable(DynamicBusinessModelDO model, List<DynamicFieldDefinitionDO> fields, List<DynamicFieldDefinitionDO> oldFields);

    /**
     * 删除数据表
     *
     * @param model 业务模型
     */
    void dropTable(DynamicBusinessModelDO model);

    /**
     * 重命名数据表
     *
     * @param model 业务模型
     * @param newTableName 新表名
     */
    void renameTable(DynamicBusinessModelDO model, String newTableName);

    /**
     * 备份数据表
     *
     * @param model 业务模型
     * @return 备份文件路径
     */
    String backupTable(DynamicBusinessModelDO model);

    /**
     * 恢复数据表
     *
     * @param model 业务模型
     * @param backupPath 备份文件路径
     */
    void restoreTable(DynamicBusinessModelDO model, String backupPath);

    /**
     * 数据迁移
     *
     * @param sourceModel 源业务模型
     * @param targetModel 目标业务模型
     * @param fieldMapping 字段映射关系（JSON格式）
     */
    void migrateData(DynamicBusinessModelDO sourceModel, DynamicBusinessModelDO targetModel, String fieldMapping);

    /**
     * 获取表结构信息
     *
     * @param model 业务模型
     * @return 表结构信息（JSON格式）
     */
    String getTableSchema(DynamicBusinessModelDO model);

    /**
     * 获取表大小
     *
     * @param model 业务模型
     * @return 表大小（字节）
     */
    Long getTableSize(DynamicBusinessModelDO model);
} 