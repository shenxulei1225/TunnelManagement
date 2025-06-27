@echo off
chcp 65001 >nul
echo ===============================================
echo TunnelManagement 灵活数据库恢复工具 (Windows)
echo ===============================================
echo.

REM 默认数据库连接参数 - 可通过命令行参数修改
set DB_HOST=127.0.0.1
set DB_PORT=3306
set DB_USER=root
set DB_PASSWORD=Coolhomer
set DB_NAME=tunnel_management

REM 解析命令行参数
:parse_args
if "%~1"=="" goto :end_parse
if "%~1"=="--host" (
    set DB_HOST=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--port" (
    set DB_PORT=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--user" (
    set DB_USER=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--password" (
    set DB_PASSWORD=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--database" (
    set DB_NAME=%~2
    shift
    shift
    goto :parse_args
)
if "%~1"=="--help" (
    goto :show_help
)
shift
goto :parse_args
:end_parse

REM 获取当前时间戳
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set "YY=%dt:~2,2%" & set "YYYY=%dt:~0,4%" & set "MM=%dt:~4,2%" & set "DD=%dt:~6,2%"
set "HH=%dt:~8,2%" & set "Min=%dt:~10,2%" & set "Sec=%dt:~12,2%"
set "DATE_TIME=%YYYY%%MM%%DD%_%HH%%Min%%Sec%"

echo [%date% %time%] [INFO] 数据库恢复工具启动
echo.
echo 当前配置:
echo   主机: %DB_HOST%:%DB_PORT%
echo   用户: %DB_USER%
echo   目标数据库: %DB_NAME%
echo.

REM 检查MySQL是否安装
mysql --version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] MySQL客户端未找到，请安装MySQL并添加到PATH
    echo.
    echo 下载地址: https://dev.mysql.com/downloads/mysql/
    pause
    exit /b 1
)

REM 测试数据库连接
echo [%date% %time%] [INFO] 测试数据库连接...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT 1;" >nul 2>&1
if errorlevel 1 (
    echo [ERROR] 数据库连接失败，请检查以下配置:
    echo   - 主机地址: %DB_HOST%
    echo   - 端口: %DB_PORT%
    echo   - 用户名: %DB_USER%
    echo   - 密码: %DB_PASSWORD%
    echo.
    echo 提示: 可使用参数修改配置，如:
    echo   %~nx0 --host 192.168.1.100 --database my_tunnel
    pause
    exit /b 1
)
echo [%date% %time%] [INFO] 数据库连接测试成功
echo.

REM 检查是否存在备份文件
if not exist "backup\database\full" (
    echo [ERROR] 备份目录不存在: backup\database\full
    echo 请确保解压了完整的恢复包
    pause
    exit /b 1
)

REM 列出可用的备份文件
echo 可用的备份文件:
echo.
setlocal enabledelayedexpansion
set /a count=0
for /r "backup\database\full" %%f in (*.sql.gz) do (
    set /a count+=1
    set "file!count!=%%f"
    for %%A in ("%%f") do set size=%%~zA
    set /a size_kb=!size!/1024
    echo !count!^) %%~nxf ^(!size_kb! KB^)
)

if %count%==0 (
    echo [ERROR] 没有找到备份文件 (*.sql.gz)
    echo 请检查备份目录: backup\database\full
    pause
    exit /b 1
)

echo.
echo 0^) 手动输入备份文件路径
echo.
set /p choice="请选择要恢复的备份文件编号 (1-%count%): "

REM 处理用户选择
if "%choice%"=="0" (
    set /p selected_file="请输入备份文件的完整路径: "
    if not exist "!selected_file!" (
        echo [ERROR] 文件不存在: !selected_file!
        pause
        exit /b 1
    )
) else (
    REM 验证选择
    if %choice% LEQ 0 goto :invalid_choice
    if %choice% GTR %count% goto :invalid_choice
    call set selected_file=%%file%choice%%%
)

echo.
echo [%date% %time%] [INFO] 选择的备份文件: !selected_file!
echo.

REM 检测备份文件中的数据库名称
echo [%date% %time%] [INFO] 分析备份文件...
for /f "tokens=*" %%i in ('gunzip -c "!selected_file!" ^| findstr /r "CREATE DATABASE.*`.*`" ^| head -1') do (
    set backup_line=%%i
)

REM 提取数据库名称（简化处理）
set backup_db_name=
for /f "tokens=3 delims=`" %%a in ("!backup_line!") do set backup_db_name=%%a

if defined backup_db_name (
    if not "!backup_db_name!"=="%DB_NAME%" (
        echo.
        echo [WARN] 检测到数据库名称差异:
        echo   备份文件中的数据库: !backup_db_name!
        echo   目标数据库名称: %DB_NAME%
        echo.
        echo 恢复过程将自动进行数据库名称转换。
        echo.
    )
) else (
    echo [INFO] 无法检测备份文件中的数据库名称，将使用默认恢复流程
)

