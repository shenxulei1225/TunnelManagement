#!/bin/bash

# ===========================================
# TunnelManagement Windows 恢复包打包脚本
# 用于打包备份文件和脚本到Windows系统
# ===========================================

set -e

# 获取当前时间戳
DATE=$(date +%Y%m%d_%H%M%S)

# ===========================================
# 配置参数
# ===========================================

# 打包配置
PACKAGE_NAME="tunnel_management_windows_recovery_${DATE}"
PACKAGE_DIR="./packages/${PACKAGE_NAME}"
PACKAGE_FILE="${PACKAGE_DIR}.zip"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ===========================================
# 日志函数
# ===========================================

log_info() {
    echo -e "[$(date '+%Y-%m-%d %H:%M:%S')] [${GREEN}INFO${NC}] $*"
}

log_error() {
    echo -e "[$(date '+%Y-%m-%d %H:%M:%S')] [${RED}ERROR${NC}] $*"
}

log_warn() {
    echo -e "[$(date '+%Y-%m-%d %H:%M:%S')] [${YELLOW}WARN${NC}] $*"
}

log_success() {
    echo -e "[$(date '+%Y-%m-%d %H:%M:%S')] [${GREEN}SUCCESS${NC}] $*"
}

# ===========================================
# 主要函数
# ===========================================

check_dependencies() {
    log_info "检查依赖..."
    
    # 检查zip是否存在
    if ! command -v zip &> /dev/null; then
        log_error "zip 命令未找到，请安装zip工具"
        exit 1
    fi
    
    log_info "依赖检查完成"
}

create_package_structure() {
    log_info "创建打包目录结构..."
    
    # 创建主目录
    mkdir -p "$PACKAGE_DIR"
    mkdir -p "./packages"
    
    # 创建子目录
    mkdir -p "$PACKAGE_DIR/backup"
    mkdir -p "$PACKAGE_DIR/scripts"
    mkdir -p "$PACKAGE_DIR/docs"
    mkdir -p "$PACKAGE_DIR/config"
    
    log_info "目录结构创建完成"
}

copy_backup_files() {
    log_info "复制备份文件..."
    
    # 复制整个备份目录
    if [ -d "./backup" ]; then
        # 手动复制，跳过符号链接和系统文件
        find ./backup -type f \( -name "*.sql.gz" -o -name "*.log" -o -name "last_full_backup_time" \) | while read file; do
            rel_path=${file#./backup/}
            target_dir="$PACKAGE_DIR/backup/$(dirname "$rel_path")"
            mkdir -p "$target_dir"
            cp "$file" "$PACKAGE_DIR/backup/$rel_path"
        done
        
        # 复制目录结构
        find ./backup -type d | while read dir; do
            rel_path=${dir#./backup/}
            if [ -n "$rel_path" ]; then
                mkdir -p "$PACKAGE_DIR/backup/$rel_path"
            fi
        done
        
        # 重新创建正确的符号链接
        if [ -d "$PACKAGE_DIR/backup/database/full" ]; then
            cd "$PACKAGE_DIR/backup/database/full"
            # 找到最新的备份文件
            latest_backup=$(find . -name "*.sql.gz" -type f | sort | tail -1)
            if [ -n "$latest_backup" ]; then
                # 创建相对路径的符号链接
                ln -sf "$latest_backup" latest_full_backup.sql.gz
            fi
            cd - >/dev/null
        fi
        
        local backup_size=$(du -sh "$PACKAGE_DIR/backup" 2>/dev/null | cut -f1 || echo "未知")
        log_info "备份文件复制完成，大小: $backup_size"
    else
        log_warn "备份目录不存在，跳过备份文件复制"
    fi
}

create_windows_scripts() {
    log_info "创建Windows恢复脚本..."
    
    # 创建Windows批处理脚本（原版）
    cat > "$PACKAGE_DIR/scripts/restore_database.bat" << 'EOF'
@echo off
chcp 65001 >nul
echo ===============================================
echo TunnelManagement Windows 数据库恢复工具
echo ===============================================
echo.

REM 设置数据库连接参数 - 请根据实际环境修改
set DB_HOST=127.0.0.1
set DB_PORT=3306
set DB_USER=root
set DB_PASSWORD=Coolhomer
set DB_NAME=tunnel_management

REM 获取当前时间戳
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set "YY=%dt:~2,2%" & set "YYYY=%dt:~0,4%" & set "MM=%dt:~4,2%" & set "DD=%dt:~6,2%"
set "HH=%dt:~8,2%" & set "Min=%dt:~10,2%" & set "Sec=%dt:~12,2%"
set "DATE_TIME=%YYYY%%MM%%DD%_%HH%%Min%%Sec%"

echo [%date% %time%] [INFO] 开始数据库恢复操作...

REM 检查MySQL是否安装
mysql --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] MySQL客户端未找到，请安装MySQL并添加到PATH
    pause
    exit /b 1
)

REM 测试数据库连接
echo [%date% %time%] [INFO] 测试数据库连接...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 数据库连接失败，请检查连接参数
    pause
    exit /b 1
)
echo [%date% %time%] [INFO] 数据库连接测试成功

