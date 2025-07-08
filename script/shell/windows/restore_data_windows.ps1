# Windows Data Restore Script
# Author: TunnelManagement Team
# Version: 1.0
# Description: Restore database and files from backup

param(
    [string]$RestoreType = "full",  # full, database, files
    [string]$ConfigFile = "restore_config.json",
    [string]$BackupPath = "",
    [switch]$Force = $false,
    [switch]$Verbose = $false
)

# Set error handling
$ErrorActionPreference = "Stop"

# Get script directory
$ScriptDir = Split-Path -Parent $MyInvocation.MyCommand.Definition
$ProjectRoot = Split-Path -Parent (Split-Path -Parent $ScriptDir)

# Configuration file path
$ConfigPath = Join-Path $ScriptDir $ConfigFile

# Log configuration
$LogDir = Join-Path $ProjectRoot "backup\logs"
$LogFile = Join-Path $LogDir "restore_$(Get-Date -Format 'yyyyMMdd_HHmmss').log"

# Create log directory
if (!(Test-Path $LogDir)) {
    New-Item -ItemType Directory -Path $LogDir -Force | Out-Null
}

# Logging function
function Write-Log {
    param(
        [string]$Message,
        [string]$Level = "INFO"
    )
    
    $Timestamp = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $LogEntry = "[$Timestamp] [$Level] $Message"
    
    Write-Host $LogEntry -ForegroundColor $(
        switch ($Level) {
            "ERROR" { "Red" }
            "WARN" { "Yellow" }
            "SUCCESS" { "Green" }
            default { "White" }
        }
    )
    
    Add-Content -Path $LogFile -Value $LogEntry
}

