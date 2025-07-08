# Test Database Connection Script
# Author: TunnelManagement Team
# Version: 1.0

param(
    [string]$ConfigFile = "backup_config.json"
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

# Test MySQL connection
function Test-MySQLConnection {
    param($dbConfig)
    
    Write-Host "Testing MySQL connection..." -ForegroundColor Yellow
    
    try {
        # Check if mysql command is available
        $mysqlPath = Get-Command mysql -ErrorAction SilentlyContinue
        if (!$mysqlPath) {
            Write-Host "❌ MySQL client not found" -ForegroundColor Red
            Write-Host "Please install MySQL client and ensure it's in PATH" -ForegroundColor Red
            return $false
        }
        
        Write-Host "✅ MySQL client found: $($mysqlPath.Source)" -ForegroundColor Green
        
        # Test connection
        $arguments = @(
            "--host=$($dbConfig.host)",
            "--port=$($dbConfig.port)",
            "--user=$($dbConfig.username)",
            "--connect-timeout=10",
            "--execute=SELECT VERSION(), NOW() as current_time"
        )
        
        if ($dbConfig.password) {
            $arguments += "--password=$($dbConfig.password)"
        }
        
        Write-Host "Connecting to: $($dbConfig.host):$($dbConfig.port)" -ForegroundColor Cyan
        
        $result = & mysql @arguments 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Connection successful!" -ForegroundColor Green
            Write-Host "Server info:" -ForegroundColor Cyan
            Write-Host $result
            
            # Test database access
            foreach ($database in $dbConfig.databases) {
                Write-Host "Testing database: $database" -ForegroundColor Yellow
                
                $dbArgs = $arguments + @("--database=$database", "--execute=SELECT COUNT(*) as table_count FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='$database'")
                $dbResult = & mysql @dbArgs 2>&1
                
                if ($LASTEXITCODE -eq 0) {
                    Write-Host "✅ Database '$database' accessible" -ForegroundColor Green
                    Write-Host "Tables info:" -ForegroundColor Cyan
                    Write-Host $dbResult
                }
                else {
                    Write-Host "❌ Database '$database' not accessible: $dbResult" -ForegroundColor Red
                }
            }
            
            return $true
        }
        else {
            Write-Host "❌ Connection failed: $result" -ForegroundColor Red
            return $false
        }
    }
    catch {
        Write-Host "❌ Connection test failed: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Test PostgreSQL connection
function Test-PostgreSQLConnection {
    param($dbConfig)
    
    Write-Host "Testing PostgreSQL connection..." -ForegroundColor Yellow
    
    try {
        # Check if psql command is available
        $psqlPath = Get-Command psql -ErrorAction SilentlyContinue
        if (!$psqlPath) {
            Write-Host "❌ PostgreSQL client not found" -ForegroundColor Red
            Write-Host "Please install PostgreSQL client and ensure it's in PATH" -ForegroundColor Red
            return $false
        }
        
        Write-Host "✅ PostgreSQL client found: $($psqlPath.Source)" -ForegroundColor Green
        
        # Set password environment variable
        $env:PGPASSWORD = $dbConfig.password
        
        Write-Host "Connecting to: $($dbConfig.host):$($dbConfig.port)" -ForegroundColor Cyan
        
        $result = & psql -h $dbConfig.host -p $dbConfig.port -U $dbConfig.username -c "SELECT version(), now() as current_time" 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Connection successful!" -ForegroundColor Green
            Write-Host "Server info:" -ForegroundColor Cyan
            Write-Host $result
            return $true
        }
        else {
            Write-Host "❌ Connection failed: $result" -ForegroundColor Red
            return $false
        }
    }
    catch {
        Write-Host "❌ Connection test failed: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
    finally {
        # Clean up environment variable
        Remove-Item Env:PGPASSWORD -ErrorAction SilentlyContinue
    }
}

# Test SQL Server connection
function Test-SQLServerConnection {
    param($dbConfig)
    
    Write-Host "Testing SQL Server connection..." -ForegroundColor Yellow
    
    try {
        # Check if sqlcmd is available
        $sqlcmdPath = Get-Command sqlcmd -ErrorAction SilentlyContinue
        if (!$sqlcmdPath) {
            Write-Host "❌ SQL Server client not found" -ForegroundColor Red
            Write-Host "Please install SQL Server client tools" -ForegroundColor Red
            return $false
        }
        
        Write-Host "✅ SQL Server client found: $($sqlcmdPath.Source)" -ForegroundColor Green
        
        Write-Host "Connecting to: $($dbConfig.host):$($dbConfig.port)" -ForegroundColor Cyan
        
        $connectionString = "Server=$($dbConfig.host),$($dbConfig.port);Database=master;User Id=$($dbConfig.username);Password=$($dbConfig.password);Connection Timeout=10;"
        
        $result = & sqlcmd -S "$($dbConfig.host),$($dbConfig.port)" -U $dbConfig.username -P $dbConfig.password -Q "SELECT @@VERSION as version, GETDATE() as current_time" 2>&1
        
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Connection successful!" -ForegroundColor Green
            Write-Host "Server info:" -ForegroundColor Cyan
            Write-Host $result
            return $true
        }
        else {
            Write-Host "❌ Connection failed: $result" -ForegroundColor Red
            return $false
        }
    }
    catch {
        Write-Host "❌ Connection test failed: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Test network connectivity
function Test-NetworkConnectivity {
    param($host, $port)
    
    Write-Host "Testing network connectivity to $host`:$port..." -ForegroundColor Yellow
    
    try {
        $tcpClient = New-Object System.Net.Sockets.TcpClient
        $tcpClient.ReceiveTimeout = 5000
        $tcpClient.SendTimeout = 5000
        
        $tcpClient.Connect($host, $port)
        
        if ($tcpClient.Connected) {
            Write-Host "✅ Network connectivity successful" -ForegroundColor Green
            $tcpClient.Close()
            return $true
        }
        else {
            Write-Host "❌ Network connectivity failed" -ForegroundColor Red
            return $false
        }
    }
    catch {
        Write-Host "❌ Network connectivity failed: $($_.Exception.Message)" -ForegroundColor Red
        return $false
    }
}

# Show connection troubleshooting tips
function Show-TroubleshootingTips {
    param($dbType)
    
    Write-Host "`n🔧 Troubleshooting Tips:" -ForegroundColor Cyan
    Write-Host "=" * 50
    
    switch ($dbType.ToLower()) {
        "mysql" {
            Write-Host "MySQL Connection Issues:"
            Write-Host "1. Ensure MySQL server is running"
            Write-Host "2. Check firewall settings (port 3306)"
            Write-Host "3. Verify user credentials and permissions"
            Write-Host "4. Install MySQL client: https://dev.mysql.com/downloads/mysql/"
            Write-Host "5. Test with: mysql -h host -P port -u user -p"
        }
        "postgresql" {
            Write-Host "PostgreSQL Connection Issues:"
            Write-Host "1. Ensure PostgreSQL server is running"
            Write-Host "2. Check firewall settings (port 5432)"
            Write-Host "3. Verify pg_hba.conf configuration"
            Write-Host "4. Install PostgreSQL client: https://www.postgresql.org/download/"
            Write-Host "5. Test with: psql -h host -p port -U user -d database"
        }
        "sqlserver" {
            Write-Host "SQL Server Connection Issues:"
            Write-Host "1. Ensure SQL Server is running"
            Write-Host "2. Check firewall settings (port 1433)"
            Write-Host "3. Enable SQL Server authentication"
            Write-Host "4. Install SQL Server client tools"
            Write-Host "5. Test with: sqlcmd -S server -U user -P password"
        }
    }
    
    Write-Host "`nGeneral Tips:"
    Write-Host "- Check network connectivity with: telnet host port"
    Write-Host "- Verify DNS resolution"
    Write-Host "- Check antivirus/firewall blocking"
    Write-Host "- Ensure database service is running"
    Write-Host "- Verify connection string parameters"
}

# Main function
function Main {
    Write-Host "TunnelManagement Database Connection Test" -ForegroundColor Green
    Write-Host "=" * 50
    
    $config = Read-Config
    if (!$config) {
        return 1
    }
    
    if (!$config.database.enabled) {
        Write-Host "Database backup is disabled in configuration" -ForegroundColor Yellow
        return 0
    }
    
    $dbConfig = $config.database
    
    Write-Host "Database Configuration:" -ForegroundColor Cyan
    Write-Host "Type: $($dbConfig.type)"
    Write-Host "Host: $($dbConfig.host)"
    Write-Host "Port: $($dbConfig.port)"
    Write-Host "Username: $($dbConfig.username)"
    Write-Host "Databases: $($dbConfig.databases -join ', ')"
    Write-Host ""
    
    # Test network connectivity first
    $networkOk = Test-NetworkConnectivity $dbConfig.host $dbConfig.port
    
    if (!$networkOk) {
        Write-Host "❌ Network connectivity failed. Please check network settings." -ForegroundColor Red
        Show-TroubleshootingTips $dbConfig.type
        return 1
    }
    
    # Test database connection
    $connectionOk = $false
    
    switch ($dbConfig.type.ToLower()) {
        "mysql" {
            $connectionOk = Test-MySQLConnection $dbConfig
        }
        "postgresql" {
            $connectionOk = Test-PostgreSQLConnection $dbConfig
        }
        "sqlserver" {
            $connectionOk = Test-SQLServerConnection $dbConfig
        }
        default {
            Write-Host "❌ Unsupported database type: $($dbConfig.type)" -ForegroundColor Red
            return 1
        }
    }
    
    if (!$connectionOk) {
        Show-TroubleshootingTips $dbConfig.type
        return 1
    }
    
    Write-Host "`n🎉 All tests passed! Database connection is working properly." -ForegroundColor Green
    return 0
}

# Execute main function
exit (Main) 