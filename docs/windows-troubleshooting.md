# Windows 数据库恢复故障排除指南

## 🚨 常见问题解决方案

### 问题1: "the system cannot accept the time enterd"

**症状:** 运行恢复脚本时出现时间相关错误

**原因:** Windows批处理脚本的时间戳获取方法在某些系统配置下不兼容

**解决方案:**

#### 方案A: 使用简化恢复脚本（推荐）
```cmd
# 使用不依赖复杂时间戳的简化脚本
scripts\restore_database_simple.bat

# 指定数据库名称
scripts\restore_database_simple.bat --database your_db_name
```

#### 方案B: 使用PowerShell脚本
```powershell
# 在PowerShell中运行
.\scripts\restore_database.ps1

# 或带参数
.\scripts\restore_database.ps1 -DbName "your_database_name"
```

#### 方案C: 手动恢复
```cmd
# 1. 测试数据库连接
mysql -h127.0.0.1 -uroot -p -e "SELECT 1;"

# 2. 直接恢复（需要gunzip工具）
gunzip -c backup\database\full\202506\backup_file.sql.gz | mysql -h127.0.0.1 -uroot -p

# 3. 如果没有gunzip，使用7-Zip解压后恢复
# 先用7-Zip解压.gz文件，然后:
mysql -h127.0.0.1 -uroot -p < backup_file.sql
```

### 问题2: MySQL客户端未找到

**症状:** "MySQL客户端未找到" 或 "'mysql' is not recognized"

**解决方案:**

#### 选项1: 安装MySQL完整版
1. 下载MySQL Server: https://dev.mysql.com/downloads/mysql/
2. 安装时选择"Developer Default"或"Server only"
3. 安装后MySQL会自动添加到PATH

#### 选项2: 仅安装MySQL命令行工具
1. 下载MySQL Shell: https://dev.mysql.com/downloads/shell/
2. 解压到指定目录（如 C:\mysql\bin）
3. 手动添加到PATH环境变量

#### 选项3: 使用MySQL Workbench
1. 下载MySQL Workbench: https://dev.mysql.com/downloads/workbench/
2. 安装后命令行工具通常在: `C:\Program Files\MySQL\MySQL Workbench 8.0 CE\`

#### 手动添加PATH步骤:
```cmd
# 1. 找到mysql.exe所在目录，通常是:
C:\Program Files\MySQL\MySQL Server 8.0\bin
# 或
C:\mysql\bin

# 2. 添加到系统PATH:
# 控制面板 → 系统 → 高级系统设置 → 环境变量 → 系统变量 → Path → 编辑 → 新建
# 输入MySQL bin目录路径

# 3. 重新打开命令提示符测试:
mysql --version
```

### 问题3: 数据库连接失败

**症状:** "数据库连接失败" 或 "Access denied"

**解决方案:**

#### 检查MySQL服务
```cmd
# 检查MySQL服务状态
sc query MySQL80
# 或
net start | findstr MySQL

# 启动MySQL服务
net start MySQL80
```

#### 检查连接参数
```cmd
# 测试连接（替换为实际参数）
mysql -h127.0.0.1 -P3306 -uroot -p

# 如果密码为空
mysql -h127.0.0.1 -uroot

# 检查用户权限
mysql -h127.0.0.1 -uroot -p -e "SHOW GRANTS FOR 'root'@'localhost';"
```

#### 重置root密码（如果忘记）
```cmd
# 1. 停止MySQL服务
net stop MySQL80

# 2. 以安全模式启动MySQL
mysqld --console --skip-grant-tables --shared-memory

# 3. 新开命令提示符，连接MySQL
mysql -uroot

# 4. 重置密码
USE mysql;
UPDATE user SET authentication_string=PASSWORD('new_password') WHERE User='root';
FLUSH PRIVILEGES;
EXIT;

