@echo off
chcp 65001 >nul
echo ===============================================
echo TunnelManagement 简化数据库恢复工具 (Windows)
echo ===============================================
echo.

REM 数据库连接参数 - 请根据实际环境修改
set DB_HOST=127.0.0.1
set DB_PORT=3306
set DB_USER=root
set DB_PASSWORD=Coolhomer
set DB_NAME=tunnel_management

REM 解析基本参数
if "%~1"=="--database" set DB_NAME=%~2
if "%~1"=="--help" goto :show_help

echo 当前配置:
echo   主机: %DB_HOST%:%DB_PORT%
echo   用户: %DB_USER%
echo   目标数据库: %DB_NAME%
echo.

REM 检查MySQL
where mysql >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 未找到MySQL客户端
    echo 请安装MySQL并确保mysql命令在PATH中
    echo.
    pause
    exit /b 1
)

REM 测试连接
echo [INFO] 测试数据库连接...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 数据库连接失败
    echo 请检查主机、端口、用户名和密码设置
    pause
    exit /b 1
)
echo [SUCCESS] 数据库连接成功
echo.

REM 检查备份文件
if not exist "backup\database\full" (
    echo [ERROR] 备份目录不存在
    echo 请确保已解压完整的恢复包
    pause
    exit /b 1
)

REM 列出备份文件
echo 可用的备份文件:
echo.
setlocal enabledelayedexpansion
set /a count=0
for /r "backup\database\full" %%f in (*.sql.gz) do (
    set /a count+=1
    echo !count!^) %%~nxf
    set "file!count!=%%f"
)

if %count%==0 (
    echo [ERROR] 没有找到.sql.gz备份文件
    pause
    exit /b 1
)

echo.
set /p choice="请选择备份文件编号 (1-%count%): "

REM 验证选择
if "%choice%"=="" goto :invalid_choice
if %choice% LEQ 0 goto :invalid_choice
if %choice% GTR %count% goto :invalid_choice

call set selected_file=%%file%choice%%%

echo.
echo [INFO] 选择的文件: !selected_file!
echo [INFO] 目标数据库: %DB_NAME%
echo.
echo ⚠️  警告: 此操作将完全覆盖数据库 %DB_NAME%
set /p confirm="确认继续? (y/n): "
if /i not "%confirm%"=="y" (
    echo 操作已取消
    pause
    exit /b 0
)

REM 创建安全备份目录
if not exist "backup\database\safety" mkdir "backup\database\safety"

REM 检查目标数据库是否存在并备份
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "USE %DB_NAME%;" >nul 2>&1
if not errorlevel 1 (
    echo [INFO] 正在备份现有数据库...
    set backup_name=%DB_NAME%_safety_%random%.sql.gz
    mysqldump -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% --single-transaction --databases %DB_NAME% 2>nul | gzip > "backup\database\safety\!backup_name!"
    if not errorlevel 1 (
        echo [SUCCESS] 安全备份已创建: !backup_name!
    )
)

REM 删除现有数据库
echo [INFO] 删除现有数据库...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "DROP DATABASE IF EXISTS `%DB_NAME%`;" 2>nul

REM 恢复数据库
echo [INFO] 正在恢复数据库...
gunzip -c "!selected_file!" | mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% 2>nul

if errorlevel 1 (
    echo [ERROR] 恢复失败
    if defined backup_name (
        echo 可尝试从安全备份恢复: backup\database\safety\!backup_name!
    )
    pause
    exit /b 1
)

REM 验证结果
echo [INFO] 验证恢复结果...
for /f "skip=1" %%i in ('mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='%DB_NAME%';" 2^>nul') do set table_count=%%i

echo.
echo ===============================================
echo ✅ 恢复完成!
echo.
echo 恢复信息:
echo   备份文件: !selected_file!
echo   目标数据库: %DB_NAME%
echo   表数量: %table_count%
if defined backup_name (
echo   安全备份: backup\database\safety\!backup_name!
)
echo ===============================================
echo.
pause
exit /b 0

:invalid_choice
echo [ERROR] 无效选择
pause
exit /b 1

:show_help
echo.
echo 简化数据库恢复工具
echo.
echo 用法: %~nx0 [--database 数据库名]
echo.
echo 示例:
echo   %~nx0                              # 使用默认数据库名
echo   %~nx0 --database my_tunnel_db      # 指定数据库名
echo.
echo 说明:
echo   - 自动列出可用备份文件
echo   - 恢复前自动创建安全备份
echo   - 支持自定义目标数据库名称
echo.
pause
exit /b 0 