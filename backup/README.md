# 数据库备份目录

## 目录说明

此目录用于存储TunnelManagement项目的数据库备份文件和相关日志。

## 目录结构

```
backup/
└── database/
    ├── full/           # 完整备份文件
    │   ├── 202506/     # 按年月组织的备份文件
    │   │   └── *.sql.gz
    │   └── latest_full_backup.sql.gz -> 指向最新备份的符号链接
    ├── incremental/    # 增量备份文件（如果启用）
    ├── safety/         # 恢复前的安全备份
    └── logs/           # 备份和恢复操作日志
        ├── backup_*.log
        ├── restore_*.log
        ├── cron_backup.log
        └── last_full_backup_time
```

## Git版本控制策略

为了平衡版本控制和存储效率，我们采用以下策略：

### ✅ 包含在Git中
- 📁 目录结构（通过.gitkeep文件保留）
- 📄 配置文件和脚本
- 📝 小型日志文件（用于参考）
- ⏰ 时间戳文件（last_full_backup_time）

### ❌ 排除在Git外
- 💾 实际备份文件（*.sql.gz）- 文件过大，不适合版本控制
- 📋 详细日志文件（*.log）- 会快速增长

## 使用说明

### 本地开发
在本地开发环境中，备份文件会正常生成和存储在此目录中。

### 版本控制
- 目录结构会被保留在Git中
- 新环境克隆代码后，备份脚本会自动使用这些目录
- 实际备份文件需要通过其他方式传输（如打包脚本）

### 生产环境
1. 克隆代码后，目录结构已就绪
2. 运行备份脚本会自动创建备份文件
3. 设置定时任务进行自动备份

## 相关脚本

- `script/shell/backup_database.sh` - 数据库备份脚本
- `script/shell/restore_database.sh` - 数据库恢复脚本
- `script/shell/setup_backup_cron.sh` - 定时备份设置脚本
- `script/shell/package_for_windows.sh` - Windows恢复包打包脚本

## 注意事项

⚠️ **重要提醒**
- 备份文件包含敏感数据，请妥善保管
- 不要将实际备份文件提交到公共Git仓库
- 定期清理过期的备份文件以节省空间
- 生产环境建议配置异地备份

## 技术支持

详细的备份和恢复指南请参考：`docs/database-management.md` 