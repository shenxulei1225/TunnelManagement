# TunnelManagement Windows 备份恢复系统

## 📋 概述

这是一个完整的Windows备份恢复解决方案，支持数据库和文件的自动备份与恢复。

## 🚀 快速开始

### 1. 备份数据

```cmd
# 双击运行备份工具
backup_windows.bat

# 或使用PowerShell
powershell -ExecutionPolicy Bypass -File backup_data_windows.ps1 -BackupType full
```

### 2. 恢复数据

```cmd
# 双击运行恢复工具
restore_windows.bat

# 或使用PowerShell
powershell -ExecutionPolicy Bypass -File restore_data_windows.ps1 -RestoreType full
```

## 📁 文件结构

```
script/shell/
├── backup_windows.bat              # 备份主菜单
├── restore_windows.bat             # 恢复主菜单
├── backup_data_windows.ps1         # 备份PowerShell脚本
├── restore_data_windows.ps1        # 恢复PowerShell脚本
├── list_backups_windows.ps1        # 列出备份文件
├── test_connection_windows.ps1     # 测试数据库连接
├── backup_config.json              # 备份配置文件
├── restore_config.json             # 恢复配置文件
└── README_windows_backup.md        # 本文档
```

## ⚙️ 配置说明

### 备份配置 (backup_config.json)

```json
{
  "backup": {
    "base_path": "E:\\TunnelManagement\\backup",  // 备份根目录
    "retention_days": 30,                         // 保留天数
    "compress": true                              // 是否压缩
  },
  "database": {
    "enabled": true,                              // 启用数据库备份
    "type": "mysql",                              // 数据库类型
    "host": "localhost",                          // 数据库主机
    "port": 3306,                                // 数据库端口
    "username": "root",                           // 数据库用户名
    "password": "Coolhomer",                      // 数据库密码
    "databases": ["tunnel_management"],           // 要备份的数据库
    "backup_path": "database"                     // 备份子目录
  },
  "files": {
    "enabled": true,                              // 启用文件备份
    "backup_path": "files",                       // 文件备份子目录
    "sources": [                                  // 备份源配置
      {
        "name": "Application Config",
        "path": "yudao-server\\src\\main\\resources",
        "exclude": ["*.log", "*.tmp"]
      }
    ]
  }
}
```

### 恢复配置 (restore_config.json)

```json
{
  "backup": {
    "base_path": "E:\\TunnelManagement\\backup"   // 备份文件位置
  },
  "database": {
    "enabled": true,
    "type": "mysql",
    "host": "localhost",
    "port": 3306,
    "username": "root",
    "password": "Coolhomer",
    "databases": ["tunnel_management"],
    "backup_path": "database",
    "create_safety_backup": true                  // 恢复前创建安全备份
  },
  "files": {
    "enabled": true,
    "backup_path": "files",
    "restore_targets": [                          // 恢复目标配置
      {
        "name": "Application Config",
        "target_path": "yudao-server\\src\\main\\resources"
      }
    ]
  }
}
```

## 🔧 功能特性

### 备份功能

1. **数据库备份**
   - 支持MySQL、PostgreSQL、SQL Server
   - 自动压缩备份文件
   - 包含存储过程、触发器等
   - 支持多数据库备份

2. **文件备份**
   - 支持多目录备份
   - 文件过滤功能
   - 增量备份支持
   - 压缩存储

3. **自动化管理**
   - 定时清理过期备份
   - 邮件通知功能
   - 详细日志记录
   - 备份完整性验证

### 恢复功能

1. **数据库恢复**
   - 交互式备份选择
   - 恢复前安全备份
   - 自动数据库重建
   - 恢复结果验证

2. **文件恢复**
   - 选择性恢复
   - 现有文件备份
   - 目录结构保持
   - 权限保持

3. **安全机制**
   - 恢复前确认
   - 安全备份创建
   - 操作日志记录
   - 回滚支持

## 📊 使用示例

### 1. 完整备份

```cmd
backup_windows.bat
# 选择 1 - Full Backup
```

### 2. 仅备份数据库

```cmd
backup_windows.bat
# 选择 2 - Database Only
```

### 3. 查看可用备份

```cmd
backup_windows.bat
# 选择 4 - List Available Backups
```

### 4. 恢复数据库

```cmd
restore_windows.bat
# 选择 2 - Database Only
# 从列表中选择要恢复的备份
```

### 5. 测试数据库连接

```cmd
restore_windows.bat
# 选择 5 - Test Database Connection
```

## 🛠️ 环境要求

### 必需软件

1. **Windows 10/11**
2. **PowerShell 5.0+**
3. **数据库客户端工具**
   - MySQL: `mysql`, `mysqldump`
   - PostgreSQL: `psql`, `pg_dump`
   - SQL Server: `sqlcmd`

### 安装数据库客户端

#### MySQL
```cmd
# 下载并安装MySQL Server或MySQL Workbench
# https://dev.mysql.com/downloads/mysql/
# 确保mysql和mysqldump在PATH中
```

#### PostgreSQL
```cmd
# 下载并安装PostgreSQL
# https://www.postgresql.org/download/windows/
# 确保psql和pg_dump在PATH中
```

#### SQL Server
```cmd
# 安装SQL Server Management Studio (SSMS)
# https://docs.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms
# 或安装SQL Server Command Line Utilities
```

## 🔍 故障排除

### 常见问题

1. **"命令未找到"错误**
   ```cmd
   # 检查PATH环境变量
   echo %PATH%
   
   # 手动添加数据库客户端路径
   set PATH=%PATH%;C:\Program Files\MySQL\MySQL Server 8.0\bin
   ```

2. **数据库连接失败**
   ```cmd
   # 使用连接测试工具
   restore_windows.bat
   # 选择 5 - Test Database Connection
   ```

3. **权限不足**
   ```cmd
   # 以管理员身份运行命令提示符
   # 右键点击 "命令提示符" -> "以管理员身份运行"
   ```

4. **备份文件损坏**
   ```cmd
   # 检查备份文件完整性
   # 查看备份日志文件
   ```

### 日志文件

- **备份日志**: `backup\logs\backup_[时间戳].log`
- **恢复日志**: `backup\logs\restore_[时间戳].log`

### 配置验证

```powershell
# 验证备份配置
powershell -ExecutionPolicy Bypass -File test_connection_windows.ps1

# 列出可用备份
powershell -ExecutionPolicy Bypass -File list_backups_windows.ps1
```

## 📞 获取帮助

1. **查看详细日志**
   ```cmd
   # 日志文件位置
   backup\logs\
   ```

2. **检查配置文件**
   ```cmd
   # 备份配置
   script\shell\backup_config.json
   
   # 恢复配置
   script\shell\restore_config.json
   ```

3. **命令行帮助**
   ```cmd
   # 查看备份工具帮助
   backup_windows.bat
   # 选择 6 - Help
   
   # 查看恢复工具帮助
   restore_windows.bat
   # 选择 7 - Help
   ```

## 🔐 安全建议

1. **定期备份**
   - 设置自动备份计划
   - 验证备份完整性
   - 测试恢复流程

2. **备份存储**
   - 使用不同的存储位置
   - 定期清理过期备份
   - 考虑异地备份

3. **访问控制**
   - 限制备份文件访问权限
   - 使用强密码
   - 定期更新数据库密码

4. **监控告警**
   - 启用邮件通知
   - 监控备份任务状态
   - 定期检查日志文件

---

**版本**: 1.0  
**更新时间**: 2025-06-28  
**作者**: TunnelManagement Team 