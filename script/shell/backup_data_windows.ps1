# Windows Data Backup Script
# Author: TunnelManagement Team
# Version: 1.0
# Description: Automatically backup database and important files to specified directory

param(
    [string]$BackupType = "full",  # full, incremental, database, files
    [string]$ConfigFile = "backup_config.json",
    [switch]$Compress = $true,
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
$LogFile = Join-Path $LogDir "backup_$(Get-Date -Format 'yyyyMMdd_HHmmss').log"

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
            retention_days = 30
            compress = $true
        }
        database = @{
            enabled = $true
            type = "mysql"  # mysql, postgresql, sqlserver
            host = "localhost"
            port = 3306
            username = "backup_user"
            password = ""
            databases = @("tunnel_management")
            backup_path = "database"
        }
        files = @{
            enabled = $true
            backup_path = "files"
            sources = @(
                @{
                    name = "Application Config"
                    path = "yudao-server\src\main\resources"
                    exclude = @("*.log", "*.tmp")
                },
                @{
                    name = "Upload Files"
                    path = "upload"
                    exclude = @("*.tmp")
                },
                @{
                    name = "Important Scripts"
                    path = "script"
                    exclude = @("*.log", "node_modules")
                }
            )
        }
        notification = @{
            enabled = $false
            email = @{
                smtp_server = "smtp.example.com"
                smtp_port = 587
                username = "backup@example.com"
                password = ""
                to = @("admin@example.com")
            }
        }
    }
    
    $defaultConfig | ConvertTo-Json -Depth 10 | Set-Content $ConfigPath
    Write-Log "Default configuration file created: $ConfigPath"
}

# Database backup function
function Backup-Database {
    param($config)
    
    if (!$config.database.enabled) {
        Write-Log "Database backup is disabled" "WARN"
        return
    }
    
    $dbConfig = $config.database
    $backupDir = Join-Path $config.backup.base_path $dbConfig.backup_path
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    
    # Create backup directory
    if (!(Test-Path $backupDir)) {
        New-Item -ItemType Directory -Path $backupDir -Force | Out-Null
    }
    
    Write-Log "Starting database backup..."
    
    foreach ($database in $dbConfig.databases) {
        try {
            $backupFile = Join-Path $backupDir "${database}_${timestamp}.sql"
            
            switch ($dbConfig.type.ToLower()) {
                "mysql" {
                    $mysqldumpPath = Get-Command mysqldump -ErrorAction SilentlyContinue
                    if (!$mysqldumpPath) {
                        throw "mysqldump command not found, please ensure MySQL client is installed"
                    }
                    
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
                    
                    & mysqldump @arguments > $backupFile
                }
                "postgresql" {
                    $env:PGPASSWORD = $dbConfig.password
                    & pg_dump -h $dbConfig.host -p $dbConfig.port -U $dbConfig.username -d $database -f $backupFile
                }
                "sqlserver" {
                    $connectionString = "Server=$($dbConfig.host),$($dbConfig.port);Database=$database;User Id=$($dbConfig.username);Password=$($dbConfig.password);"
                    $query = "BACKUP DATABASE [$database] TO DISK = '$backupFile'"
                    Invoke-Sqlcmd -ConnectionString $connectionString -Query $query
                }
                default {
                    throw "Unsupported database type: $($dbConfig.type)"
                }
            }
            
            if (Test-Path $backupFile) {
                $fileSize = (Get-Item $backupFile).Length
                Write-Log "Database $database backup successful: $backupFile ($(Format-FileSize $fileSize))" "SUCCESS"
                
                # Compress backup file
                if ($config.backup.compress) {
                    Compress-BackupFile $backupFile
                }
            }
            else {
                throw "Backup file was not generated"
            }
        }
        catch {
            Write-Log "Database $database backup failed: $($_.Exception.Message)" "ERROR"
        }
    }
}

