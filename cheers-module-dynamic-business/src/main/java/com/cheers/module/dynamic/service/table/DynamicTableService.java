package com.cheers.module.dynamic.service.table;

import com.cheers.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.module.dynamic.dal.dataobject.model.BusinessModelDO;

import java.util.List;

/**
 * 动态表结构管理服务
 */
public interface DynamicTableService {

    /**
     * 创建数据表
     *
     * @param model 业务模型
     * @param fields 字段定义列表
     */
    void createTable(BusinessModelDO model, List<FieldDefinitionDO> fields);

    /**
     * 更新数据表结构
     *
     * @param model 业务模型
     * @param fields 字段定义列表
     * @param oldFields 原字段定义列表
     */
    void updateTable(BusinessModelDO model, List<FieldDefinitionDO> fields, List<FieldDefinitionDO> oldFields);

    /**
     * 删除数据表
     *
     * @param model 业务模型
     */
    void dropTable(BusinessModelDO model);

    /**
     * 重命名数据表
     *
     * @param model 业务模型
     * @param newTableName 新表名
     */
    void renameTable(BusinessModelDO model, String newTableName);

    /**
     * 备份数据表
     *
     * @param model 业务模型
     * @return 备份文件路径
     */
    String backupTable(BusinessModelDO model);

    /**
     * 恢复数据表
     *
     * @param model 业务模型
     * @param backupPath 备份文件路径
     */
    void restoreTable(BusinessModelDO model, String backupPath);

    /**
     * 迁移数据
     *
     * @param sourceModel 源业务模型
     * @param targetModel 目标业务模型
     * @param fieldMapping 字段映射
     */
    void migrateData(BusinessModelDO sourceModel, BusinessModelDO targetModel, String fieldMapping);

    /**
     * 验证表名是否可用
     *
     * @param tableName 表名
     * @return 是否可用
     */
    boolean validateTableName(String tableName);

    /**
     * 获取表结构信息
     *
     * @param model 业务模型
     * @return 表结构信息
     */
    String getTableSchema(BusinessModelDO model);

    /**
     * 获取表大小信息
     *
     * @param model 业务模型
     * @return 表大小（单位：字节）
     */
    Long getTableSize(BusinessModelDO model);
} 