REM 最终确认
echo ===============================================
echo 恢复确认信息:
echo   备份文件: !selected_file!
echo   目标主机: %DB_HOST%:%DB_PORT%
echo   目标数据库: %DB_NAME%
if defined backup_db_name (
    echo   源数据库: !backup_db_name!
)
echo ===============================================
echo.
echo 警告: 恢复操作将完全覆盖现有数据库 %DB_NAME%！
set /p confirm="确认继续？(Y/N): "
if /i not "%confirm%"=="Y" (
    echo 操作已取消
    pause
    exit /b 0
)

REM 创建安全备份
echo.
echo [%date% %time%] [INFO] 创建安全备份...
if not exist "backup\database\safety" mkdir "backup\database\safety"

REM 检查目标数据库是否存在
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "USE %DB_NAME%;" >nul 2>&1
if not errorlevel 1 (
    echo [%date% %time%] [INFO] 备份现有数据库 %DB_NAME%...
    mysqldump -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% --single-transaction --routines --triggers --events --set-gtid-purged=OFF --default-character-set=utf8mb4 --lock-tables=false --add-drop-database --databases %DB_NAME% 2>nul | gzip > "backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz"
    
    if errorlevel 1 (
        echo [ERROR] 安全备份失败
        pause
        exit /b 1
    )
    echo [%date% %time%] [INFO] 安全备份完成
) else (
    echo [%date% %time%] [INFO] 目标数据库 %DB_NAME% 不存在，跳过安全备份
)

REM 删除现有数据库
echo [%date% %time%] [INFO] 删除现有数据库 %DB_NAME%...
mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "DROP DATABASE IF EXISTS `%DB_NAME%`;" 2>nul

REM 恢复数据库
echo [%date% %time%] [INFO] 恢复数据库...
if defined backup_db_name (
    if not "!backup_db_name!"=="%DB_NAME%" (
        echo [%date% %time%] [INFO] 执行数据库名称转换恢复...
        REM 使用PowerShell进行名称替换和恢复
        powershell -Command "& {$backup='!selected_file!'; $content = & gunzip -c $backup; $content = $content -replace '\`!backup_db_name!\`', '\`%DB_NAME%\`'; $content = $content -replace 'Database: !backup_db_name!', 'Database: %DB_NAME%'; $content | & mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% 2>$null}"
    ) else (
        echo [%date% %time%] [INFO] 执行直接恢复...
        gunzip -c "!selected_file!" | mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% 2>nul
    )
) else (
    echo [%date% %time%] [INFO] 执行直接恢复...
    gunzip -c "!selected_file!" | mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% 2>nul
)

if errorlevel 1 (
    echo [ERROR] 数据库恢复失败
    echo 可以尝试从安全备份恢复: backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz
    pause
    exit /b 1
)

REM 验证恢复结果
echo [%date% %time%] [INFO] 验证恢复结果...
for /f %%i in ('mysql -h%DB_HOST% -P%DB_PORT% -u%DB_USER% -p%DB_PASSWORD% -e "SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='%DB_NAME%';" 2^>nul ^| tail -1') do set table_count=%%i

if "%table_count%"=="" set table_count=0

echo.
echo [%date% %time%] [SUCCESS] 数据库恢复完成！
echo.
echo ===============================================
echo 恢复信息总结:
echo   备份文件: !selected_file!
echo   目标数据库: %DB_NAME%
echo   恢复的表数量: %table_count%
if exist "backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz" (
    echo   安全备份: backup\database\safety\%DB_NAME%_safety_before_restore_%DATE_TIME%.sql.gz
)
echo ===============================================
echo.
pause
exit /b 0

:invalid_choice
echo [ERROR] 无效的选择: %choice%
pause
exit /b 1

:show_help
echo.
echo TunnelManagement 灵活数据库恢复工具
echo.
echo 用法: %~nx0 [选项]
echo.
echo 选项:
echo   --host ^<主机^>       数据库主机地址 (默认: 127.0.0.1)
echo   --port ^<端口^>       数据库端口 (默认: 3306)
echo   --user ^<用户名^>     数据库用户名 (默认: root)
echo   --password ^<密码^>   数据库密码 (默认: Coolhomer)
echo   --database ^<数据库名^> 目标数据库名 (默认: tunnel_management)
echo   --help              显示此帮助信息
echo.
echo 示例:
echo   %~nx0                                    # 使用默认配置
echo   %~nx0 --database tunnel_management_win  # 指定不同的数据库名
echo   %~nx0 --host 192.168.1.100 --user admin # 连接远程数据库
echo.
echo 功能:
echo   - 自动检测备份文件中的数据库名称
echo   - 支持跨数据库名称恢复 (自动转换)
echo   - 恢复前自动创建安全备份
echo   - 完整的恢复验证
echo.
pause
exit /b 0 