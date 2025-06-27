# 跨平台数据库恢复指南

## 概述

本指南详细说明如何在不同操作系统和数据库环境之间进行数据恢复，特别是当目标数据库名称与源数据库不同时的处理方法。

## 🎯 常见场景

### 场景1: 不同平台相同数据库名
- **源环境**: Mac/Linux，数据库名：`tunnel_management`
- **目标环境**: Windows，数据库名：`tunnel_management`
- **处理**: 直接恢复，无需特殊处理

### 场景2: 不同平台不同数据库名
- **源环境**: Mac，数据库名：`tunnel_management`
- **目标环境**: Windows，数据库名：`tunnel_management_win`
- **处理**: 需要数据库名称转换

### 场景3: 不同服务器环境
- **源环境**: 开发环境，数据库名：`tunnel_management_dev`
- **目标环境**: 生产环境，数据库名：`tunnel_management_prod`
- **处理**: 需要数据库名称转换

## 🛠️ 解决方案

### 方案1: Windows灵活恢复脚本 (推荐)

我们已经创建了一个增强版的Windows恢复脚本，支持自定义数据库名称。

#### 使用方法

1. **解压恢复包**
   ```cmd
   # 解压下载的Windows恢复包
   unzip tunnel_management_windows_recovery_[时间戳].zip
   cd tunnel_management_windows_recovery_[时间戳]
   ```

2. **使用默认配置恢复**
   ```cmd
   # 双击运行或命令行执行
   scripts\restore_database_flexible.bat
   ```

3. **自定义数据库名称恢复**
   ```cmd
   # 指定不同的数据库名称
   scripts\restore_database_flexible.bat --database tunnel_management_win
   
   # 完整自定义配置
   scripts\restore_database_flexible.bat --host localhost --port 3307 --user admin --password mypass --database my_tunnel
   ```

#### 支持的参数

| 参数 | 说明 | 默认值 | 示例 |
|------|------|--------|------|
| `--host` | 数据库主机地址 | 127.0.0.1 | `--host 192.168.1.100` |
| `--port` | 数据库端口 | 3306 | `--port 3307` |
| `--user` | 数据库用户名 | root | `--user admin` |
| `--password` | 数据库密码 | Coolhomer | `--password mypass123` |
| `--database` | 目标数据库名 | tunnel_management | `--database tunnel_win` |
| `--help` | 显示帮助信息 | - | `--help` |

### 方案2: Mac/Linux增强恢复脚本

#### 基本用法
```bash
# 交互式恢复到不同数据库名
./script/shell/restore_database.sh interactive --database tunnel_management_win

# 从最新备份恢复到远程数据库
./script/shell/restore_database.sh latest --host 192.168.1.100 --user backup_user --password mypass --database production_tunnel

# 从指定文件恢复
./script/shell/restore_database.sh file ./backup/database/full/202506/backup.sql.gz --database new_tunnel_db
```

#### 支持的参数
```bash
# 数据库连接参数
--host <主机>          # 数据库主机地址
--port <端口>          # 数据库端口
--user <用户名>        # 数据库用户名
--password <密码>      # 数据库密码
--database <数据库名>  # 目标数据库名称
--backup-dir <路径>    # 备份文件目录
```

### 方案3: 手动数据库名称转换

如果自动化脚本不能满足需求，可以手动进行数据库名称转换。

#### Windows PowerShell方法
```powershell
# 1. 解压备份文件并替换数据库名称
$backupFile = "backup\database\full\202506\tunnel_management_full_20250627_235914.sql.gz"
$sourceDbName = "tunnel_management"
$targetDbName = "tunnel_management_win"

# 2. 读取并替换内容
$content = & gunzip -c $backupFile
$content = $content -replace "`$sourceDbName`", "`$targetDbName`"
$content = $content -replace "Database: $sourceDbName", "Database: $targetDbName"

# 3. 导入到MySQL
$content | & mysql -hlocalhost -uroot -p$password
```

#### Linux/Mac Bash方法
```bash
# 1. 设置变量
BACKUP_FILE="backup/database/full/202506/tunnel_management_full_20250627_235914.sql.gz"
SOURCE_DB="tunnel_management"
TARGET_DB="tunnel_management_win"

# 2. 解压、替换并导入
gunzip -c "$BACKUP_FILE" | \
sed "s/\`$SOURCE_DB\`/\`$TARGET_DB\`/g" | \
sed "s/Database: $SOURCE_DB/Database: $TARGET_DB/g" | \
mysql -h127.0.0.1 -uroot -p
```

## 📋 详细操作流程

### Windows环境完整恢复流程

1. **环境准备**
   ```cmd
   # 确保MySQL客户端已安装
   mysql --version
   
   # 如果未安装，从官网下载
   # https://dev.mysql.com/downloads/mysql/
   ```

2. **获取恢复包**
   - 从Mac/Linux环境生成恢复包
   - 传输到Windows系统
   - 解压到工作目录

3. **配置检查**
   ```cmd
   # 检查目标数据库连接
   mysql -h127.0.0.1 -uroot -p -e "SHOW DATABASES;"
   
   # 确认目标数据库名称
   mysql -h127.0.0.1 -uroot -p -e "SELECT 'target_db_name' as target_database;"
   ```