# Read configuration file
function Read-Config {
    if (!(Test-Path $ConfigPath)) {
        Write-Log "Configuration file not found, creating default config: $ConfigPath" "WARN"
        Create-DefaultConfig
    }
    
    try {
        $config = Get-Content $ConfigPath | ConvertFrom-Json
        Write-Log "Configuration file loaded successfully"
        return $config
    }
    catch {
        Write-Log "Failed to read configuration file: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Create default configuration
function Create-DefaultConfig {
    $defaultConfig = @{
        backup = @{
            base_path = "E:\TunnelManagement\backup"
        }
        database = @{
            enabled = $true
            type = "mysql"  # mysql, postgresql, sqlserver
            host = "localhost"
            port = 3306
            username = "root"
            password = "Coolhomer"
            databases = @("tunnel_management")
            backup_path = "database"
            create_safety_backup = $true
        }
        files = @{
            enabled = $true
            backup_path = "files"
            restore_targets = @(
                @{
                    name = "Application Config"
                    target_path = "yudao-server\src\main\resources"
                },
                @{
                    name = "Upload Files"
                    target_path = "upload"
                },
                @{
                    name = "Important Scripts"
                    target_path = "script"
                }
            )
        }
    }
    
    $defaultConfig | ConvertTo-Json -Depth 10 | Set-Content $ConfigPath
    Write-Log "Default configuration file created: $ConfigPath"
}

# List available backups
function Get-AvailableBackups {
    param($config, $type)
    
    $backupBasePath = $config.backup.base_path
    $backups = @()
    
    switch ($type) {
        "database" {
            $dbBackupPath = Join-Path $backupBasePath $config.database.backup_path
            if (Test-Path $dbBackupPath) {
                $backups = Get-ChildItem -Path $dbBackupPath -Filter "*.sql*" -Recurse | Sort-Object CreationTime -Descending
            }
        }
        "files" {
            $fileBackupPath = Join-Path $backupBasePath $config.files.backup_path
            if (Test-Path $fileBackupPath) {
                $backups = Get-ChildItem -Path $fileBackupPath -Filter "*.zip" -Recurse | Sort-Object CreationTime -Descending
            }
        }
    }
    
    return $backups
}

# Select backup interactively
function Select-Backup {
    param($backups, $type)
    
    if ($backups.Count -eq 0) {
        Write-Log "No $type backups found" "WARN"
        return $null
    }
    
    Write-Host "`nAvailable $type backups:" -ForegroundColor Cyan
    Write-Host "=" * 50
    
    for ($i = 0; $i -lt $backups.Count; $i++) {
        $backup = $backups[$i]
        $size = Format-FileSize $backup.Length
        $date = $backup.CreationTime.ToString("yyyy-MM-dd HH:mm:ss")
        Write-Host "$($i + 1). $($backup.Name) ($size) - $date"
    }
    
    Write-Host "0. Cancel"
    Write-Host ""
    
    do {
        $choice = Read-Host "Select backup number (0-$($backups.Count))"
        $choiceNum = 0
        if ([int]::TryParse($choice, [ref]$choiceNum) -and $choiceNum -ge 0 -and $choiceNum -le $backups.Count) {
            break
        }
        Write-Host "Invalid choice. Please enter a number between 0 and $($backups.Count)" -ForegroundColor Red
    } while ($true)
    
    if ($choiceNum -eq 0) {
        return $null
    }
    
    return $backups[$choiceNum - 1]
}

# Test database connection
function Test-DatabaseConnection {
    param($config)
    
    $dbConfig = $config.database
    
    try {
        switch ($dbConfig.type.ToLower()) {
            "mysql" {
                $mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
                if (!$mysqlPath) {
                    throw "mysql command not found"
                }
                
                $arguments = @(
                    "--host=$($dbConfig.host)",
                    "--port=$($dbConfig.port)",
                    "--user=$($dbConfig.username)",
                    "--execute=SELECT 1"
                )
                
                if ($dbConfig.password) {
                    $arguments += "--password=$($dbConfig.password)"
                }
                
                $result = & mysql @arguments 2>&1
                if ($LASTEXITCODE -ne 0) {
                    throw "Connection failed: $result"
                }
            }
            "postgresql" {
                $env:PGPASSWORD = $dbConfig.password
                $result = & psql -h $dbConfig.host -p $dbConfig.port -U $dbConfig.username -c "SELECT 1" 2>&1
                if ($LASTEXITCODE -ne 0) {
                    throw "Connection failed: $result"
                }
            }
            "sqlserver" {
                $connectionString = "Server=$($dbConfig.host),$($dbConfig.port);Database=master;User Id=$($dbConfig.username);Password=$($dbConfig.password);"
                $connection = New-Object System.Data.SqlClient.SqlConnection($connectionString)
                $connection.Open()
                $connection.Close()
            }
        }
        
        Write-Log "Database connection test successful" "SUCCESS"
        return $true
    }
    catch {
        Write-Log "Database connection test failed: $($_.Exception.Message)" "ERROR"
        return $false
    }
}

# Create safety backup
function Create-SafetyBackup {
    param($config)
    
    if (!$config.database.create_safety_backup) {
        return
    }
    
    $dbConfig = $config.database
    $safetyDir = Join-Path $config.backup.base_path "safety"
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    
    if (!(Test-Path $safetyDir)) {
        New-Item -ItemType Directory -Path $safetyDir -Force | Out-Null
    }
    
    Write-Log "Creating safety backup before restore..."
    
    foreach ($database in $dbConfig.databases) {
        try {
            $safetyFile = Join-Path $safetyDir "${database}_safety_${timestamp}.sql"
            
            switch ($dbConfig.type.ToLower()) {
                "mysql" {
                    $arguments = @(
                        "--host=$($dbConfig.host)",
                        "--port=$($dbConfig.port)",
                        "--user=$($dbConfig.username)",
                        "--single-transaction",
                        "--routines",
                        "--triggers",
                        "--databases",
                        $database
                    )
                    
                    if ($dbConfig.password) {
                        $arguments += "--password=$($dbConfig.password)"
                    }
                    
                    & mysqldump @arguments > $safetyFile
                }
                "postgresql" {
                    $env:PGPASSWORD = $dbConfig.password
                    & pg_dump -h $dbConfig.host -p $dbConfig.port -U $dbConfig.username -d $database -f $safetyFile
                }
            }
            
            if (Test-Path $safetyFile) {
                # Compress safety backup
                $compressedFile = "$safetyFile.gz"
                Compress-File $safetyFile $compressedFile
                Remove-Item $safetyFile -Force
                
                Write-Log "Safety backup created: $compressedFile" "SUCCESS"
            }
        }
        catch {
            Write-Log "Failed to create safety backup for $database: $($_.Exception.Message)" "ERROR"
        }
    }
}

# Restore database
function Restore-Database {
    param($config)
    
    if (!$config.database.enabled) {
        Write-Log "Database restore is disabled" "WARN"
        return
    }
    
    # Test database connection
    if (!(Test-DatabaseConnection $config)) {
        throw "Database connection failed"
    }
    
    # Get available backups
    $backups = Get-AvailableBackups $config "database"
    $selectedBackup = Select-Backup $backups "database"
    
    if (!$selectedBackup) {
        Write-Log "No backup selected, skipping database restore" "WARN"
        return
    }
    
    Write-Log "Selected backup: $($selectedBackup.FullName)"
    
    # Create safety backup
    Create-SafetyBackup $config
    
    # Extract database name from backup filename
    $backupName = $selectedBackup.BaseName
    $databaseName = $backupName -replace '_\d{8}_\d{6}.*$', ''
    
    if ($selectedBackup.Extension -eq ".gz") {
        $databaseName = $databaseName -replace '\.sql$', ''
    }
    
    Write-Log "Restoring database: $databaseName"
    
    try {
        $dbConfig = $config.database
        
        # Drop existing database
        Write-Log "Dropping existing database: $databaseName"
        switch ($dbConfig.type.ToLower()) {
            "mysql" {
                $arguments = @(
                    "--host=$($dbConfig.host)",
                    "--port=$($dbConfig.port)",
                    "--user=$($dbConfig.username)",
                    "--execute=DROP DATABASE IF EXISTS \`$databaseName\`"
                )
                
                if ($dbConfig.password) {
                    $arguments += "--password=$($dbConfig.password)"
                }
                
                & mysql @arguments
            }
        }
        
        # Restore database
        Write-Log "Restoring database from backup..."
        
        if ($selectedBackup.Extension -eq ".gz") {
            # Decompress and restore
            $tempFile = [System.IO.Path]::GetTempFileName()
            Expand-GzipFile $selectedBackup.FullName $tempFile
            
            switch ($dbConfig.type.ToLower()) {
                "mysql" {
                    $arguments = @(
                        "--host=$($dbConfig.host)",
                        "--port=$($dbConfig.port)",
                        "--user=$($dbConfig.username)"
                    )
                    
                    if ($dbConfig.password) {
                        $arguments += "--password=$($dbConfig.password)"
                    }
                    
                    Get-Content $tempFile | & mysql @arguments
                }
            }
            
            Remove-Item $tempFile -Force
        }
        else {
            # Direct restore
            switch ($dbConfig.type.ToLower()) {
                "mysql" {
                    $arguments = @(
                        "--host=$($dbConfig.host)",
                        "--port=$($dbConfig.port)",
                        "--user=$($dbConfig.username)"
                    )
                    
                    if ($dbConfig.password) {
                        $arguments += "--password=$($dbConfig.password)"
                    }
                    
                    Get-Content $selectedBackup.FullName | & mysql @arguments
                }
            }
        }
        
        # Verify restore
        $arguments = @(
            "--host=$($dbConfig.host)",
            "--port=$($dbConfig.port)",
            "--user=$($dbConfig.username)",
            "--execute=SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='$databaseName'"
        )
        
        if ($dbConfig.password) {
            $arguments += "--password=$($dbConfig.password)"
        }
        
        $tableCount = & mysql @arguments --skip-column-names
        
        Write-Log "Database restore completed successfully. Tables: $tableCount" "SUCCESS"
    }
    catch {
        Write-Log "Database restore failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Restore files
function Restore-Files {
    param($config)
    
    if (!$config.files.enabled) {
        Write-Log "File restore is disabled" "WARN"
        return
    }
    
    # Get available backups
    $backups = Get-AvailableBackups $config "files"
    $selectedBackup = Select-Backup $backups "files"
    
    if (!$selectedBackup) {
        Write-Log "No backup selected, skipping file restore" "WARN"
        return
    }
    
    Write-Log "Selected backup: $($selectedBackup.FullName)"
    
    try {
        # Extract backup
        $tempDir = Join-Path $env:TEMP "tunnel_restore_$(Get-Date -Format 'yyyyMMdd_HHmmss')"
        Write-Log "Extracting backup to: $tempDir"
        
        Expand-Archive -Path $selectedBackup.FullName -DestinationPath $tempDir -Force
        
        # Restore files based on configuration
        foreach ($target in $config.files.restore_targets) {
            $sourcePath = Join-Path $tempDir $target.name
            $targetPath = Join-Path $ProjectRoot $target.target_path
            
            if (Test-Path $sourcePath) {
                Write-Log "Restoring $($target.name): $sourcePath -> $targetPath"
                
                # Backup existing files
                if (Test-Path $targetPath) {
                    $backupPath = "${targetPath}_backup_$(Get-Date -Format 'yyyyMMdd_HHmmss')"
                    Move-Item $targetPath $backupPath -Force
                    Write-Log "Existing files backed up to: $backupPath"
                }
                
                # Copy restored files
                Copy-Item $sourcePath $targetPath -Recurse -Force
                Write-Log "$($target.name) restored successfully" "SUCCESS"
            }
            else {
                Write-Log "$($target.name) not found in backup" "WARN"
            }
        }
        
        # Clean up temp directory
        Remove-Item $tempDir -Recurse -Force
        Write-Log "File restore completed successfully" "SUCCESS"
    }
    catch {
        Write-Log "File restore failed: $($_.Exception.Message)" "ERROR"
        throw
    }
}

# Compress file using gzip
function Compress-File {
    param($SourcePath, $DestinationPath)
    
    $fileStream = [System.IO.File]::OpenRead($SourcePath)
    $compressedStream = [System.IO.File]::Create($DestinationPath)
    $gzipStream = New-Object System.IO.Compression.GzipStream($compressedStream, [System.IO.Compression.CompressionMode]::Compress)
    
    $fileStream.CopyTo($gzipStream)
    
    $gzipStream.Close()
    $compressedStream.Close()
    $fileStream.Close()
}

# Expand gzip file
function Expand-GzipFile {
    param($SourcePath, $DestinationPath)
    
    $compressedStream = [System.IO.File]::OpenRead($SourcePath)
    $gzipStream = New-Object System.IO.Compression.GzipStream($compressedStream, [System.IO.Compression.CompressionMode]::Decompress)
    $fileStream = [System.IO.File]::Create($DestinationPath)
    
    $gzipStream.CopyTo($fileStream)
    
    $fileStream.Close()
    $gzipStream.Close()
    $compressedStream.Close()
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

# Main function
function Main {
    try {
        Write-Log "========== Restore Task Started =========="
        Write-Log "Restore Type: $RestoreType"
        Write-Log "Project Path: $ProjectRoot"
        Write-Log "Log File: $LogFile"
        
        # Read configuration
        $config = Read-Config
        
        # Verify backup path exists
        $backupPath = $config.backup.base_path
        if (!(Test-Path $backupPath)) {
            throw "Backup path does not exist: $backupPath"
        }
        
        # Execute restore
        switch ($RestoreType.ToLower()) {
            "full" {
                Restore-Database $config
                Restore-Files $config
            }
            "database" {
                Restore-Database $config
            }
            "files" {
                Restore-Files $config
            }
            default {
                throw "Unsupported restore type: $RestoreType"
            }
        }
        
        Write-Log "========== Restore Task Completed ==========" "SUCCESS"
        return 0
    }
    catch {
        Write-Log "Restore task failed: $($_.Exception.Message)" "ERROR"
        return 1
    }
}

# Execute main function
exit (Main) 