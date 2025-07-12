package com.cheers.arch.module.dynamic.service.table.impl;

import com.cheers.framework.common.exception.ServiceException;
import com.cheers.arch.module.dynamic.dal.dataobject.field.FieldDefinitionDO;
import com.cheers.arch.module.dynamic.dal.dataobject.model.BusinessModelDO;
import com.cheers.arch.module.dynamic.service.table.DynamicTableService;
import com.cheers.arch.module.dynamic.service.table.dialect.DatabaseDialect;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 动态表结构管理服务实现类
 */
@Service
@Slf4j
public class DynamicTableServiceImpl implements DynamicTableService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private DatabaseDialect databaseDialect;

    private static final String BACKUP_PATH = "backup/database/dynamic/";
    private static final DateTimeFormatter BACKUP_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTable(BusinessModelDO model, List<FieldDefinitionDO> fields) {
        // 校验表名
        if (!databaseDialect.isValidTableName(model.getTableName())) {
            throw new ServiceException("表名不合法");
        }

        try {
            // 生成建表SQL
            String sql = databaseDialect.generateCreateTableSQL(model.getTableName(), fields);
            log.info("[createTable][开始创建表({})，SQL语句为({})]", model.getTableName(), sql);

            // 执行建表SQL
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("[createTable][创建表({})失败]", model.getTableName(), e);
            throw new ServiceException("创建表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTable(BusinessModelDO model, List<FieldDefinitionDO> fields, List<FieldDefinitionDO> oldFields) {
        try {
            // 1. 找出需要新增的字段
            for (FieldDefinitionDO field : fields) {
                if (oldFields.stream().noneMatch(old -> old.getCode().equals(field.getCode()))) {
                    String sql = databaseDialect.generateAddColumnSQL(model.getTableName(), field);
                    log.info("[updateTable][表({})新增字段，SQL语句为({})]", model.getTableName(), sql);
                    jdbcTemplate.execute(sql);
                }
            }

            // 2. 找出需要修改的字段
            for (FieldDefinitionDO field : fields) {
                oldFields.stream()
                        .filter(old -> old.getCode().equals(field.getCode()))
                        .findFirst()
                        .ifPresent(old -> {
                            if (!isSameFieldDefinition(old, field)) {
                                String sql = databaseDialect.generateModifyColumnSQL(model.getTableName(), old, field);
                                log.info("[updateTable][表({})修改字段，SQL语句为({})]", model.getTableName(), sql);
                                jdbcTemplate.execute(sql);
                            }
                        });
            }

            // 3. 找出需要删除的字段
            for (FieldDefinitionDO old : oldFields) {
                if (fields.stream().noneMatch(field -> field.getCode().equals(old.getCode()))) {
                    String sql = databaseDialect.generateDropColumnSQL(model.getTableName(), old);
                    log.info("[updateTable][表({})删除字段，SQL语句为({})]", model.getTableName(), sql);
                    jdbcTemplate.execute(sql);
                }
            }
        } catch (Exception e) {
            log.error("[updateTable][更新表({})结构失败]", model.getTableName(), e);
            throw new ServiceException("更新表结构失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dropTable(BusinessModelDO model) {
        try {
            String sql = databaseDialect.generateDropTableSQL(model.getTableName());
            log.info("[dropTable][开始删除表({})，SQL语句为({})]", model.getTableName(), sql);
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("[dropTable][删除表({})失败]", model.getTableName(), e);
            throw new ServiceException("删除表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void renameTable(BusinessModelDO model, String newTableName) {
        // 校验新表名
        if (!databaseDialect.isValidTableName(newTableName)) {
            throw new ServiceException("新表名不合法");
        }

        try {
            String sql = databaseDialect.generateRenameTableSQL(model.getTableName(), newTableName);
            log.info("[renameTable][开始重命名表({} -> {})，SQL语句为({})]", model.getTableName(), newTableName, sql);
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("[renameTable][重命名表({} -> {})失败]", model.getTableName(), newTableName, e);
            throw new ServiceException("重命名表失败");
        }
    }

    @Override
    public String backupTable(BusinessModelDO model) {
        try {
            // 生成备份表名
            String backupTableName = generateBackupTableName(model.getTableName());
            
            // 生成备份SQL
            String sql = databaseDialect.generateBackupTableSQL(model.getTableName(), backupTableName);
            log.info("[backupTable][开始备份表({} -> {})，SQL语句为({})]", model.getTableName(), backupTableName, sql);
            
            // 执行备份
            jdbcTemplate.execute(sql);
            
            // 返回备份表名
            return backupTableName;
        } catch (Exception e) {
            log.error("[backupTable][备份表({})失败]", model.getTableName(), e);
            throw new ServiceException("备份表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreTable(BusinessModelDO model, String backupPath) {
        try {
            String sql = databaseDialect.generateRestoreFromBackupSQL(model.getTableName(), backupPath);
            log.info("[restoreTable][开始恢复表({})，SQL语句为({})]", model.getTableName(), sql);
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("[restoreTable][恢复表({})失败]", model.getTableName(), e);
            throw new ServiceException("恢复表失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void migrateData(BusinessModelDO sourceModel, BusinessModelDO targetModel, String fieldMapping) {
        try {
            String sql = databaseDialect.generateDataMigrationSQL(
                    sourceModel.getTableName(), targetModel.getTableName(), fieldMapping);
            log.info("[migrateData][开始迁移数据({} -> {})，SQL语句为({})]",
                    sourceModel.getTableName(), targetModel.getTableName(), sql);
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            log.error("[migrateData][迁移数据({} -> {})失败]",
                    sourceModel.getTableName(), targetModel.getTableName(), e);
            throw new ServiceException("迁移数据失败");
        }
    }

    @Override
    public boolean validateTableName(String tableName) {
        return databaseDialect.isValidTableName(tableName);
    }

    @Override
    public String getTableSchema(BusinessModelDO model) {
        try {
            String sql = databaseDialect.generateShowTableSchemaSQL(model.getTableName());
            return jdbcTemplate.queryForObject(sql, String.class);
        } catch (Exception e) {
            log.error("[getTableSchema][获取表({})结构失败]", model.getTableName(), e);
            throw new ServiceException("获取表结构失败");
        }
    }

    @Override
    public Long getTableSize(BusinessModelDO model) {
        try {
            String sql = databaseDialect.generateTableSizeSQL(model.getTableName());
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            log.error("[getTableSize][获取表({})大小失败]", model.getTableName(), e);
            throw new ServiceException("获取表大小失败");
        }
    }

    private String generateBackupTableName(String tableName) {
        return tableName + "_backup_" + LocalDateTime.now().format(BACKUP_TIME_FORMATTER);
    }

    private boolean isSameFieldDefinition(FieldDefinitionDO field1, FieldDefinitionDO field2) {
        return field1.getType() == field2.getType() &&
               field1.getLength().equals(field2.getLength()) &&
               field1.getPrecision().equals(field2.getPrecision()) &&
               field1.getRequired().equals(field2.getRequired()) &&
               StringUtils.equals(field1.getDefaultValue(), field2.getDefaultValue());
    }
} 