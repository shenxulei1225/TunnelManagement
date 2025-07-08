# List Available Backups Script
# Author: TunnelManagement Team
# Version: 1.0

param(
    [string]$ConfigFile = "backup_config.json",
    [string]$BackupType = "all"  # all, database, files
)

# Get script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Split-Path -Parent (Split-Path -Parent $ScriptDir)
$ConfigPath = Join-Path $ScriptDir $ConfigFile

# Read configuration
function Read-Config {
    if (!(Test-Path $ConfigPath)) {
        Write-Host "Configuration file not found: $ConfigPath" -ForegroundColor Red
        return $null
    }
    
    try {
        return Get-Content $ConfigPath | ConvertFrom-Json
    }
    catch {
        Write-Host "Failed to read configuration file: $($_.Exception.Message)" -ForegroundColor Red
        return $null
    }
}

# Format file size
function Format-FileSize {
    param($Size)
    
    if ($Size -gt 1GB) {
        return "{0:N2} GB" -f ($Size / 1GB)
    }
    elseif ($Size -gt 1MB) {
        return "{0:N2} MB" -f ($Size / 1MB)
    }
    elseif ($Size -gt 1KB) {
        return "{0:N2} KB" -f ($Size / 1KB)
    }
    else {
        return "$Size Bytes"
    }
}

# List database backups
function List-DatabaseBackups {
    param($config)
    
    $dbBackupPath = Join-Path $config.backup.base_path $config.database.backup_path
    
    if (!(Test-Path $dbBackupPath)) {
        Write-Host "Database backup directory not found: $dbBackupPath" -ForegroundColor Yellow
        return
    }
    
    $backups = Get-ChildItem -Path $dbBackupPath -Filter "*.sql*" -Recurse | Sort-Object CreationTime -Descending
    
    if ($backups.Count -eq 0) {
        Write-Host "No database backups found" -ForegroundColor Yellow
        return
    }
    
    Write-Host "`n📁 Database Backups:" -ForegroundColor Cyan
    Write-Host "=" * 80
    Write-Host "No.  Name                                    Size        Date Created        Age"
    Write-Host "-" * 80
    
    for ($i = 0; $i -lt $backups.Count; $i++) {
        $backup = $backups[$i]
        $size = Format-FileSize $backup.Length
        $date = $backup.CreationTime.ToString("yyyy-MM-dd HH:mm:ss")
        $age = (Get-Date) - $backup.CreationTime
        $ageStr = if ($age.Days -gt 0) { 
            "$($age.Days)d $($age.Hours)h" 
        } elseif ($age.Hours -gt 0) { 
            "$($age.Hours)h $($age.Minutes)m" 
        } else { 
            "$($age.Minutes)m" 
        }
        
        $color = if ($age.Days -gt 7) { "Red" } elseif ($age.Days -gt 3) { "Yellow" } else { "Green" }
        
        Write-Host ("{0,3}. {1,-35} {2,10} {3,19} {4,8}" -f ($i + 1), $backup.Name, $size, $date, $ageStr) -ForegroundColor $color
    }
    
    Write-Host ""
    Write-Host "Total: $($backups.Count) database backups" -ForegroundColor White
}

