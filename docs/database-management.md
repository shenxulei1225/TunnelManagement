# TunnelManagement 数据库管理指南

## 概述

本项目提供了完整的数据库备份和恢复解决方案，包括自动化脚本、定时任务设置和安全恢复机制。

## 🔧 核心工具

### 1. 数据库备份脚本 (`script/shell/backup_database.sh`)

**功能特性：**
- ✅ MySQL完整备份
- ✅ 自动压缩（gzip）
- ✅ 按月份组织备份文件
- ✅ 自动清理过期备份
- ✅ 详细的日志记录
- ✅ 连接测试功能

**使用方法：**
```bash
# 执行完整备份
./script/shell/backup_database.sh full

# 清理过期备份
./script/shell/backup_database.sh cleanup

# 查看备份信息
./script/shell/backup_database.sh info

# 测试数据库连接
./script/shell/backup_database.sh test

# 查看帮助
./script/shell/backup_database.sh help
```

### 2. 数据库恢复脚本 (`script/shell/restore_database.sh`)

**功能特性：**
- ✅ 交互式恢复界面
- ✅ 自动安全备份（恢复前）
- ✅ 备份文件完整性验证
- ✅ 恢复失败自动回滚
- ✅ 恢复结果验证
- ✅ 多种恢复模式

**使用方法：**
```bash
# 交互式恢复（推荐）
./script/shell/restore_database.sh interactive

# 从最新备份恢复
./script/shell/restore_database.sh latest

# 从指定文件恢复
./script/shell/restore_database.sh file /path/to/backup.sql.gz

# 列出可用备份
./script/shell/restore_database.sh list

# 测试连接
./script/shell/restore_database.sh test
```

### 3. 定时备份设置脚本 (`script/shell/setup_backup_cron.sh`)

**功能特性：**
- ✅ 推荐备份策略
- ✅ 自定义定时任务
- ✅ Cron任务管理
- ✅ 备份脚本测试

**使用方法：**
```bash
# 设置推荐备份策略
./script/shell/setup_backup_cron.sh recommended

# 自定义备份任务
./script/shell/setup_backup_cron.sh custom

# 移除所有备份任务
./script/shell/setup_backup_cron.sh remove

# 查看当前任务
./script/shell/setup_backup_cron.sh show
```

## 📁 目录结构

```
backup/
└── database/
    ├── full/           # 完整备份文件
    │   ├── 202506/     # 按年月组织
    │   │   └── tunnel_management_full_20250627_235914.sql.gz
    │   └── latest_full_backup.sql.gz -> 最新备份的符号链接
    ├── safety/         # 恢复前安全备份
    └── logs/           # 操作日志
        ├── backup_*.log
        ├── restore_*.log
        └── cron_backup.log
```

## ⚙️ 配置说明

### 数据库连接配置

所有脚本中的数据库配置都需要根据实际环境修改：

```bash
# 数据库连接配置
DB_HOST="127.0.0.1"        # 数据库主机
DB_PORT="3306"             # 数据库端口
DB_USER="root"             # 数据库用户名
DB_PASSWORD="Coolhomer"    # 数据库密码
DB_NAME="tunnel_management" # 数据库名称
```

### 备份策略配置

```bash
# 保留策略（天数）
FULL_BACKUP_RETENTION_DAYS=30      # 完整备份保留30天
INCREMENTAL_BACKUP_RETENTION_DAYS=7 # 增量备份保留7天
LOG_RETENTION_DAYS=30               # 日志保留30天
```

## 🕒 推荐的备份策略

### 定时任务设置

```bash
# 每天凌晨2点执行完整备份
0 2 * * * cd /path/to/project && ./script/shell/backup_database.sh full

# 每周日凌晨1点清理过期备份
0 1 * * 0 cd /path/to/project && ./script/shell/backup_database.sh cleanup
```

### 备份频率建议

| 环境类型 | 备份频率 | 保留策略 | 说明 |
|---------|---------|---------|------|
| 生产环境 | 每天1次 | 30天 | 关键数据，必须可靠 |
| 测试环境 | 每周1次 | 14天 | 开发测试使用 |
| 开发环境 | 手动执行 | 7天 | 本地开发环境 |

## 🚨 恢复操作指南

### 恢复前准备

1. **停止应用服务**
   ```bash
   # 停止Spring Boot应用
   ps aux | grep yudao-server
   kill -15 <PID>
   ```

2. **验证备份文件**
   ```bash
   # 列出可用备份
   ./script/shell/restore_database.sh list
   ```

