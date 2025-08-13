# 🚀 Production Setup Guide

## 🔧 Environment Variables for Production

### **1. Generate Secure JWT Secret**

```bash
# Generate a secure 512-bit JWT secret
openssl rand -base64 64
```

### **2. Required Environment Variables**

Set these environment variables in your production environment:

```bash
# CRITICAL: Use a unique, secure JWT secret
export JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET="your_secure_512_bit_secret_here"

# JWT Token Configuration (shorter for production)
export JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS=3600     # 1 hour
export JHIPSTER_SECURITY_AUTHENTICATION_JWT_TOKEN_VALIDITY_IN_SECONDS_FOR_REMEMBER_ME=86400  # 24 hours

# Database Configuration
export SPRING_DATASOURCE_URL="jdbc:mysql://your-db-host:3306/magnus"
export SPRING_DATASOURCE_USERNAME="your_db_user"
export SPRING_DATASOURCE_PASSWORD="your_secure_db_password"

# CORS Configuration (CRITICAL: adjust for your production domain)
export JHIPSTER_CORS_ALLOWED_ORIGINS="https://your-frontend-domain.com"

# Enhanced Caching for Party Budget Bliss
export JHIPSTER_CACHE_EHCACHE_TIME_TO_LIVE_SECONDS=3600
export JHIPSTER_CACHE_EHCACHE_MAX_ENTRIES=2000

# Workflow Automation Configuration
export WORKFLOW_TASK_GENERATION_ENABLED=true
export WORKFLOW_NOTIFICATION_ENABLED=true
export WORKFLOW_ASYNC_PROCESSING=true

# Task Scheduling Rules
export TASK_SHOPPING_DAYS_BEFORE=2
export TASK_PREPARATION_DAYS_BEFORE=1
export TASK_DELIVERY_DAYS_BEFORE=0
export TASK_COOKING_HOURS_BEFORE=4

# Real-time Features Cache Settings
export CACHE_TEMPLATES_TTL=3600
export CACHE_SHOPPING_LISTS_TTL=900
export CACHE_TASK_LISTS_TTL=300
export CACHE_DASHBOARD_ANALYTICS_TTL=1800

# WebSocket Configuration
export WEBSOCKET_HEARTBEAT_INTERVAL=10000
export WEBSOCKET_TASK_SCHEDULER_POOL_SIZE=10

# Spring Profile
export SPRING_PROFILES_ACTIVE="prod"
```

## 🐳 Docker Production Setup

### **1. Create docker-compose.prod.yml**

```yaml
version: '3.8'
services:
  magnus-backend:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=${JWT_SECRET}
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/magnus
      - SPRING_DATASOURCE_USERNAME=${DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${DB_PASSWORD}
      - JHIPSTER_CORS_ALLOWED_ORIGINS=${FRONTEND_URL}
    depends_on:
      - mysql
    networks:
      - magnus-network

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD}
      - MYSQL_DATABASE=magnus
      - MYSQL_USER=${DB_USER}
      - MYSQL_PASSWORD=${DB_PASSWORD}
    volumes:
      - mysql_data:/var/lib/mysql
    networks:
      - magnus-network

volumes:
  mysql_data:

networks:
  magnus-network:
    driver: bridge
```

### **2. Create .env file for Docker**

```bash
# Copy this to .env file in your production environment
JWT_SECRET=your_secure_512_bit_secret_here
DB_USER=magnus_user
DB_PASSWORD=your_secure_db_password
MYSQL_ROOT_PASSWORD=your_mysql_root_password
FRONTEND_URL=https://your-frontend-domain.com
```

### **3. Root-level Compose (Backend + Frontend + MySQL)**

From repository root you can build and run the full stack with:

```
docker compose -f docker-compose.prod.yml up --build -d
```

## ☁️ Cloud Deployment

### **AWS Elastic Beanstalk**

```bash
# Set environment variables in EB console or use:
eb setenv JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=your_secret
eb setenv SPRING_DATASOURCE_URL=jdbc:mysql://your-rds-endpoint:3306/magnus
eb setenv SPRING_DATASOURCE_USERNAME=your_username
eb setenv SPRING_DATASOURCE_PASSWORD=your_password
```

### **Heroku**

```bash
# Set environment variables in Heroku
heroku config:set JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=your_secret
heroku config:set SPRING_DATASOURCE_URL=jdbc:mysql://your-db-url:3306/magnus
heroku config:set SPRING_DATASOURCE_USERNAME=your_username
heroku config:set SPRING_DATASOURCE_PASSWORD=your_password
```

### **Google Cloud Platform**

```bash
# Set environment variables in GCP
gcloud run services update magnus-backend \
  --set-env-vars JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=your_secret \
  --set-env-vars SPRING_DATASOURCE_URL=jdbc:mysql://your-db-url:3306/magnus
```

## 🔒 Security Checklist

- [ ] **JWT Secret**: Use a unique 512-bit secret generated with `openssl rand -base64 64`
- [ ] **Database**: Use strong credentials and enable SSL/TLS
- [ ] **CORS**: Restrict to specific production domains
- [ ] **HTTPS**: Always use HTTPS in production
- [ ] **Token Validity**: Use shorter token lifetimes (1-4 hours)
- [ ] **Environment Variables**: Never hardcode secrets in configuration files
- [ ] **Firewall**: Restrict database access to application servers only
- [ ] **Monitoring**: Set up logging and monitoring for security events

## 🚦 Health Checks

### **Application Health**
```bash
curl https://your-domain.com/management/health
```

### **Database Connection**
```bash
curl https://your-domain.com/management/health/db
```

### **JWT Configuration**
```bash
# Should return 401 Unauthorized (expected for unauthenticated request)
curl -I https://your-domain.com/api/budgets
```

## 📊 Monitoring and Logging

### **Production Logging Configuration**

Add to `application-prod.yml`:

```yaml
logging:
  level:
    ROOT: WARN
    com.magnus: INFO
    org.springframework.security: WARN
  pattern:
    console: "%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{ISO8601} [%thread] %-5level %logger{36} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

## 🔄 Backup and Recovery

### **Database Backup**
```bash
# Daily database backup
mysqldump -h your-db-host -u your-user -p magnus > magnus-backup-$(date +%Y%m%d).sql
```

### **Environment Variables Backup**
```bash
# Export current environment variables (remove sensitive data before storing)
printenv | grep JHIPSTER > environment-backup.txt
printenv | grep SPRING >> environment-backup.txt
```

## 🎯 Deployment Steps

1. **Prepare Environment**
   ```bash
   # Generate JWT secret
   JWT_SECRET=$(openssl rand -base64 64)
   echo "Generated JWT Secret: $JWT_SECRET"
   ```

2. **Set Environment Variables**
   ```bash
   export JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET="$JWT_SECRET"
   # ... set other variables
   ```

3. **Build Application**
   ```bash
   ./mvnw clean package -Pprod
   ```

4. **Deploy**
   ```bash
   java -jar target/magnus-*.jar
   ```

5. **Verify Deployment**
   ```bash
   curl -I https://your-domain.com/management/health
   ```

---

💡 **Remember**: Always test your production configuration in a staging environment first! 