# List file backups
function List-FileBackups {
    param($config)
    
    $fileBackupPath = Join-Path $config.backup.base_path $config.files.backup_path
    
    if (!(Test-Path $fileBackupPath)) {
        Write-Host "File backup directory not found: $fileBackupPath" -ForegroundColor Yellow
        return
    }
    
    $backups = Get-ChildItem -Path $fileBackupPath -Filter "*.zip" -Recurse | Sort-Object CreationTime -Descending
    
    if ($backups.Count -eq 0) {
        Write-Host "No file backups found" -ForegroundColor Yellow
        return
    }
    
    Write-Host "`n📂 File Backups:" -ForegroundColor Cyan
    Write-Host "=" * 80
    Write-Host "No.  Name                                    Size        Date Created        Age"
    Write-Host "-" * 80
    
    for ($i = 0; $i -lt $backups.Count; $i++) {
        $backup = $backups[$i]
        $size = Format-FileSize $backup.Length
        $date = $backup.CreationTime.ToString("yyyy-MM-dd HH:mm:ss")
        $age = (Get-Date) - $backup.CreationTime
        $ageStr = if ($age.Days -gt 0) { 
            "$($age.Days)d $($age.Hours)h" 
        } elseif ($age.Hours -gt 0) { 
            "$($age.Hours)h $($age.Minutes)m" 
        } else { 
            "$($age.Minutes)m" 
        }
        
        $color = if ($age.Days -gt 7) { "Red" } elseif ($age.Days -gt 3) { "Yellow" } else { "Green" }
        
        Write-Host ("{0,3}. {1,-35} {2,10} {3,19} {4,8}" -f ($i + 1), $backup.Name, $size, $date, $ageStr) -ForegroundColor $color
    }
    
    Write-Host ""
    Write-Host "Total: $($backups.Count) file backups" -ForegroundColor White
}

# List backup statistics
function Show-BackupStatistics {
    param($config)
    
    $backupBasePath = $config.backup.base_path
    
    if (!(Test-Path $backupBasePath)) {
        Write-Host "Backup directory not found: $backupBasePath" -ForegroundColor Red
        return
    }
    
    # Calculate total size
    $totalSize = (Get-ChildItem -Path $backupBasePath -Recurse -File | Measure-Object -Property Length -Sum).Sum
    
    # Count files by type
    $sqlFiles = (Get-ChildItem -Path $backupBasePath -Filter "*.sql*" -Recurse).Count
    $zipFiles = (Get-ChildItem -Path $backupBasePath -Filter "*.zip" -Recurse).Count
    $logFiles = (Get-ChildItem -Path $backupBasePath -Filter "*.log" -Recurse).Count
    
    # Find oldest and newest backups
    $allBackups = Get-ChildItem -Path $backupBasePath -Recurse -File | Where-Object { $_.Extension -in @('.sql', '.gz', '.zip') }
    $oldestBackup = $allBackups | Sort-Object CreationTime | Select-Object -First 1
    $newestBackup = $allBackups | Sort-Object CreationTime -Descending | Select-Object -First 1
    
    Write-Host "`n📊 Backup Statistics:" -ForegroundColor Cyan
    Write-Host "=" * 50
    Write-Host "Backup Directory: $backupBasePath"
    Write-Host "Total Size: $(Format-FileSize $totalSize)"
    Write-Host "Database Backups: $sqlFiles"
    Write-Host "File Backups: $zipFiles"
    Write-Host "Log Files: $logFiles"
    
    if ($oldestBackup) {
        Write-Host "Oldest Backup: $($oldestBackup.Name) ($($oldestBackup.CreationTime.ToString('yyyy-MM-dd HH:mm:ss')))"
    }
    
    if ($newestBackup) {
        Write-Host "Newest Backup: $($newestBackup.Name) ($($newestBackup.CreationTime.ToString('yyyy-MM-dd HH:mm:ss')))"
    }
}

# Main function
function Main {
    Write-Host "TunnelManagement Backup Listing Tool" -ForegroundColor Green
    Write-Host "=" * 50
    
    $config = Read-Config
    if (!$config) {
        return 1
    }
    
    # Show backup statistics
    Show-BackupStatistics $config
    
    # List backups based on type
    switch ($BackupType.ToLower()) {
        "all" {
            List-DatabaseBackups $config
            List-FileBackups $config
        }
        "database" {
            List-DatabaseBackups $config
        }
        "files" {
            List-FileBackups $config
        }
        default {
            Write-Host "Invalid backup type: $BackupType" -ForegroundColor Red
            Write-Host "Valid types: all, database, files"
            return 1
        }
    }
    
    Write-Host "`n💡 Tips:" -ForegroundColor Cyan
    Write-Host "- Green: Recent backups (< 3 days)"
    Write-Host "- Yellow: Older backups (3-7 days)"
    Write-Host "- Red: Old backups (> 7 days)"
    Write-Host ""
    
    return 0
}

# Execute main function
exit (Main) 