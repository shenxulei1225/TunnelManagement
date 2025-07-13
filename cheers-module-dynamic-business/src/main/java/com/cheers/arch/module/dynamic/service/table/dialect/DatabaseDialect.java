package com.cheers.arch.module.dynamic.service.table.dialect;

import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;

import java.util.List;

/**
 * 数据库方言接口
 */
public interface DatabaseDialect {
    
    /**
     * 获取数据库类型
     */
    String getDatabaseType();
    
    /**
     * 验证表名是否有效
     */
    boolean isValidTableName(String tableName);
    
    /**
     * 生成建表SQL
     */
    String generateCreateTableSQL(String tableName, List<FieldDefinitionDO> fields);
    
    /**
     * 生成添加字段SQL
     */
    String generateAddColumnSQL(String tableName, FieldDefinitionDO field);
    
    /**
     * 生成修改字段SQL
     */
    String generateModifyColumnSQL(String tableName, FieldDefinitionDO field);
    
    /**
     * 生成删除字段SQL
     */
    String generateDropColumnSQL(String tableName, String columnName);
    
    /**
     * 生成删除表SQL
     */
    String generateDropTableSQL(String tableName);
    
    /**
     * 生成重命名表SQL
     */
    String generateRenameTableSQL(String oldTableName, String newTableName);
    
    /**
     * 生成备份表SQL
     */
    String generateBackupTableSQL(String tableName, String backupTableName);
    
    /**
     * 生成从备份恢复表SQL
     */
    String generateRestoreFromBackupSQL(String tableName, String backupTableName);
    
    /**
     * 生成数据迁移SQL
     */
    String generateDataMigrationSQL(String sourceTable, String targetTable, String condition);
    
    /**
     * 生成显示表结构SQL
     */
    String generateShowTableSchemaSQL(String tableName);
    
    /**
     * 生成表大小查询SQL
     */
    String generateTableSizeSQL(String tableName);
} 