4. **执行恢复**
   ```cmd
   # 方法1: 使用灵活恢复脚本（推荐）
   scripts\restore_database_flexible.bat --database your_target_db_name
   
   # 方法2: 使用标准恢复脚本
   scripts\restore_database.bat
   # 然后手动修改脚本中的DB_NAME变量
   ```

5. **验证恢复**
   ```cmd
   # 检查数据库是否创建成功
   mysql -h127.0.0.1 -uroot -p -e "USE your_target_db_name; SHOW TABLES;"
   
   # 检查数据完整性
   mysql -h127.0.0.1 -uroot -p -e "USE your_target_db_name; SELECT COUNT(*) FROM some_important_table;"
   ```

### Mac/Linux环境恢复流程

1. **环境准备**
   ```bash
   # 检查MySQL客户端
   mysql --version
   
   # 克隆项目（如果需要）
   git clone [repository-url]
   cd TunnelManagement
   ```

2. **配置数据库连接**
   ```bash
   # 编辑恢复脚本或使用参数
   vim script/shell/restore_database.sh  # 可选，修改默认配置
   ```

3. **执行恢复**
   ```bash
   # 交互式恢复到不同数据库
   ./script/shell/restore_database.sh interactive --database target_db_name
   
   # 或者从最新备份直接恢复
   ./script/shell/restore_database.sh latest --database target_db_name
   ```

4. **验证恢复**
   ```bash
   # 检查恢复结果
   mysql -h127.0.0.1 -uroot -p -e "USE target_db_name; SHOW TABLES;"
   ```

## 🔧 高级配置

### 配置文件方式

创建配置文件避免每次输入参数：

#### Windows配置文件
创建 `config\restore.conf`：
```ini
[database]
host=127.0.0.1
port=3306
user=root
password=your_password
database=tunnel_management_win

[backup]
backup_dir=backup\database
```

#### Linux/Mac配置文件
创建 `config/restore.conf`：
```bash
# 数据库配置
DB_HOST="192.168.1.100"
DB_PORT="3306"
DB_USER="backup_user"
DB_PASSWORD="secure_password"
DB_NAME="production_tunnel"

# 备份目录
BACKUP_BASE_DIR="./backup/database"
```

### 批量恢复脚本

对于多个数据库的恢复：

```bash
# 创建批量恢复脚本
#!/bin/bash

DATABASES=("tunnel_dev" "tunnel_test" "tunnel_prod")
BACKUP_FILE="backup/database/full/202506/tunnel_management_full_20250627_235914.sql.gz"

for db in "${DATABASES[@]}"; do
    echo "恢复数据库: $db"
    ./script/shell/restore_database.sh file "$BACKUP_FILE" --database "$db"
    if [ $? -eq 0 ]; then
        echo "✅ $db 恢复成功"
    else
        echo "❌ $db 恢复失败"
    fi
done
```

## ⚠️ 注意事项

### 数据库名称限制
- MySQL数据库名称不能包含特殊字符
- 建议使用字母、数字、下划线
- 避免使用MySQL保留字

### 权限要求
- 恢复用户需要CREATE、DROP、ALTER权限
- 建议使用具有完整权限的管理员账户
- 生产环境需要专门的备份恢复用户

### 网络配置
- 确保目标数据库服务器可访问
- 检查防火墙设置
- 验证数据库服务器监听地址

### 存储空间
- 确保目标服务器有足够存储空间
- 恢复过程中需要临时空间
- 考虑安全备份的空间需求

## 🚨 故障排除

### 常见错误及解决方案

1. **数据库连接失败**
   ```
   错误: ERROR 2003 (HY000): Can't connect to MySQL server
   解决: 检查主机地址、端口、用户名、密码
   ```

2. **数据库名称冲突**
   ```
   错误: ERROR 1007 (HY000): Can't create database 'xxx'; database exists
   解决: 使用不同的数据库名称或先删除现有数据库
   ```

3. **权限不足**
   ```
   错误: ERROR 1044 (42000): Access denied for user
   解决: 确保用户有CREATE、DROP权限
   ```

4. **备份文件损坏**
   ```
   错误: gunzip: invalid magic number
   解决: 重新下载备份文件或检查文件完整性
   ```

### 调试方法

```bash
# 启用详细日志
export MYSQL_DEBUG=1
./script/shell/restore_database.sh interactive --database test_db

# 手动测试数据库连接
mysql -h127.0.0.1 -uroot -p -e "SELECT VERSION();"

# 检查备份文件
gunzip -t backup_file.sql.gz
gunzip -c backup_file.sql.gz | head -20
```

## 📞 技术支持

如遇问题，请：

1. **查看日志文件**
   - Windows: `backup\database\logs\restore_*.log`
   - Linux/Mac: `backup/database/logs/restore_*.log`

2. **检查环境配置**
   - MySQL客户端版本
   - 网络连接状态
   - 用户权限设置

3. **参考相关文档**
   - `docs/database-management.md` - 完整管理指南
   - `backup/README.md` - 备份目录说明
   - 脚本内置help功能

---

**最后更新：** 2025-06-28  
**版本：** 1.0.0  
**适用范围：** Windows、Mac、Linux跨平台环境 