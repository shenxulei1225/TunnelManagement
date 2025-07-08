@echo off
chcp 65001 > nul
title TunnelManagement Data Restore Tool

:menu
cls
echo ========================================
echo  TunnelManagement Data Restore Tool
echo ========================================
echo.
echo Please select restore type:
echo.
echo 1. Full Restore (Database + Files)
echo 2. Database Only
echo 3. Files Only
echo 4. List Available Backups
echo 5. Test Database Connection
echo 6. View Restore Logs
echo 7. Help
echo 0. Exit
echo.
set /p choice=Enter option (0-7): 

if "%choice%"=="1" goto full_restore
if "%choice%"=="2" goto database_restore
if "%choice%"=="3" goto files_restore
if "%choice%"=="4" goto list_backups
if "%choice%"=="5" goto test_connection
if "%choice%"=="6" goto view_logs
if "%choice%"=="7" goto help
if "%choice%"=="0" goto exit
goto invalid_choice

:full_restore
echo.
echo Starting full restore...
echo.
echo ⚠️  WARNING: This will overwrite existing data!
set /p confirm=Are you sure you want to continue? (y/N): 
if /i not "%confirm%"=="y" goto menu

powershell -ExecutionPolicy Bypass -File "%~dp0restore_data_windows.ps1" -RestoreType full
goto end

:database_restore
echo.
echo Starting database restore...
echo.
echo ⚠️  WARNING: This will overwrite existing database!
set /p confirm=Are you sure you want to continue? (y/N): 
if /i not "%confirm%"=="y" goto menu

powershell -ExecutionPolicy Bypass -File "%~dp0restore_data_windows.ps1" -RestoreType database
goto end

:files_restore
echo.
echo Starting files restore...
echo.
echo ⚠️  WARNING: This will overwrite existing files!
set /p confirm=Are you sure you want to continue? (y/N): 
if /i not "%confirm%"=="y" goto menu

powershell -ExecutionPolicy Bypass -File "%~dp0restore_data_windows.ps1" -RestoreType files
goto end

:list_backups
echo.
echo Listing available backups...
powershell -ExecutionPolicy Bypass -File "%~dp0list_backups_windows.ps1"
echo.
pause
goto menu

:test_connection
echo.
echo Testing database connection...
powershell -ExecutionPolicy Bypass -File "%~dp0test_connection_windows.ps1"
echo.
pause
goto menu

:view_logs
echo.
echo Opening logs directory...
if exist "%~dp0..\..\backup\logs" (
    start "" "%~dp0..\..\backup\logs"
) else (
    echo No logs directory found.
)
goto menu

:help
echo.
echo ========================================
echo  Help Information
echo ========================================
echo.
echo This tool provides restore functionality for:
echo - MySQL/PostgreSQL/SQL Server databases
echo - Application files and configurations
echo.
echo Before restoring:
echo 1. Ensure database service is running
echo 2. Verify backup files are accessible
echo 3. Check configuration in restore_config.json
echo.
echo Configuration file: restore_config.json
echo Log location: backup\logs\
echo.
echo For detailed documentation, see:
echo script\shell\README_restore_windows.md
echo.
echo ⚠️  Important Notes:
echo - Always create a backup before restoring
echo - Restore operations will overwrite existing data
echo - Ensure sufficient disk space is available
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
echo Restore task completed!
echo Check logs for details: backup\logs\
echo.
pause
goto menu

:exit
echo.
echo Goodbye!
timeout /t 2 > nul
exit 