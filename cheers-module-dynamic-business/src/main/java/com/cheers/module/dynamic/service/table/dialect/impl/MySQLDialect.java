package com.cheers.module.dynamic.service.table.dialect.impl;

import com.cheers.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.module.dynamic.enums.field.FieldTypeEnum;
import com.cheers.module.dynamic.service.table.dialect.DatabaseDialect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * MySQL方言实现
 */
@Component
public class MySQLDialect implements DatabaseDialect {

    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_]*$");
    private static final int MAX_TABLE_NAME_LENGTH = 64;

    @Override
    public String generateCreateTableSQL(String tableName, List<FieldDefinitionDO> fields) {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ").append(tableName).append(" (\n");
        
        // 添加ID字段
        sql.append("  id BIGINT NOT NULL AUTO_INCREMENT,\n");
        
        // 添加业务字段
        for (FieldDefinitionDO field : fields) {
            sql.append("  ").append(field.getCode())
               .append(" ").append(getColumnTypeSQL(field));
            
            // 添加是否必填
            if (Boolean.TRUE.equals(field.getRequired())) {
                sql.append(" NOT NULL");
            }
            
            // 添加默认值
            if (StringUtils.isNotEmpty(field.getDefaultValue())) {
                sql.append(" DEFAULT ").append(formatDefaultValue(field));
            }
            
            sql.append(",\n");
        }
        
        // 添加基础字段
        sql.append("  creator VARCHAR(64),\n")
           .append("  create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,\n")
           .append("  updater VARCHAR(64),\n")
           .append("  update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n")
           .append("  deleted TINYINT NOT NULL DEFAULT 0,\n")
           .append("  tenant_id BIGINT NOT NULL,\n");
        
        // 添加主键
        sql.append("  PRIMARY KEY (id)\n");
        
        sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;");
        
        return sql.toString();
    }

    @Override
    public String generateAddColumnSQL(String tableName, FieldDefinitionDO field) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName)
           .append(" ADD COLUMN ").append(field.getCode())
           .append(" ").append(getColumnTypeSQL(field));
        
        if (Boolean.TRUE.equals(field.getRequired())) {
            sql.append(" NOT NULL");
        }
        
        if (StringUtils.isNotEmpty(field.getDefaultValue())) {
            sql.append(" DEFAULT ").append(formatDefaultValue(field));
        }
        
        return sql.toString();
    }

    @Override
    public String generateModifyColumnSQL(String tableName, FieldDefinitionDO oldField, FieldDefinitionDO newField) {
        StringBuilder sql = new StringBuilder();
        sql.append("ALTER TABLE ").append(tableName)
           .append(" MODIFY COLUMN ").append(newField.getCode())
           .append(" ").append(getColumnTypeSQL(newField));
        
        if (Boolean.TRUE.equals(newField.getRequired())) {
            sql.append(" NOT NULL");
        }
        
        if (StringUtils.isNotEmpty(newField.getDefaultValue())) {
            sql.append(" DEFAULT ").append(formatDefaultValue(newField));
        }
        
        return sql.toString();
    }

    @Override
    public String generateDropColumnSQL(String tableName, FieldDefinitionDO field) {
        return "ALTER TABLE " + tableName + " DROP COLUMN " + field.getCode();
    }

    @Override
    public String generateRenameTableSQL(String oldTableName, String newTableName) {
        return "RENAME TABLE " + oldTableName + " TO " + newTableName;
    }

    @Override
    public String generateDropTableSQL(String tableName) {
        return "DROP TABLE IF EXISTS " + tableName;
    }

    @Override
    public String generateBackupTableSQL(String tableName, String backupTableName) {
        return "CREATE TABLE " + backupTableName + " LIKE " + tableName + ";\n" +
               "INSERT INTO " + backupTableName + " SELECT * FROM " + tableName;
    }

    @Override
    public String generateRestoreFromBackupSQL(String tableName, String backupTableName) {
        return "TRUNCATE TABLE " + tableName + ";\n" +
               "INSERT INTO " + tableName + " SELECT * FROM " + backupTableName;
    }

    @Override
    public String generateDataMigrationSQL(String sourceTable, String targetTable, String fieldMapping) {
        // TODO: 根据字段映射生成数据迁移SQL
        return "";
    }

    @Override
    public String getColumnTypeSQL(FieldDefinitionDO field) {
        if (field.getType() == null) {
            return "VARCHAR(255)";
        }
        
        switch (field.getType()) {
            case TEXT:
                return field.getLength() != null && field.getLength() > 0 
                       ? "VARCHAR(" + field.getLength() + ")" 
                       : "VARCHAR(255)";
            case TEXTAREA:
                return "TEXT";
            case NUMBER:
                return "INT";
            case DECIMAL:
                return field.getPrecision() != null 
                       ? "DECIMAL(" + field.getLength() + "," + field.getPrecision() + ")"
                       : "DECIMAL(10,2)";
            case DATE:
                return "DATE";
            case DATETIME:
                return "DATETIME";
            case TIME:
                return "TIME";
            case RICH_TEXT:
                return "LONGTEXT";
            default:
                return "VARCHAR(255)";
        }
    }

    @Override
    public String generateShowTableSchemaSQL(String tableName) {
        return "SHOW CREATE TABLE " + tableName;
    }

    @Override
    public String generateTableSizeSQL(String tableName) {
        return "SELECT " +
               "ROUND(((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024), 2) AS size_in_mb " +
               "FROM information_schema.TABLES " +
               "WHERE TABLE_SCHEMA = DATABASE() " +
               "AND TABLE_NAME = '" + tableName + "'";
    }

    @Override
    public boolean isValidTableName(String tableName) {
        return tableName != null &&
               tableName.length() <= MAX_TABLE_NAME_LENGTH &&
               TABLE_NAME_PATTERN.matcher(tableName).matches();
    }

    @Override
    public String getDatabaseType() {
        return "MySQL";
    }

    private String formatDefaultValue(FieldDefinitionDO field) {
        if (field.getDefaultValue() == null) {
            return null;
        }

        switch (field.getType()) {
            case TEXT:
            case TEXTAREA:
            case TIME:
            case RICH_TEXT:
                return "'" + field.getDefaultValue() + "'";
            case DATE:
                return "'" + field.getDefaultValue() + "'";
            case DATETIME:
                return "'" + field.getDefaultValue() + "'";
            case NUMBER:
            case DECIMAL:
                return field.getDefaultValue();
            default:
                return "'" + field.getDefaultValue() + "'";
        }
    }
} 