@echo off
chcp 65001 > nul
title Quick Mac Backup

echo ============================================
echo  快速Mac兼容备份
echo ============================================
echo.

REM 直接执行备份，不需要确认
powershell -ExecutionPolicy Bypass -File "%~dp0backup_mac_compatible.ps1" -BackupType full -Compress

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo  ✓ 备份成功完成！
    echo ============================================
    echo.
    echo 备份文件位置:
    echo backup\mac_compatible\
    echo.
    echo 最新备份:
    echo backup\mac_compatible\latest_mac_backup.sql.gz
    echo.
    echo Mac恢复步骤:
    echo 1. 复制 .sql.gz 文件到Mac
    echo 2. gunzip filename.sql.gz
    echo 3. mysql -u root -p tunnel_management ^< filename.sql
    echo.
) else (
    echo.
    echo ============================================
    echo  ✗ 备份失败
    echo ============================================
    echo 请查看日志文件获取详细信息
    echo.
)

echo 按任意键退出...
pause > nul