# File backup function
function Backup-Files {
    param($config)
    
    if (!$config.files.enabled) {
        Write-Log "File backup is disabled" "WARN"
        return
    }
    
    $filesConfig = $config.files
    $backupDir = Join-Path $config.backup.base_path $filesConfig.backup_path
    $timestamp = Get-Date -Format "yyyyMMdd_HHmmss"
    
    # Create backup directory
    if (!(Test-Path $backupDir)) {
        New-Item -ItemType Directory -Path $backupDir -Force | Out-Null
    }
    
    Write-Log "Starting file backup..."
    
    foreach ($source in $filesConfig.sources) {
        try {
            $sourcePath = Join-Path $ProjectRoot $source.path
            
            if (!(Test-Path $sourcePath)) {
                Write-Log "Source path does not exist: $sourcePath" "WARN"
                continue
            }
            
            $backupName = "$($source.name)_$timestamp"
            $targetDir = Join-Path $backupDir $backupName
            
            Write-Log "Backing up $($source.name): $sourcePath -> $targetDir"
            
            # Use robocopy for file copying
            $excludeArgs = @()
            if ($source.exclude) {
                $excludeArgs = $source.exclude | ForEach-Object { "/XF", $_ }
            }
            
            $robocopyArgs = @(
                $sourcePath,
                $targetDir,
                "/E",  # Copy subdirectories, including empty ones
                "/R:3",  # Number of retries
                "/W:10",  # Wait time between retries
                "/MT:8",  # Multi-threaded copying
                "/LOG+:$LogFile"  # Log recording
            ) + $excludeArgs
            
            $result = & robocopy @robocopyArgs
            $exitCode = $LASTEXITCODE
            
            # Handle robocopy exit codes
            if ($exitCode -le 7) {
                Write-Log "$($source.name) backup successful" "SUCCESS"
                
                # Compress backup directory
                if ($config.backup.compress) {
                    Compress-BackupDirectory $targetDir
                }
            }
            else {
                Write-Log "$($source.name) backup failed, exit code: $exitCode" "ERROR"
            }
        }
        catch {
            Write-Log "$($source.name) backup failed: $($_.Exception.Message)" "ERROR"
        }
    }
}

# Compress backup file
function Compress-BackupFile {
    param($FilePath)
    
    try {
        $compressedPath = "$FilePath.gz"
        Write-Log "Compressing file: $FilePath"
        
        # Use .NET compression
        $fileStream = [System.IO.File]::OpenRead($FilePath)
        $compressedStream = [System.IO.File]::Create($compressedPath)
        $gzipStream = New-Object System.IO.Compression.GzipStream($compressedStream, [System.IO.Compression.CompressionMode]::Compress)
        
        $fileStream.CopyTo($gzipStream)
        
        $gzipStream.Close()
        $compressedStream.Close()
        $fileStream.Close()
        
        # Remove original file
        Remove-Item $FilePath -Force
        
        $originalSize = (Get-Item $FilePath -ErrorAction SilentlyContinue).Length
        $compressedSize = (Get-Item $compressedPath).Length
        
        Write-Log "File compression completed: $(Format-FileSize $compressedSize) (compression ratio: $(Format-CompressionRatio $originalSize $compressedSize))" "SUCCESS"
    }
    catch {
        Write-Log "File compression failed: $($_.Exception.Message)" "ERROR"
    }
}

# Compress backup directory
function Compress-BackupDirectory {
    param($DirectoryPath)
    
    try {
        $compressedPath = "$DirectoryPath.zip"
        Write-Log "Compressing directory: $DirectoryPath"
        
        # Use Compress-Archive cmdlet
        Compress-Archive -Path "$DirectoryPath\*" -DestinationPath $compressedPath -CompressionLevel Optimal
        
        # Remove original directory
        Remove-Item $DirectoryPath -Recurse -Force
        
        $compressedSize = (Get-Item $compressedPath).Length
        Write-Log "Directory compression completed: $(Format-FileSize $compressedSize)" "SUCCESS"
    }
    catch {
        Write-Log "Directory compression failed: $($_.Exception.Message)" "ERROR"
    }
}

