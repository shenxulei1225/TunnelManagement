# Windows 数据备份脚本使用说明

## 概述

这是一个专为 TunnelManagement 项目设计的 Windows 数据备份解决方案，支持数据库备份、文件备份、压缩存储、自动清理和邮件通知等功能。

## 文件结构

```
script/shell/
├── backup_data_windows.ps1    # PowerShell 主备份脚本
├── backup_windows.bat         # 批处理启动脚本（推荐使用）
├── backup_config.json         # 配置文件示例
└── README_backup_windows.md   # 使用说明文档
```

## 快速开始

### 1. 准备工作

1. **确保 PowerShell 可用**（Windows 10/11 默认已安装）
2. **安装数据库客户端工具**（根据使用的数据库类型）：
   - MySQL: 安装 MySQL Client 或 MySQL Workbench
   - PostgreSQL: 安装 PostgreSQL Client
   - SQL Server: 安装 SQL Server Management Studio

### 2. 配置文件设置

1. 复制 `backup_config.json` 并根据实际环境修改：
   ```json
   {
     "backup": {
       "base_path": "E:\\TunnelManagement\\backup",  // 备份存储路径
       "retention_days": 30,                         // 保留天数
       "compress": true                              // 是否压缩
     },
     "database": {
       "enabled": true,
       "type": "mysql",                              // 数据库类型
       "host": "localhost",
       "port": 3306,
       "username": "backup_user",                    // 数据库用户名
       "password": "your_password",                  // 数据库密码
       "databases": ["tunnel_management"]            // 要备份的数据库
     }
   }
   ```

2. **重要**：修改数据库连接信息和备份路径

### 3. 运行备份

#### 方式一：使用批处理脚本（推荐）
双击运行 `backup_windows.bat`，按照菜单提示操作。

#### 方式二：直接运行 PowerShell 脚本
```powershell
# 完整备份
.\backup_data_windows.ps1 -BackupType "full"

# 仅数据库备份
.\backup_data_windows.ps1 -BackupType "database"

# 仅文件备份
.\backup_data_windows.ps1 -BackupType "files"

# 使用自定义配置文件
.\backup_data_windows.ps1 -BackupType "full" -ConfigFile "custom_config.json"
```

## 功能特性

### 1. 数据库备份
- **支持多种数据库**：MySQL、PostgreSQL、SQL Server
- **完整备份**：包含表结构、数据、存储过程、触发器等
- **多数据库支持**：可同时备份多个数据库
- **自动压缩**：备份文件自动压缩为 .gz 格式

### 2. 文件备份
- **多源备份**：支持备份多个目录
- **排除规则**：支持排除特定文件类型或目录
- **增量复制**：使用 robocopy 进行高效文件复制
- **自动压缩**：备份目录自动压缩为 .zip 格式

### 3. 自动化功能
- **旧备份清理**：自动删除超过保留期的备份文件
- **日志记录**：详细的操作日志，支持不同级别（INFO、WARN、ERROR、SUCCESS）
- **错误处理**：完善的错误处理和恢复机制
- **邮件通知**：支持备份完成后发送邮件通知

### 4. 安全特性
- **权限控制**：支持数据库用户权限验证
- **配置文件保护**：敏感信息存储在配置文件中
- **日志审计**：完整的操作审计日志

## 配置详解

### 备份配置 (backup)
```json
{
  "backup": {
    "base_path": "E:\\TunnelManagement\\backup",  // 备份根目录
    "retention_days": 30,                         // 备份保留天数
    "compress": true                              // 是否启用压缩
  }
}
```

### 数据库配置 (database)
```json
{
  "database": {
    "enabled": true,                              // 是否启用数据库备份
    "type": "mysql",                              // 数据库类型：mysql/postgresql/sqlserver
    "host": "localhost",                          // 数据库主机
    "port": 3306,                                 // 数据库端口
    "username": "backup_user",                    // 数据库用户名
    "password": "your_password",                  // 数据库密码
    "databases": ["tunnel_management"],           // 要备份的数据库列表
    "backup_path": "database"                     // 数据库备份子目录
  }
}
```

