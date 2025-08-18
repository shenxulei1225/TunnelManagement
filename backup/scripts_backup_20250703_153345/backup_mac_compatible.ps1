# ============================================
# TunnelManagement Mac-Compatible Backup Script
# 生成可在Mac/Linux系统恢复的MySQL备份
# ============================================

param(
    [string]$BackupType = "full",
    [switch]$Compress = $true,
    [switch]$ValidateOnly = $false
)

# 设置错误处理
$ErrorActionPreference = "Stop"

# 获取脚本目录
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$ProjectRoot = Split-Path -Parent (Split-Path -Parent $ScriptDir)

# 加载配置
$ConfigFile = Join-Path $ScriptDir "backup_config.json"
if (-not (Test-Path $ConfigFile)) {
    Write-Error "Configuration file not found: $ConfigFile"
    exit 1
}

$Config = Get-Content $ConfigFile | ConvertFrom-Json

# 设置变量
$Timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
$YearMonth = Get-Date -Format "yyyyMM"
$BackupBasePath = $Config.backup.base_path
$DbConfig = $Config.database

# 创建备份目录
$MacBackupDir = Join-Path $BackupBasePath "mac_compatible"
$MacBackupMonthDir = Join-Path $MacBackupDir $YearMonth
$LogDir = Join-Path $BackupBasePath "logs"

# 确保目录存在
@($MacBackupDir, $MacBackupMonthDir, $LogDir) | ForEach-Object {
    if (-not (Test-Path $_)) {
        New-Item -ItemType Directory -Path $_ -Force | Out-Null
    }
}

# 日志文件
$LogFile = Join-Path $LogDir "backup_mac_${Timestamp}.log"

# 日志函数
function Write-Log {
    param(
        [string]$Message,
        [string]$Level = "INFO"
    )
    $LogMessage = "$(Get-Date -Format 'yyyy-MM-dd HH:mm:ss') [$Level] $Message"
    Add-Content -Path $LogFile -Value $LogMessage
    
    switch ($Level) {
        "ERROR" { Write-Host $LogMessage -ForegroundColor Red }
        "WARN" { Write-Host $LogMessage -ForegroundColor Yellow }
        "SUCCESS" { Write-Host $LogMessage -ForegroundColor Green }
        default { Write-Host $LogMessage }
    }
}

