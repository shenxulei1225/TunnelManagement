@echo off
chcp 65001 > nul
title TunnelManagement Data Backup Tool

echo ========================================
echo  TunnelManagement Data Backup Tool
echo ========================================
echo.
echo Please select backup type:
echo.
echo 1. Full Backup (Database + Files)
echo 2. Database Only
echo 3. Files Only  
echo 4. Mac-Compatible Database Backup
echo 5. Test Configuration
echo 6. View Logs
echo 7. Help
echo 0. Exit
echo.
set /p choice=Enter option (0-7): 

if "%choice%"=="1" goto full_backup
if "%choice%"=="2" goto database_backup
if "%choice%"=="3" goto files_backup
if "%choice%"=="4" goto mac_backup
if "%choice%"=="5" goto test_config
if "%choice%"=="6" goto view_logs
if "%choice%"=="7" goto help
if "%choice%"=="0" goto exit
goto invalid_choice

:full_backup
echo.
echo Starting full backup...
powershell -ExecutionPolicy Bypass -File "%~dp0backup_data_windows.ps1" -BackupType full
goto end

:database_backup
echo.
echo Starting database backup...
powershell -ExecutionPolicy Bypass -File "%~dp0backup_data_windows.ps1" -BackupType database
goto end

:files_backup
echo.
echo Starting files backup...
powershell -ExecutionPolicy Bypass -File "%~dp0backup_data_windows.ps1" -BackupType files
goto end

:mac_backup
echo.
echo Starting Mac-compatible database backup...
call "%~dp0backup_mac_compatible.bat"
goto end

:test_config
echo.
echo Testing configuration...
powershell -ExecutionPolicy Bypass -File "%~dp0test_backup.ps1"
goto end

:view_logs
echo.
echo Opening logs directory...
start "" "%~dp0..\..\backup\logs"
goto end

:help
echo.
echo ========================================
echo  Help Information
echo ========================================
echo.
echo This tool provides automated backup for:
echo - MySQL/PostgreSQL/SQL Server databases
echo - Important application files
echo - Configuration files
echo.
echo Configuration file: backup_config.json
echo Log location: backup\logs\
echo.
echo For detailed documentation, see:
echo script\shell\README_backup_windows.md
echo.
pause
goto menu

:invalid_choice
echo.
echo Invalid option. Please enter a number between 0-7.
echo.
pause
goto menu

:end
echo.
echo Backup task completed!
echo Check logs for details: backup\logs\
echo.
pause
goto menu

:menu
cls
goto :eof

:exit
echo.
echo Goodbye!
timeout /t 2 > nul
exit 