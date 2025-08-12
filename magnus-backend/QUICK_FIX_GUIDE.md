# 🚨 Quick Fix Guide: Database Schema Issue

## Problem
HTTP 500 error during authentication due to missing JHipster user tables.

## Root Cause
Liquibase database migration hasn't run to create the database schema.

## ✅ Solution Steps

### 1. Stop Any Running Backend
```powershell
Stop-Process -Name "java" -ErrorAction SilentlyContinue
```

### 2. Ensure MySQL is Running
```powershell
docker ps | findstr mysql
# Should show: magnus-mysql-1 container running
```

### 3. Recreate Database (Optional)
```powershell
docker exec magnus-mysql-1 mysql -u root -e "DROP DATABASE IF EXISTS magnus; CREATE DATABASE magnus;"
```

### 4. Start Backend (Choose Method A or B)

**Method A: Using Maven Wrapper**
```powershell
cd magnus-backend
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw.cmd spring-boot:run
```

**Method B: Using PowerShell Script**
```powershell
cd magnus-backend
.\start-dev.ps1
```

### 5. Wait for Liquibase Initialization
Look for these log messages:
```
Liquibase: Creating database history table with name: DATABASECHANGELOG
Liquibase: Successfully acquired change log lock
Liquibase: Creating initial schema...
Liquibase: ChangeSet executed successfully
```

### 6. Test Authentication
```powershell
$body = '{"username":"admin","password":"admin"}'
Invoke-RestMethod -Uri "http://localhost:8080/api/authenticate" -Method POST -Body $body -ContentType "application/json"
```

## Expected Result
- Returns JWT token: `{"id_token": "eyJhbGciOiJIUzI1NiJ9..."}`
- Frontend test panel should work properly

## If Still Having Issues
1. Check application logs for errors
2. Verify database tables exist:
   ```powershell
   docker exec magnus-mysql-1 mysql -u root -e "USE magnus; SHOW TABLES;"
   ```
3. Ensure port 8080 is not being used by other applications

## Default Users Created
- **Username:** admin | **Password:** admin (ADMIN role)
- **Username:** user | **Password:** user (USER role) 