REM 列出可用的备份文件
echo.
echo 可用的备份文件:
echo.
set /a count=0
for %%f in (backup\database\full\*\*.sql.gz) do (
    set /a count+=1
    echo !count!^) %%~nxf
    set "file!count!=%%f"
)

if %count%==0 (
    echo [ERROR] 没有找到备份文件
    pause
    exit /b 1
)

echo.
set /p choice="请选择要恢复的备份文件编号: "

REM 验证选择
if %choice% LEQ 0 goto :invalid_choice
if %choice% GTR %count% goto :invalid_choice

call set selected_file=%%file%choice%%%
echo.
echo [%date% %time%] [INFO] 选择的备份文件: %selected_file%
echo.
echo 警告: 恢复操作将完全覆盖现有数据库！
set /p confirm="确认继续？(Y/N): "
if /i not "%confirm%"=="Y" (
    echo 操作已取消
    pause
    exit /b 0
)

REM 创建安全备份
echo [%date% %time%] [INFO] 创建安全备份...
if not exist "backup\database\safety" mkdir "backup\database\safety"
mysqldump -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% --single-transaction --routines --triggers --events --set-gtid-purged=OFF --default-character-set=utf8mb4 --lock-tables=false --add-drop-database --databases %DB_NAME% | gzip > "backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz"

if errorlevel 1 (
    echo [ERROR] 安全备份失败
    pause
    exit /b 1
)
echo [%date% %time%] [INFO] 安全备份完成

REM 删除现有数据库
echo [%date% %time%] [INFO] 删除现有数据库...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "DROP DATABASE IF EXISTS `%DB_NAME%`;"

REM 恢复数据库
echo [%date% %time%] [INFO] 恢复数据库...
gunzip -c "%selected_file%" | mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD%

if errorlevel 1 (
    echo [ERROR] 数据库恢复失败
    pause
    exit /b 1
)

echo [%date% %time%] [SUCCESS] 数据库恢复完成！
echo.
echo 恢复信息:
echo - 备份文件: %selected_file%
echo - 目标数据库: %DB_NAME%
echo - 安全备份: backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz
echo.
pause
exit /b 0

:invalid_choice
echo [ERROR] 无效的选择
pause
exit /b 1
EOF

    # 复制新的灵活恢复脚本
    if [ -f "./script/shell/restore_database_flexible.bat" ]; then
        cp "./script/shell/restore_database_flexible.bat" "$PACKAGE_DIR/scripts/"
        log_info "添加灵活数据库恢复脚本"
    fi

    # 创建简化的PowerShell脚本
    cat > "$PACKAGE_DIR/scripts/restore_database.ps1" << 'EOF'
# TunnelManagement PowerShell 数据库恢复脚本
# PowerShell执行策略: Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser

param(
    [string]$BackupFile = "",
    [string]$DbHost = "127.0.0.1",
    [string]$DbPort = "3306", 
    [string]$DbUser = "root",
    [string]$DbPassword = "Coolhomer",
    [string]$DbName = "tunnel_management"
)

# 设置控制台编码为UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

Write-Host "===============================================" -ForegroundColor Blue
Write-Host "TunnelManagement PowerShell 数据库恢复工具" -ForegroundColor Blue  
Write-Host "===============================================" -ForegroundColor Blue
Write-Host

# 检查MySQL
try {
    $null = Get-Command mysql -ErrorAction Stop
    Write-Host "[INFO] MySQL客户端检查通过" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] MySQL客户端未找到，请安装MySQL并添加到PATH" -ForegroundColor Red
    Read-Host "按任意键退出"
    exit 1
}

# 测试数据库连接
Write-Host "[INFO] 测试数据库连接..." -ForegroundColor Yellow
try {
    $result = & mysql -h$DbHost -P$DbPort -u$DbUser -p$DbPassword -e "SELECT 1;" 2>$null
    Write-Host "[INFO] 数据库连接测试成功" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] 数据库连接失败，请检查连接参数" -ForegroundColor Red
    Read-Host "按任意键退出"
    exit 1
}