### 文件配置 (files)
```json
{
  "files": {
    "enabled": true,                              // 是否启用文件备份
    "backup_path": "files",                       // 文件备份子目录
    "sources": [                                  // 备份源列表
      {
        "name": "应用配置",                       // 备份源名称
        "path": "yudao-server\\src\\main\\resources", // 相对于项目根目录的路径
        "exclude": ["*.log", "*.tmp"]             // 排除的文件模式
      }
    ]
  }
}
```

### 通知配置 (notification)
```json
{
  "notification": {
    "enabled": false,                             // 是否启用邮件通知
    "email": {
      "smtp_server": "smtp.qq.com",              // SMTP 服务器
      "smtp_port": 587,                          // SMTP 端口
      "username": "backup@example.com",          // 发件人邮箱
      "password": "your_email_password",         // 邮箱密码或应用密码
      "to": ["admin@example.com"]                // 收件人列表
    }
  }
}
```

## 备份策略建议

### 1. 日常备份策略
- **每日备份**：数据库 + 重要配置文件
- **每周备份**：完整备份（数据库 + 所有文件）
- **每月备份**：归档备份（长期保存）

### 2. 自动化部署
使用 Windows 任务计划程序设置定时备份：

1. 打开"任务计划程序"
2. 创建基本任务
3. 设置触发器（每日、每周等）
4. 设置操作：
   - 程序：`powershell.exe`
   - 参数：`-ExecutionPolicy Bypass -File "E:\TunnelManagement\script\shell\backup_data_windows.ps1" -BackupType "full"`

### 3. 存储策略
- **本地存储**：快速恢复，适合日常备份
- **网络存储**：异地备份，防止硬件故障
- **云存储**：长期归档，最高安全级别

## 故障排除

### 常见问题

1. **PowerShell 执行策略错误**
   ```powershell
   Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
   ```

2. **数据库连接失败**
   - 检查数据库服务是否运行
   - 验证用户名密码是否正确
   - 确认网络连接和防火墙设置

3. **文件访问权限错误**
   - 以管理员身份运行脚本
   - 检查备份目录的写入权限

4. **压缩失败**
   - 检查磁盘空间是否充足
   - 确认 .NET Framework 版本支持

### 日志分析
备份日志位于 `backup\logs\` 目录下，文件名格式：`backup_YYYYMMDD_HHMMSS.log`

日志级别说明：
- **INFO**：普通信息
- **SUCCESS**：操作成功
- **WARN**：警告信息
- **ERROR**：错误信息

## 安全注意事项

1. **配置文件安全**
   - 不要将包含密码的配置文件提交到版本控制系统
   - 设置适当的文件权限，限制访问

2. **备份文件安全**
   - 备份文件可能包含敏感数据，确保存储位置安全
   - 考虑对备份文件进行加密

3. **网络安全**
   - 如果使用网络存储，确保传输加密
   - 定期更新数据库和系统密码

## 性能优化

1. **并行处理**：脚本支持多线程文件复制
2. **压缩优化**：根据文件类型选择合适的压缩算法
3. **增量备份**：仅备份变更的文件（功能待实现）
4. **存储优化**：定期清理旧备份，避免磁盘空间不足

## 扩展功能

### 计划中的功能
1. **增量备份**：仅备份变更的文件
2. **差异备份**：基于完整备份的差异备份
3. **备份验证**：自动验证备份文件完整性
4. **恢复工具**：自动化恢复脚本
5. **监控集成**：与监控系统集成

### 自定义扩展
脚本采用模块化设计，可以轻松扩展：
- 添加新的数据库类型支持
- 集成云存储服务
- 添加自定义通知方式
- 集成企业级备份解决方案

## 支持与反馈

如有问题或建议，请：
1. 查看日志文件获取详细错误信息
2. 检查配置文件格式是否正确
3. 参考故障排除章节
4. 联系系统管理员或开发团队

---

**版本信息**：v1.0  
**最后更新**：2024年1月  
**兼容性**：Windows 10/11, PowerShell 5.1+ 