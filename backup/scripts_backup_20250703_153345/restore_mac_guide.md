# Mac系统MySQL数据库恢复指南

## 📋 概述

本指南介绍如何在Mac系统上恢复从Windows系统导出的MySQL数据库备份。

## 🔧 前置要求

### 1. 安装MySQL 8

如果尚未安装MySQL，请使用以下方法之一：

#### 使用Homebrew安装（推荐）
```bash
# 安装Homebrew（如果尚未安装）
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# 安装MySQL 8
brew install mysql@8.1

# 启动MySQL服务
brew services start mysql@8.1

# 设置root密码
mysql_secure_installation
```

#### 使用官方DMG安装包
1. 访问 https://dev.mysql.com/downloads/mysql/
2. 下载 macOS 版本的 DMG 安装包
3. 双击安装并按照向导操作

### 2. 验证MySQL安装
```bash
# 检查MySQL版本
mysql --version

# 测试连接
mysql -u root -p -e "SELECT VERSION();"
```

## 📦 恢复备份文件

### 步骤1：传输备份文件

将Windows系统生成的备份文件传输到Mac：

```bash
# 创建备份目录
mkdir -p ~/mysql_backups

# 使用以下方法之一传输文件：
# 方法1: 使用scp（如果Windows启用了SSH）
scp user@windows-ip:E:/TunnelManagement/backup/mac_compatible/*.sql.gz ~/mysql_backups/

# 方法2: 使用云存储服务（如iCloud、Dropbox、OneDrive）
# 方法3: 使用U盘或移动硬盘
# 方法4: 使用AirDrop（如果是从另一台Mac传输）
```

### 步骤2：解压备份文件（如果是压缩格式）

```bash
cd ~/mysql_backups

# 查看备份文件
ls -la *.sql.gz

# 解压文件
gunzip tunnel_management_mac_compatible_*.sql.gz

# 验证解压后的文件
ls -la *.sql
```

### 步骤3：创建数据库（如果不存在）

```bash
# 登录MySQL
mysql -u root -p

# 在MySQL命令行中执行
CREATE DATABASE IF NOT EXISTS tunnel_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 查看数据库
SHOW DATABASES;

# 退出MySQL
EXIT;
```

### 步骤4：恢复数据库

```bash
# 方法1：使用mysql命令直接恢复
mysql -u root -p tunnel_management < ~/mysql_backups/tunnel_management_mac_compatible_*.sql

# 方法2：在MySQL命令行中恢复
mysql -u root -p
USE tunnel_management;
SOURCE ~/mysql_backups/tunnel_management_mac_compatible_20250119_123456.sql;
EXIT;

# 方法3：使用进度显示（适用于大文件）
pv ~/mysql_backups/tunnel_management_mac_compatible_*.sql | mysql -u root -p tunnel_management
```

## ✅ 验证恢复结果

```bash
# 登录MySQL并验证
mysql -u root -p tunnel_management

# 查看所有表
SHOW TABLES;

# 查看表结构示例
DESCRIBE your_table_name;

# 检查数据行数
SELECT COUNT(*) FROM your_table_name;

# 查看存储过程
SHOW PROCEDURE STATUS WHERE Db = 'tunnel_management';

# 查看触发器
SHOW TRIGGERS FROM tunnel_management;

# 退出
EXIT;
```

## 🔍 故障排除

### 常见问题及解决方案

#### 1. 字符编码问题
```bash
# 如果出现乱码，在恢复前设置字符集
mysql -u root -p --default-character-set=utf8mb4 tunnel_management < backup.sql
```

#### 2. 权限问题
```bash
# 授予必要权限
mysql -u root -p
GRANT ALL PRIVILEGES ON tunnel_management.* TO 'your_user'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

#### 3. 外键约束错误
```bash
# 临时禁用外键检查
mysql -u root -p
SET FOREIGN_KEY_CHECKS = 0;
SOURCE ~/mysql_backups/backup.sql;
SET FOREIGN_KEY_CHECKS = 1;
EXIT;
```

#### 4. 内存不足错误
```bash
# 增加MySQL最大允许包大小
mysql -u root -p --max_allowed_packet=1024M tunnel_management < backup.sql
```

#### 5. 时区问题
```bash
# 设置时区
mysql -u root -p
SET time_zone = '+08:00';  # 根据需要调整
SOURCE ~/mysql_backups/backup.sql;
EXIT;
```

## 🛠️ 高级选项

### 使用配置文件
创建 `~/.my.cnf` 文件以避免重复输入密码：

```ini
[client]
user=root
password=your_password
host=localhost
port=3306
default-character-set=utf8mb4
```

设置文件权限：
```bash
chmod 600 ~/.my.cnf
```

### 批量恢复多个数据库
```bash
#!/bin/bash
# restore_all.sh

BACKUP_DIR=~/mysql_backups
MYSQL_USER=root

for sql_file in $BACKUP_DIR/*.sql; do
    if [ -f "$sql_file" ]; then
        db_name=$(basename "$sql_file" | cut -d'_' -f1)
        echo "Restoring $db_name from $sql_file..."
        
        mysql -u $MYSQL_USER -p -e "CREATE DATABASE IF NOT EXISTS $db_name;"
        mysql -u $MYSQL_USER -p $db_name < "$sql_file"
        
        echo "Completed: $db_name"
    fi
done
```

### 恢复后优化
```bash
# 优化表
mysql -u root -p tunnel_management -e "OPTIMIZE TABLE table_name;"

# 分析表
mysql -u root -p tunnel_management -e "ANALYZE TABLE table_name;"

# 更新统计信息
mysqlcheck -u root -p --analyze tunnel_management
```

## 📊 性能建议

1. **大文件恢复**
   - 使用 `--quick` 选项减少内存使用
   - 分批恢复大表

2. **监控恢复进度**
   ```bash
   # 安装pv工具
   brew install pv
   
   # 使用pv显示进度
   pv backup.sql | mysql -u root -p database_name
   ```

3. **并行恢复**
   - 对于多个独立数据库，可以并行恢复以节省时间

## 📝 恢复检查清单

- [ ] MySQL服务已启动
- [ ] 目标数据库已创建
- [ ] 备份文件已传输并解压
- [ ] 字符集设置正确
- [ ] 有足够的磁盘空间
- [ ] 恢复命令执行成功
- [ ] 数据完整性验证通过
- [ ] 应用程序连接测试成功

## 🔐 安全建议

1. **恢复后立即更改密码**
   ```bash
   mysql -u root -p
   ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_secure_password';
   FLUSH PRIVILEGES;
   ```

2. **限制访问权限**
   ```bash
   # 只授予必要的权限
   GRANT SELECT, INSERT, UPDATE, DELETE ON tunnel_management.* TO 'app_user'@'localhost';
   ```

3. **启用SSL连接**
   ```bash
   # 检查SSL状态
   mysql -u root -p -e "SHOW VARIABLES LIKE '%ssl%';"
   ```

## 📞 获取帮助

如果遇到问题：

1. 查看MySQL错误日志：
   ```bash
   # Homebrew安装的MySQL
   tail -f /usr/local/var/mysql/*.err
   
   # 官方安装的MySQL
   tail -f /usr/local/mysql/data/*.err
   ```

2. 检查MySQL状态：
   ```bash
   brew services list | grep mysql
   # 或
   mysql.server status
   ```

3. 重启MySQL服务：
   ```bash
   brew services restart mysql@8.1
   # 或
   mysql.server restart
   ```

---

**版本**: 1.0  
**更新时间**: 2025-01-19  
**兼容性**: MySQL 8.x, macOS 10.15+