# 5. 重启MySQL服务
net start MySQL80
```

### 问题4: gunzip命令未找到

**症状:** "'gunzip' is not recognized as an internal or external command"

**解决方案:**

#### 选项1: 安装Git for Windows（推荐）
1. 下载: https://git-scm.com/download/win
2. 安装时选择"Git Bash Here"
3. Git会自带gunzip命令

#### 选项2: 使用7-Zip
```cmd
# 1. 安装7-Zip: https://www.7-zip.org/download.html
# 2. 使用7-Zip解压.gz文件:
"C:\Program Files\7-Zip\7z.exe" x backup_file.sql.gz
# 3. 然后恢复解压后的.sql文件:
mysql -h127.0.0.1 -uroot -p database_name < backup_file.sql
```

#### 选项3: 使用PowerShell
```powershell
# PowerShell可以处理.gz文件
Add-Type -AssemblyName System.IO.Compression.FileSystem
[System.IO.Compression.ZipFile]::ExtractToDirectory("backup_file.sql.gz", ".")
```

### 问题5: 权限不足

**症状:** "Access denied" 或 "permission denied"

**解决方案:**

#### 以管理员身份运行
```cmd
# 1. 右键点击"命令提示符"
# 2. 选择"以管理员身份运行"
# 3. 重新执行恢复脚本
```

#### 检查文件权限
```cmd
# 检查备份文件是否可读
dir backup\database\full\202506\*.sql.gz

# 检查写入权限
echo test > backup\database\safety\test.txt
del backup\database\safety\test.txt
```

### 问题6: 数据库名称包含特殊字符

**症状:** SQL语法错误或数据库名称无法识别

**解决方案:**

```cmd
# 使用反引号包围数据库名
mysql -h127.0.0.1 -uroot -p -e "CREATE DATABASE `tunnel-management`;"

# 或在脚本中修改DB_NAME变量为:
set DB_NAME=`tunnel-management`
```

## 🔧 调试工具和方法

### 启用详细日志

#### 方法1: 修改脚本
在bat文件开头添加:
```cmd
@echo on
```

#### 方法2: 使用PowerShell
```powershell
# 设置详细输出
$VerbosePreference = "Continue"
.\restore_database.ps1 -Verbose
```

### 测试各个组件

#### 测试MySQL连接
```cmd
mysql -h127.0.0.1 -P3306 -uroot -p -e "SELECT VERSION(), NOW();"
```

#### 测试gunzip
```cmd
gunzip --version
# 或测试解压
gunzip -t backup_file.sql.gz
```

#### 测试备份文件完整性
```cmd
# 检查文件大小
dir backup\database\full\202506\*.sql.gz

# 尝试查看文件头部
gunzip -c backup_file.sql.gz | head -10
```

## 📋 环境检查清单

在运行恢复脚本前，请确认以下项目：

### 基础环境
- [ ] Windows 10/11 系统
- [ ] 管理员权限
- [ ] 充足的磁盘空间（至少是备份文件的3倍）

### MySQL环境
- [ ] MySQL Server已安装并运行
- [ ] mysql命令可在命令行使用
- [ ] 数据库用户有足够权限（CREATE, DROP, ALTER）
- [ ] 可以正常连接到目标数据库服务器

### 文件和工具
- [ ] 恢复包已完整解压
- [ ] 备份文件存在且完整
- [ ] gunzip或7-Zip可用
- [ ] PowerShell 5.0+（可选）

### 网络和安全
- [ ] 防火墙允许MySQL连接（端口3306）
- [ ] 数据库服务器可访问
- [ ] 杀毒软件不会阻止脚本执行

## 🆘 获得帮助

如果以上解决方案都无法解决问题：

1. **查看详细错误信息**
   ```cmd
   # 将错误输出保存到文件
   restore_database_simple.bat > error_log.txt 2>&1
   ```

2. **收集系统信息**
   ```cmd
   # Windows版本
   ver
   
   # MySQL版本
   mysql --version
   
   # 环境变量
   echo %PATH%
   ```

3. **使用最小配置测试**
   ```cmd
   # 最简单的恢复命令
   mysql -h127.0.0.1 -uroot -p tunnel_management < backup_file.sql
   ```

4. **联系技术支持时提供**
   - 完整的错误信息
   - Windows版本
   - MySQL版本
   - 使用的恢复脚本名称
   - 执行的具体命令

## 📞 快速联系

- 📧 查看项目README.md中的联系方式
- 📖 参考docs/database-management.md获取完整指南
- 🔍 搜索相关错误信息的在线解决方案

---

**最后更新:** 2025-06-28  
**适用版本:** Windows恢复包 v1.2+ 