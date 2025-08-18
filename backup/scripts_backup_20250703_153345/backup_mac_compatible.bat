@echo off
chcp 65001 > nul
title Mac-Compatible Database Backup

echo ============================================
echo  Mac-Compatible Database Backup Tool
echo ============================================
echo.
echo This tool creates MySQL backups that can be
echo restored on Mac/Linux systems.
echo.
echo Features:
echo - UTF-8 encoding without BOM
echo - Unix line endings (LF)
echo - Cross-platform SQL syntax
echo - Optional gzip compression
echo ============================================
echo.

set /p confirm=Do you want to create a Mac-compatible backup? (Y/N): 

if /i "%confirm%"=="Y" goto start_backup
if /i "%confirm%"=="N" goto cancel
goto invalid_input

:start_backup
echo.
echo Starting Mac-compatible backup...
echo.

REM 运行PowerShell脚本
powershell -ExecutionPolicy Bypass -File "%~dp0backup_mac_compatible.ps1" -BackupType full -Compress

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ============================================
    echo  Backup completed successfully!
    echo ============================================
    echo.
    echo Backup location: backup\mac_compatible\
    echo.
    echo To restore on Mac:
    echo 1. Copy the .sql.gz file to your Mac
    echo 2. Run: gunzip backup_file.sql.gz
    echo 3. Run: mysql -u root -p database_name ^< backup_file.sql
    echo.
) else (
    echo.
    echo ============================================
    echo  Backup failed!
    echo ============================================
    echo Check the log file for details.
    echo.
)

pause
goto end

:cancel
echo.
echo Backup cancelled.
echo.
pause
goto end

:invalid_input
echo.
echo Invalid input. Please enter Y or N.
echo.
pause
cls
goto :start

:end
exit