# Clean old backups
function Clean-OldBackups {
    param($config)
    
    $retentionDays = $config.backup.retention_days
    $cutoffDate = (Get-Date).AddDays(-$retentionDays)
    
    Write-Log "Cleaning backup files older than $retentionDays days..."
    
    $backupPath = $config.backup.base_path
    if (Test-Path $backupPath) {
        $oldFiles = Get-ChildItem -Path $backupPath -Recurse -File | Where-Object { $_.CreationTime -lt $cutoffDate }
        
        foreach ($file in $oldFiles) {
            try {
                Remove-Item $file.FullName -Force
                Write-Log "Deleted old backup file: $($file.FullName)"
            }
            catch {
                Write-Log "Failed to delete file: $($file.FullName) - $($_.Exception.Message)" "ERROR"
            }
        }
        
        # Delete empty directories
        $emptyDirs = Get-ChildItem -Path $backupPath -Recurse -Directory | Where-Object { (Get-ChildItem $_.FullName).Count -eq 0 }
        foreach ($dir in $emptyDirs) {
            try {
                Remove-Item $dir.FullName -Force
                Write-Log "Deleted empty directory: $($dir.FullName)"
            }
            catch {
                Write-Log "Failed to delete directory: $($dir.FullName) - $($_.Exception.Message)" "ERROR"
            }
        }
    }
}

# Send notification
function Send-Notification {
    param($config, $success, $message)
    
    if (!$config.notification.enabled) {
        return
    }
    
    try {
        $emailConfig = $config.notification.email
        $subject = if ($success) { "Backup Success Notification" } else { "Backup Failure Notification" }
        $body = @"
TunnelManagement Data Backup Report

Time: $(Get-Date -Format "yyyy-MM-dd HH:mm:ss")
Status: $(if ($success) { "Success" } else { "Failed" })
Type: $BackupType

Details:
$message

Log File: $LogFile
"@
        
        $smtpClient = New-Object System.Net.Mail.SmtpClient($emailConfig.smtp_server, $emailConfig.smtp_port)
        $smtpClient.EnableSsl = $true
        $smtpClient.Credentials = New-Object System.Net.NetworkCredential($emailConfig.username, $emailConfig.password)
        
        $mailMessage = New-Object System.Net.Mail.MailMessage
        $mailMessage.From = $emailConfig.username
        $mailMessage.Subject = $subject
        $mailMessage.Body = $body
        
        foreach ($to in $emailConfig.to) {
            $mailMessage.To.Add($to)
        }
        
        $smtpClient.Send($mailMessage)
        Write-Log "Notification email sent successfully" "SUCCESS"
    }
    catch {
        Write-Log "Failed to send notification: $($_.Exception.Message)" "ERROR"
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

# Format compression ratio
function Format-CompressionRatio {
    param($OriginalSize, $CompressedSize)
    
    if ($OriginalSize -eq 0) { return "0%" }
    
    $ratio = (1 - ($CompressedSize / $OriginalSize)) * 100
    return "{0:N1}%" -f $ratio
}

# Main function
function Main {
    try {
        Write-Log "========== Backup Task Started =========="
        Write-Log "Backup Type: $BackupType"
        Write-Log "Project Path: $ProjectRoot"
        Write-Log "Log File: $LogFile"
        
        # Read configuration
        $config = Read-Config
        
        # Create backup directory
        $backupPath = $config.backup.base_path
        if (!(Test-Path $backupPath)) {
            New-Item -ItemType Directory -Path $backupPath -Force | Out-Null
            Write-Log "Created backup directory: $backupPath"
        }
        
        # Execute backup
        switch ($BackupType.ToLower()) {
            "full" {
                Backup-Database $config
                Backup-Files $config
            }
            "database" {
                Backup-Database $config
            }
            "files" {
                Backup-Files $config
            }
            "incremental" {
                Write-Log "Incremental backup feature not yet implemented" "WARN"
            }
            default {
                throw "Unsupported backup type: $BackupType"
            }
        }
        
        # Clean old backups
        Clean-OldBackups $config
        
        Write-Log "========== Backup Task Completed ==========" "SUCCESS"
        
        # Send success notification
        Send-Notification $config $true "Backup task completed successfully"
        
        return 0
    }
    catch {
        Write-Log "Backup task failed: $($_.Exception.Message)" "ERROR"
        
        # Send failure notification
        if ($config) {
            Send-Notification $config $false $_.Exception.Message
        }
        
        return 1
    }
}

# Execute main function
exit (Main) 