# 测试数据库连接
function Test-DatabaseConnection {
    Write-Log "Testing database connection..."
    
    $mysqlPath = "mysql"
    
    # 构建连接命令
    $mysqlArgs = @(
        "-h", $DbConfig.host,
        "-P", $DbConfig.port,
        "-u", $DbConfig.username,
        "-p$($DbConfig.password)",
        "-e", "SELECT VERSION();"
    )
    
    try {
        # 使用cmd.exe来执行mysql命令，避免PowerShell的错误处理问题
        $mysqlCmd = "mysql -h $($DbConfig.host) -P $($DbConfig.port) -u $($DbConfig.username) -p$($DbConfig.password) -e `"SELECT VERSION();`" 2>nul"
        $result = cmd /c $mysqlCmd
        
        if ($LASTEXITCODE -eq 0) {
            Write-Log "Database connection successful" "SUCCESS"
            return $true
        } else {
            Write-Log "Database connection failed" "ERROR"
            return $false
        }
    } catch {
        Write-Log "Failed to connect to database: $_" "ERROR"
        return $false
    }
}

# 执行Mac兼容备份
function Backup-ForMac {
    param(
        [string]$Database = $DbConfig.databases[0]
    )
    
    Write-Log "Starting Mac-compatible backup for database: $Database"
    
    # 设置备份文件路径
    $BackupFileName = "${Database}_mac_compatible_${Timestamp}.sql"
    $BackupFilePath = Join-Path $MacBackupMonthDir $BackupFileName
    $TempFilePath = "${BackupFilePath}.tmp"
    
    # mysqldump路径
    $mysqldumpPath = "mysqldump"
    
    # Mac兼容的mysqldump参数
    $mysqldumpArgs = @(
        "-h", $DbConfig.host,
        "-P", $DbConfig.port,
        "-u", $DbConfig.username,
        "-p$($DbConfig.password)",
        "--single-transaction",
        "--routines",
        "--triggers",
        "--events",
        "--set-gtid-purged=OFF",
        "--default-character-set=utf8mb4",
        "--hex-blob",                      # 二进制数据使用十六进制
        "--skip-extended-insert",           # 每行一个INSERT语句，提高可读性
        "--complete-insert",                # 包含列名
        "--skip-comments",                  # 跳过注释，减少平台差异
        "--skip-set-charset",               # 跳过字符集设置
        "--no-create-db",                   # 不创建数据库语句
        "--skip-add-drop-table",           # 跳过DROP TABLE语句
        "--skip-disable-keys",              # 跳过禁用键的语句
        "--skip-add-locks",                 # 跳过锁表语句
        "--result-file=$TempFilePath",     # 直接输出到文件
        $Database
    )
    
    Write-Log "Executing mysqldump with Mac-compatible options..."
    
    try {
        # 使用cmd.exe执行mysqldump，避免PowerShell的输出重定向问题
        $mysqldumpCmd = "mysqldump -h $($DbConfig.host) -P $($DbConfig.port) -u $($DbConfig.username) -p$($DbConfig.password) --single-transaction --routines --triggers --events --set-gtid-purged=OFF --default-character-set=utf8mb4 --hex-blob --skip-extended-insert --complete-insert --skip-comments --skip-set-charset --no-create-db --skip-add-drop-table --skip-disable-keys --skip-add-locks $Database > `"$TempFilePath`" 2>nul"
        
        $result = cmd /c $mysqldumpCmd
        
        if ($LASTEXITCODE -eq 0) {
            Write-Log "mysqldump completed successfully" "SUCCESS"
            
            # 处理文件编码和换行符
            Write-Log "Converting file format for Mac compatibility..."
            
            # 读取文件内容
            $content = Get-Content -Path $TempFilePath -Raw -Encoding UTF8
            
            # 添加Mac兼容的头部
            $header = @"
-- Mac/Linux Compatible MySQL Dump
-- Generated on Windows for cross-platform compatibility
-- Database: $Database
-- Generation Date: $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')
-- MySQL Version: 8.x compatible

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
SET SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO';
SET TIME_ZONE = '+00:00';

USE ``$Database``;

"@
            
            # 添加尾部
            $footer = @"

SET FOREIGN_KEY_CHECKS = 1;
-- End of dump
"@
            
            # 组合内容
            $finalContent = $header + $content + $footer
            
            # 转换Windows路径为Unix格式（如果有）
            $finalContent = $finalContent -replace '\\\\', '/'
            $finalContent = $finalContent -replace '\r\n', "`n"  # CRLF to LF
            
            # 保存为UTF-8无BOM格式
            $utf8NoBom = New-Object System.Text.UTF8Encoding $false
            [System.IO.File]::WriteAllText($BackupFilePath, $finalContent, $utf8NoBom)
            
            # 删除临时文件
            Remove-Item -Path $TempFilePath -Force -ErrorAction SilentlyContinue
            
            # 获取文件大小
            $fileSize = (Get-Item $BackupFilePath).Length / 1MB
            Write-Log "Backup file created: $BackupFilePath (Size: $([math]::Round($fileSize, 2)) MB)" "SUCCESS"
            
            # 压缩备份（如果启用）
            if ($Compress) {
                Write-Log "Compressing backup file..."
                $compressedFile = "${BackupFilePath}.gz"
                
                # 使用.NET进行gzip压缩
                $input = [System.IO.File]::OpenRead($BackupFilePath)
                $output = [System.IO.File]::Create($compressedFile)
                $gzipStream = New-Object System.IO.Compression.GzipStream $output, ([System.IO.Compression.CompressionMode]::Compress)
                
                $buffer = New-Object byte[] 4096
                while (($read = $input.Read($buffer, 0, $buffer.Length)) -gt 0) {
                    $gzipStream.Write($buffer, 0, $read)
                }
                
                $gzipStream.Close()
                $output.Close()
                $input.Close()
                
                $compressedSize = (Get-Item $compressedFile).Length / 1MB
                Write-Log "Compressed file created: $compressedFile (Size: $([math]::Round($compressedSize, 2)) MB)" "SUCCESS"
                
                # 复制到最新备份文件（避免符号链接权限问题）
                $latestLink = Join-Path $MacBackupDir "latest_mac_backup.sql.gz"
                try {
                    Copy-Item -Path $compressedFile -Destination $latestLink -Force
                    Write-Log "Created latest backup copy: $latestLink" "SUCCESS"
                } catch {
                    Write-Log "Could not create latest backup copy (non-critical): $_" "WARN"
                }
                
                # 可选：删除未压缩的文件
                # Remove-Item -Path $BackupFilePath -Force
            }
            
            return $true
            
        } else {
            Write-Log "mysqldump failed with exit code: $LASTEXITCODE" "ERROR"
            Remove-Item -Path $TempFilePath -Force -ErrorAction SilentlyContinue
            return $false
        }
    } catch {
        Write-Log "Backup failed: $_" "ERROR"
        Remove-Item -Path $TempFilePath -Force -ErrorAction SilentlyContinue
        return $false
    }
}