# 如果没有指定备份文件，列出可用文件
if ([string]::IsNullOrEmpty($BackupFile)) {
    Write-Host "可用的备份文件:" -ForegroundColor Cyan
    Write-Host
    
    $backupFiles = Get-ChildItem -Path "backup\database\full" -Recurse -Filter "*.sql.gz" | Sort-Object LastWriteTime -Descending
    
    if ($backupFiles.Count -eq 0) {
        Write-Host "[ERROR] 没有找到备份文件" -ForegroundColor Red
        Read-Host "按任意键退出"
        exit 1
    }
    
    for ($i = 0; $i -lt $backupFiles.Count; $i++) {
        $file = $backupFiles[$i]
        $size = [math]::Round($file.Length / 1KB, 1)
        Write-Host "$($i + 1)) $($file.Name) ($($size)KB) - $($file.LastWriteTime)" -ForegroundColor White
    }
    
    Write-Host
    $choice = Read-Host "请选择要恢复的备份文件编号"
    
    try {
        $index = [int]$choice - 1
        if ($index -lt 0 -or $index -ge $backupFiles.Count) {
            throw "Invalid selection"
        }
        $BackupFile = $backupFiles[$index].FullName
    } catch {
        Write-Host "[ERROR] 无效的选择" -ForegroundColor Red
        Read-Host "按任意键退出"
        exit 1
    }
}

Write-Host
Write-Host "[INFO] 选择的备份文件: $BackupFile" -ForegroundColor Yellow
Write-Host
Write-Host "警告: 恢复操作将完全覆盖现有数据库！" -ForegroundColor Red
$confirm = Read-Host "确认继续？(Y/N)"

if ($confirm -ne "Y" -and $confirm -ne "y") {
    Write-Host "操作已取消" -ForegroundColor Yellow
    Read-Host "按任意键退出"
    exit 0
}

# 创建安全备份
$dateTime = Get-Date -Format "yyyyMMdd_HHmmss"
$safetyBackupDir = "backup\database\safety"
if (!(Test-Path $safetyBackupDir)) {
    New-Item -ItemType Directory -Path $safetyBackupDir -Force | Out-Null
}

$safetyBackupFile = "$safetyBackupDir\$($DbName)_safety_before_restore_$dateTime.sql.gz"

Write-Host "[INFO] 创建安全备份..." -ForegroundColor Yellow
try {
    & mysqldump -h$DbHost -P$DbPort -u$DbUser -p$DbPassword --single-transaction --routines --triggers --events --set-gtid-purged=OFF --default-character-set=utf8mb4 --lock-tables=false --add-drop-database --databases $DbName | & gzip > $safetyBackupFile
    Write-Host "[INFO] 安全备份完成: $safetyBackupFile" -ForegroundColor Green
} catch {
    Write-Host "[ERROR] 安全备份失败" -ForegroundColor Red
    Read-Host "按任意键退出"
    exit 1
}

# 恢复数据库
Write-Host "[INFO] 开始恢复数据库..." -ForegroundColor Yellow

try {
    # 解压并恢复
    & gunzip -c $BackupFile | & mysql -h$DbHost -P$DbPort -u$DbUser -p$DbPassword
    Write-Host "[SUCCESS] 数据库恢复完成！" -ForegroundColor Green
    
    Write-Host
    Write-Host "恢复信息:" -ForegroundColor Cyan
    Write-Host "- 备份文件: $BackupFile" -ForegroundColor White
    Write-Host "- 目标数据库: $DbName" -ForegroundColor White  
    Write-Host "- 安全备份: $safetyBackupFile" -ForegroundColor White
    
} catch {
    Write-Host "[ERROR] 数据库恢复失败" -ForegroundColor Red
    Write-Host "可以从安全备份恢复: $safetyBackupFile" -ForegroundColor Yellow
}

Write-Host
Read-Host "按任意键退出"
EOF

    log_info "Windows脚本创建完成"
}

