# Magnus Backend Development Startup Script (PowerShell)
# This script sets up the development environment and starts the backend server

param(
    [switch]$SkipTests,
    [switch]$Help
)

# Colors for output
$Colors = @{
    Info = "Cyan"
    Success = "Green" 
    Warning = "Yellow"
    Error = "Red"
}

function Write-Status {
    param([string]$Message)
    Write-Host "[INFO] $Message" -ForegroundColor $Colors.Info
}

function Write-Success {
    param([string]$Message)
    Write-Host "[SUCCESS] $Message" -ForegroundColor $Colors.Success
}

function Write-Warning {
    param([string]$Message)
    Write-Host "[WARNING] $Message" -ForegroundColor $Colors.Warning
}

function Write-Error {
    param([string]$Message)
    Write-Host "[ERROR] $Message" -ForegroundColor $Colors.Error
}

function Show-Help {
    Write-Host "Magnus Backend Development Startup Script" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Usage: .\start-dev.ps1 [options]"
    Write-Host ""
    Write-Host "Options:"
    Write-Host "  -SkipTests    Skip running tests during startup"
    Write-Host "  -Help         Show this help message"
    Write-Host ""
    Write-Host "Prerequisites:"
    Write-Host "  - Java 17 or higher"
    Write-Host "  - Docker Desktop (for MySQL)"
    Write-Host "  - Maven (or use included mvnw)"
    Write-Host ""
    exit 0
}

function Test-JavaInstallation {
    Write-Status "Checking Java installation..."
    
    try {
        $javaVersion = java -version 2>&1 | Select-String "version" | Select-Object -First 1
        if ($javaVersion) {
            $version = $javaVersion -replace '.*"(\d+).*', '$1'
            if ([int]$version -ge 17) {
                Write-Success "Java $version detected"
                return $true
            } else {
                Write-Error "Java 17 or higher is required. Current version: $version"
                return $false
            }
        }
    } catch {
        Write-Error "Java is not installed or not in PATH"
        Write-Host "Please install Java 17 or higher and add it to your PATH"
        return $false
    }
    
    return $false
}

function Test-DockerInstallation {
    Write-Status "Checking Docker installation..."
    
    try {
        $dockerInfo = docker info 2>&1
        if ($LASTEXITCODE -eq 0) {
            Write-Success "Docker is running"
            return $true
        } else {
            Write-Error "Docker is not running. Please start Docker Desktop."
            return $false
        }
    } catch {
        Write-Error "Docker is not installed. Please install Docker Desktop."
        return $false
    }
}

function Start-Database {
    Write-Status "Starting MySQL database..."
    
    # Check if MySQL container is already running
    $runningContainers = docker ps --format "table {{.Names}}" | Select-String "magnus-mysql"
    
    if ($runningContainers) {
        Write-Warning "MySQL container is already running"
    } else {
        Write-Status "Starting MySQL container..."
        try {
            npm run docker:db:up
            
            # Wait for database to be ready
            Write-Status "Waiting for database to be ready..."
            $maxAttempts = 30
            $attempt = 0
            
            do {
                try {
                    docker exec magnus-mysql-1 mysql -e "SELECT 1" 2>$null | Out-Null
                    if ($LASTEXITCODE -eq 0) {
                        Write-Success "Database is ready"
                        break
                    }
                } catch {
                    # Continue waiting
                }
                
                $attempt++
                Write-Host "." -NoNewline
                Start-Sleep -Seconds 2
            } while ($attempt -lt $maxAttempts)
            
            Write-Host "" # New line after dots
            
            if ($attempt -eq $maxAttempts) {
                Write-Error "Database failed to start within expected time"
                exit 1
            }
        } catch {
            Write-Error "Failed to start MySQL container: $_"
            exit 1
        }
    }
}

function Install-Dependencies {
    Write-Status "Installing/updating Maven dependencies..."
    
    try {
        .\mvnw.cmd dependency:resolve -q
        Write-Success "Dependencies are up to date"
    } catch {
        Write-Error "Failed to resolve dependencies: $_"
        exit 1
    }
}

function Set-DevProfile {
    $env:SPRING_PROFILES_ACTIVE = "dev,api-docs"
    Write-Status "Development profile activated (dev,api-docs)"
}

function Show-URLs {
    Write-Success "Backend started successfully! 🎉"
    Write-Host ""
    Write-Host "📋 Available URLs:" -ForegroundColor Cyan
    Write-Host "   🌐 Application:     http://localhost:8080"
    Write-Host "   📚 Swagger UI:      http://localhost:8080/swagger-ui"
    Write-Host "   🔍 API Docs:        http://localhost:8080/v3/api-docs"
    Write-Host "   💚 Health Check:    http://localhost:8080/management/health"
    Write-Host "   📊 Metrics:         http://localhost:8080/management/metrics"
    Write-Host ""
    Write-Host "🔑 Default Login Credentials:" -ForegroundColor Cyan
    Write-Host "   👤 Admin:           admin/admin"
    Write-Host "   👤 User:            user/user"
    Write-Host ""
    Write-Host "📖 Documentation:" -ForegroundColor Cyan
    Write-Host "   📁 API Guide:       API_INTEGRATION_GUIDE.md"
    Write-Host "   🏗️  Architecture:    PROJECT_ARCHITECTURE.md"
    Write-Host ""
}

function Start-Application {
    Write-Status "Starting Spring Boot application..."
    Write-Warning "Press Ctrl+C to stop the server"
    Write-Host ""
    
    try {
        .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev,api-docs
    } catch {
        Write-Error "Failed to start application: $_"
        exit 1
    }
}

# Handle help parameter
if ($Help) {
    Show-Help
}

# Main execution
try {
    Write-Host "🚀 Starting Magnus Backend Development Environment..." -ForegroundColor Cyan
    Write-Host "================================================" -ForegroundColor Cyan
    Write-Host ""
    
    # Perform checks
    if (-not (Test-JavaInstallation)) { exit 1 }
    if (-not (Test-DockerInstallation)) { exit 1 }
    
    # Start services
    Start-Database
    Install-Dependencies
    Set-DevProfile
    
    # Display information
    Show-URLs
    
    # Start application
    Start-Application
    
} catch {
    Write-Error "An error occurred: $_"
    exit 1
} finally {
    Write-Status "Cleanup completed"
} 