3. **确认恢复策略**
   - 是否需要完全恢复？
   - 数据丢失的接受程度？
   - 恢复时间窗口？

### 恢复步骤

1. **交互式恢复（推荐）**
   ```bash
   ./script/shell/restore_database.sh interactive
   ```

2. **选择备份文件**
   - 根据时间选择合适的备份点
   - 确认备份文件完整性

3. **确认恢复操作**
   - 系统会显示恢复信息
   - 确认后开始恢复

4. **验证恢复结果**
   - 检查数据完整性
   - 验证应用功能
   - 检查日志无错误

### 恢复失败处理

如果恢复失败，脚本会自动：
1. 尝试从安全备份恢复
2. 记录详细错误日志
3. 保留所有备份文件

手动处理步骤：
```bash
# 查看恢复日志
tail -f backup/database/logs/restore_*.log

# 检查数据库状态
mysql -h127.0.0.1 -uroot -p -e "SHOW DATABASES;"

# 手动恢复（如果需要）
gunzip -c backup/database/safety/safety_backup.sql.gz | mysql -h127.0.0.1 -uroot -p
```

## 🔍 监控和维护

### 备份监控

1. **检查备份文件**
   ```bash
   # 查看备份统计
   ./script/shell/backup_database.sh info
   
   # 检查最新备份
   ls -la backup/database/full/latest_full_backup.sql.gz
   ```

2. **监控磁盘空间**
   ```bash
   # 检查备份目录大小
   du -sh backup/database/
   
   # 检查磁盘空间
   df -h
   ```

3. **查看备份日志**
   ```bash
   # 查看最新备份日志
   tail -n 50 backup/database/logs/backup_*.log
   
   # 查看定时任务日志
   tail -f backup/database/logs/cron_backup.log
   ```

### 定期维护任务

1. **每月检查**
   - 验证备份文件完整性
   - 测试恢复流程
   - 清理过期日志

2. **每季度检查**
   - 回顾备份策略
   - 更新配置参数
   - 测试完整恢复流程

3. **年度检查**
   - 备份策略评估
   - 灾难恢复演练
   - 脚本更新和优化

## ⚠️ 安全注意事项

### 权限管理

1. **脚本权限**
   ```bash
   chmod 750 script/shell/*.sh    # 只有用户和组可执行
   ```

2. **备份文件权限**
   ```bash
   chmod 640 backup/database/**/*.sql.gz  # 只有用户可读写
   ```

3. **数据库访问**
   - 使用专用备份用户
   - 限制网络访问
   - 定期更新密码

### 数据保护

1. **备份加密**（生产环境推荐）
   ```bash
   # 加密备份文件
   gpg --cipher-algo AES256 --compress-algo 1 --symmetric backup.sql.gz
   ```

2. **异地备份**
   - 定期将备份文件传输到异地
   - 使用云存储服务
   - 实施3-2-1备份策略

3. **访问审计**
   - 记录所有备份恢复操作
   - 监控异常访问
   - 定期审查日志

## 🛠️ 故障排除

### 常见问题

1. **备份失败**
   ```bash
   # 检查错误信息
   grep ERROR backup/database/logs/backup_*.log
   
   # 测试数据库连接
   ./script/shell/backup_database.sh test
   ```

2. **恢复失败**
   ```bash
   # 检查备份文件完整性
   gunzip -t backup.sql.gz
   
   # 检查磁盘空间
   df -h
   ```

3. **定时任务未执行**
   ```bash
   # 检查cron服务
   sudo service cron status
   
   # 查看cron日志
   tail -f /var/log/cron
   ```

### 紧急恢复程序

如果所有自动化脚本都失败：

1. **手动备份**
   ```bash
   mysqldump -h127.0.0.1 -uroot -p --single-transaction tunnel_management > manual_backup.sql
   ```

2. **手动恢复**
   ```bash
   mysql -h127.0.0.1 -uroot -p -e "DROP DATABASE tunnel_management;"
   mysql -h127.0.0.1 -uroot -p -e "CREATE DATABASE tunnel_management;"
   mysql -h127.0.0.1 -uroot -p tunnel_management < backup.sql
   ```

## 📞 技术支持

如需帮助，请：
1. 查看日志文件中的详细错误信息
2. 检查数据库连接和权限
3. 确认磁盘空间充足
4. 联系系统管理员

---

**最后更新：** 2025-06-28  
**版本：** 1.0.0 