copy_documentation() {
    log_info "复制文档..."
    
    # 复制文档目录
    if [ -d "./docs" ]; then
        cp -r ./docs/* "$PACKAGE_DIR/docs/"
        log_info "文档复制完成"
    else
        log_warn "文档目录不存在，跳过文档复制"
    fi
}

create_config_files() {
    log_info "创建配置文件..."
    
    # 创建数据库配置文件
    cat > "$PACKAGE_DIR/config/database.conf" << EOF
# TunnelManagement 数据库配置文件
# 请根据Windows环境修改以下配置

# 数据库连接配置
DB_HOST=127.0.0.1
DB_PORT=3306
DB_USER=root
DB_PASSWORD=Coolhomer
DB_NAME=tunnel_management

# 备份配置
BACKUP_DIR=./backup/database
LOG_DIR=./backup/database/logs

# 其他配置
CHARSET=utf8mb4
TIMEZONE=+08:00
EOF

    # 创建README文件
    cat > "$PACKAGE_DIR/README.md" << EOF
# TunnelManagement Windows 恢复包

## 概述

这是TunnelManagement项目的Windows恢复包，包含了在Windows系统上恢复数据库所需的所有文件。

## 包含内容

- \`backup/\` - 数据库备份文件
- \`scripts/\` - Windows恢复脚本
- \`docs/\` - 完整文档
- \`config/\` - 配置文件

## 快速开始

### 🚀 方法1: 灵活恢复脚本（推荐 - 支持不同数据库名）

```cmd
# 使用默认配置（数据库名：tunnel_management）
scripts\\restore_database_flexible.bat

# 指定不同的数据库名称（解决Mac/Windows数据库名不同问题）
scripts\\restore_database_flexible.bat --database tunnel_management_win

# 完整自定义配置
scripts\\restore_database_flexible.bat --host localhost --port 3307 --user admin --password mypass --database my_tunnel
```

### 📋 方法2: 标准恢复脚本

1. 双击运行 \`scripts/restore_database.bat\`
2. 按照提示选择备份文件
3. 确认恢复操作

### 💻 方法3: PowerShell脚本

1. 以管理员身份打开PowerShell
2. 执行: \`Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser\`
3. 运行: \`.\scripts\restore_database.ps1\`

### ⚙️ 方法4: 手动恢复

\`\`\`cmd
REM 解压备份文件并恢复
gunzip -c backup\\database\\full\\202506\\backup_file.sql.gz | mysql -h127.0.0.1 -uroot -p tunnel_management
\`\`\`

## 🔧 数据库名称配置

### 常见场景

| 场景 | 源数据库名 | 目标数据库名 | 推荐方法 |
|------|-----------|-------------|----------|
| 相同环境 | tunnel_management | tunnel_management | 标准脚本 |
| 不同环境 | tunnel_management | tunnel_management_win | 灵活脚本 |
| 开发/生产 | tunnel_dev | tunnel_prod | 灵活脚本 |

### 参数说明

灵活恢复脚本支持的参数：

\`\`\`cmd
--host <主机>       数据库主机地址 (默认: 127.0.0.1)
--port <端口>       数据库端口 (默认: 3306)
--user <用户名>     数据库用户名 (默认: root)
--password <密码>   数据库密码 (默认: Coolhomer)
--database <数据库名> 目标数据库名 (默认: tunnel_management)
--help              显示帮助信息
\`\`\`

### 示例用法

\`\`\`cmd
REM 基本用法
scripts\\restore_database_flexible.bat

REM Windows环境使用不同数据库名
scripts\\restore_database_flexible.bat --database tunnel_management_windows

REM 连接远程数据库
scripts\\restore_database_flexible.bat --host 192.168.1.100 --user backup_user --password mypass123

REM 完全自定义
scripts\\restore_database_flexible.bat --host localhost --port 3307 --database production_tunnel --user admin --password secure123
\`\`\`

## 系统要求

- Windows 10/11
- MySQL 8.0+ (客户端工具)
- PowerShell 5.0+ (可选)
- 7-Zip 或 WinRAR (用于解压.gz文件，可选)

## 配置修改

在执行恢复前，请检查并修改以下配置文件：

- \`config/database.conf\` - 数据库连接参数
- \`scripts/restore_database.bat\` - 批处理脚本中的数据库参数
- \`scripts/restore_database.ps1\` - PowerShell脚本中的数据库参数

### 快速配置修改

如果经常使用相同配置，可以直接修改脚本文件中的默认值：

1. 编辑 \`scripts/restore_database_flexible.bat\`
2. 修改顶部的默认配置：
   \`\`\`cmd
   set DB_HOST=你的主机地址
   set DB_PORT=你的端口
   set DB_USER=你的用户名
   set DB_PASSWORD=你的密码
   set DB_NAME=你的数据库名
   \`\`\`

## 🔍 自动检测功能

灵活恢复脚本具有以下自动检测功能：

- ✅ **备份文件分析** - 自动检测备份文件中的原数据库名称
- ✅ **名称转换提示** - 当目标数据库名与源不同时给出明确提示
- ✅ **连接验证** - 恢复前自动测试数据库连接
- ✅ **安全备份** - 恢复前自动备份现有数据库
- ✅ **恢复验证** - 恢复后验证数据完整性

## 注意事项

⚠️ **重要提醒**
- 恢复操作将完全覆盖现有数据库
- 脚本会自动创建安全备份
- 建议在恢复前停止相关应用服务
- 确保目标数据库名称符合MySQL命名规范

🔒 **安全考虑**
- 避免在脚本中硬编码敏感密码
- 生产环境建议使用专用备份用户
- 定期更新数据库密码

## 故障排除

如果遇到问题，请查看：

1. **连接问题**
   - 检查MySQL服务是否启动
   - 验证网络连接和防火墙设置
   - 确认用户名密码正确

2. **权限问题**
   - 确保用户有CREATE、DROP、ALTER权限
   - 检查数据库访问权限设置

3. **文件问题**
   - 验证备份文件完整性：\`gunzip -t backup_file.sql.gz\`
   - 检查磁盘空间是否充足

4. **参考文档**
   - \`docs/database-management.md\` - 完整的管理指南
   - \`docs/cross-platform-database-restore.md\` - 跨平台恢复指南
   - \`backup/database/logs/\` - 操作日志

## 技术支持

如需帮助，请参考 \`docs/\` 目录中的完整文档。

---
生成时间: $(date)
包版本: ${PACKAGE_NAME}
包含脚本: restore_database.bat, restore_database_flexible.bat, restore_database.ps1
EOF

    log_info "配置文件创建完成"
}

create_package() {
    log_info "创建压缩包..."
    
    # 创建zip压缩包
    cd "./packages"
    zip -r "$(basename "$PACKAGE_FILE")" "$(basename "$PACKAGE_DIR")" >/dev/null 2>&1
    cd - >/dev/null
    
    # 删除临时目录
    rm -rf "$PACKAGE_DIR"
    
    local package_size=$(du -sh "$PACKAGE_FILE" | cut -f1)
    log_success "压缩包创建完成: $PACKAGE_FILE (大小: $package_size)"
}

show_package_info() {
    log_info "=== 打包完成 ==="
    echo ""
    echo "📦 恢复包信息:"
    echo "   文件名: $(basename "$PACKAGE_FILE")"
    echo "   位置: $PACKAGE_FILE"
    echo "   大小: $(du -sh "$PACKAGE_FILE" | cut -f1)"
    echo ""
    echo "📋 包含内容:"
    echo "   ✅ 数据库备份文件"
    echo "   ✅ Windows恢复脚本(.bat + .ps1)"
    echo "   ✅ 完整文档"
    echo "   ✅ 配置文件和说明"
    echo ""
    echo "🚀 Windows使用方法:"
    echo "   1. 将 $(basename "$PACKAGE_FILE") 传输到Windows电脑"
    echo "   2. 解压到任意目录"
    echo "   3. 双击运行 scripts/restore_database.bat"
    echo "   4. 按提示选择备份文件并确认恢复"
    echo ""
    echo "📖 详细说明请查看解压后的 README.md 文件"
}

show_help() {
    cat << EOF
TunnelManagement Windows 恢复包打包脚本

用法: $0 [选项]

选项:
    package     创建Windows恢复包（默认）
    clean       清理packages目录
    help        显示此帮助信息

示例:
    $0 package                    # 创建恢复包
    $0 clean                      # 清理旧的包文件
    $0 help                       # 显示帮助

功能说明:
    此脚本会打包以下内容到Windows恢复包：
    - 所有数据库备份文件
    - Windows批处理和PowerShell恢复脚本
    - 完整的项目文档
    - 数据库配置文件
    - 详细的使用说明

输出:
    packages/tunnel_management_windows_recovery_[时间戳].zip

EOF
}

# ===========================================
# 主函数
# ===========================================

main() {
    local action=${1:-"package"}
    
    echo -e "${BLUE}TunnelManagement Windows 恢复包打包工具${NC}"
    echo "=============================================="
    echo ""
    
    case "$action" in
        "package")
            check_dependencies
            create_package_structure
            copy_backup_files
            create_windows_scripts
            copy_documentation
            create_config_files
            create_package
            show_package_info
            ;;
        "clean")
            log_info "清理packages目录..."
            rm -rf "./packages"
            log_success "清理完成"
            ;;
        "help"|"-h"|"--help")
            show_help
            ;;
        *)
            log_error "未知选项: $action"
            show_help
            exit 1
            ;;
    esac
    
    log_info "操作完成"
}

# 运行主函数
main "$@" 