# 验证备份文件
function Validate-BackupFile {
    param(
        [string]$FilePath
    )
    
    Write-Log "Validating backup file: $FilePath"
    
    if (-not (Test-Path $FilePath)) {
        Write-Log "Backup file not found" "ERROR"
        return $false
    }
    
    # 检查文件大小
    $fileSize = (Get-Item $FilePath).Length
    if ($fileSize -eq 0) {
        Write-Log "Backup file is empty" "ERROR"
        return $false
    }
    
    # 检查SQL语法（基本验证）
    $content = Get-Content -Path $FilePath -First 100 -ErrorAction SilentlyContinue
    if ($content -match "CREATE TABLE|INSERT INTO|USE ") {
        Write-Log "Backup file contains valid SQL statements" "SUCCESS"
        return $true
    } else {
        Write-Log "Backup file may not contain valid SQL" "WARN"
        return $true  # 仍然返回true，因为可能是压缩文件
    }
}

# 清理旧备份
function Cleanup-OldBackups {
    $RetentionDays = $Config.backup.retention_days
    Write-Log "Cleaning up backups older than $RetentionDays days..."
    
    $cutoffDate = (Get-Date).AddDays(-$RetentionDays)
    
    Get-ChildItem -Path $MacBackupDir -Recurse -File | Where-Object {
        $_.LastWriteTime -lt $cutoffDate -and ($_.Extension -eq ".sql" -or $_.Extension -eq ".gz")
    } | ForEach-Object {
        Write-Log "Removing old backup: $($_.Name)"
        Remove-Item $_.FullName -Force
    }
}

# 显示备份信息
function Show-BackupInfo {
    Write-Log "=== Mac-Compatible Backup Information ==="
    
    $backupFiles = Get-ChildItem -Path $MacBackupDir -Recurse -File | Where-Object {
        $_.Extension -eq ".sql" -or $_.Extension -eq ".gz"
    }
    
    $totalSize = ($backupFiles | Measure-Object -Property Length -Sum).Sum / 1GB
    $fileCount = $backupFiles.Count
    
    Write-Log "Total backup files: $fileCount"
    Write-Log "Total size: $([math]::Round($totalSize, 2)) GB"
    
    # 显示最近的备份
    $recentBackups = $backupFiles | Sort-Object LastWriteTime -Descending | Select-Object -First 5
    if ($recentBackups) {
        Write-Log "Recent backups:"
        foreach ($backup in $recentBackups) {
            $size = [math]::Round($backup.Length / 1MB, 2)
            Write-Log "  - $($backup.Name) (${size} MB) - $($backup.LastWriteTime)"
        }
    }
}

# 主执行流程
function Main {
    Write-Log "========================================" 
    Write-Log "Mac-Compatible Backup Script Started"
    Write-Log "Backup Type: $BackupType"
    Write-Log "========================================"
    
    # 测试数据库连接
    if (-not (Test-DatabaseConnection)) {
        Write-Log "Cannot proceed without database connection" "ERROR"
        exit 1
    }
    
    # 执行备份
    $success = $true
    foreach ($database in $DbConfig.databases) {
        if (-not (Backup-ForMac -Database $database)) {
            $success = $false
            break
        }
    }
    
    if ($success) {
        # 清理旧备份
        Cleanup-OldBackups
        
        # 显示备份信息
        Show-BackupInfo
        
        Write-Log "========================================" 
        Write-Log "Backup completed successfully!" "SUCCESS"
        Write-Log "========================================"
        
        # 显示恢复说明
        Write-Host ""
        Write-Host "To restore on Mac/Linux:" -ForegroundColor Cyan
        Write-Host "1. Copy the backup file to your Mac/Linux system" -ForegroundColor Yellow
        Write-Host "2. If compressed (.gz), decompress: gunzip backup_file.sql.gz" -ForegroundColor Yellow
        Write-Host "3. Create database if not exists: mysql -u root -p -e 'CREATE DATABASE IF NOT EXISTS $($DbConfig.databases[0])'" -ForegroundColor Yellow
        Write-Host "4. Restore: mysql -u root -p $($DbConfig.databases[0]) < backup_file.sql" -ForegroundColor Yellow
        Write-Host ""
    } else {
        Write-Log "Backup failed!" "ERROR"
        exit 1
    }
}

# 运行主函数
Main
