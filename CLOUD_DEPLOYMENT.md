# Magnus Cloud Deployment Guide

This guide provides comprehensive instructions for deploying the Magnus application to various cloud environments.

## 🚀 Quick Start

### Prerequisites
- Docker and Docker Compose installed
- Kubernetes cluster (for K8s deployment)
- Domain name (for production)
- SSL certificates (for HTTPS)

### 1. Docker Compose Deployment (Recommended for testing)

```bash
# Clone the repository
git clone <your-repo-url>
cd magnus

# Copy environment template
cp .env.cloud .env

# Edit .env with your values
nano .env

# Deploy
chmod +x scripts/deploy-cloud.sh
./scripts/deploy-cloud.sh
```

### 2. Kubernetes Deployment

```bash
# Deploy to Kubernetes
chmod +x scripts/deploy-k8s.sh
./scripts/deploy-k8s.sh
```

## 📋 Deployment Options

### Option 1: Docker Compose (Local/Testing)
- **File**: `docker-compose.cloud.yml`
- **Best for**: Development, testing, single-server deployment
- **Features**: Full stack with monitoring, SSL termination, health checks

### Option 2: Kubernetes (Production)
- **Directory**: `kubernetes/`
- **Best for**: Production, scaling, multi-node clusters
- **Features**: Auto-scaling, load balancing, rolling updates

## 🔧 Configuration

### Environment Variables

Create a `.env` file based on `.env.cloud`:

```bash
# Database
MYSQL_ROOT_PASSWORD=your_secure_password
MYSQL_DATABASE=magnus
MYSQL_USER=root

# JWT Security
JWT_BASE64_SECRET=your_jwt_secret

# Application
SPRING_PROFILES_ACTIVE=prod,api-docs
PROMETHEUS_ENABLED=true

# CORS
CORS_ALLOWED_ORIGINS=https://your-domain.com
CORS_ALLOWED_METHODS=GET,POST,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=Authorization,Content-Type,X-Requested-With

# Frontend
VITE_API_URL=https://your-domain.com/api
VITE_ENVIRONMENT=production
```

### Generate Secrets

```bash
# Generate JWT secret
openssl rand -base64 64

# Generate MySQL password
openssl rand -base64 32 | tr -d "=+/" | cut -c1-25
```

## 🌐 Domain Configuration

### 1. Update CORS Settings
Edit `.env` file:
```bash
CORS_ALLOWED_ORIGINS=https://your-domain.com,https://www.your-domain.com
```

### 2. Update Frontend API URL
```bash
VITE_API_URL=https://your-domain.com/api
```

### 3. Update Kubernetes ConfigMap
Edit `kubernetes/configmap.yml`:
```yaml
cors-allowed-origins: "https://your-domain.com,https://www.your-domain.com"
vite-api-url: "https://your-domain.com/api"
```

## 🔒 SSL/TLS Configuration

### Option 1: Let's Encrypt (Recommended)
```bash
# Install cert-manager
kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.12.0/cert-manager.yaml

# Create ClusterIssuer
kubectl apply -f - <<EOF
apiVersion: cert-manager.io/v1
kind: ClusterIssuer
metadata:
  name: letsencrypt-prod
spec:
  acme:
    server: https://acme-v02.api.letsencrypt.org/directory
    email: your-email@domain.com
    privateKeySecretRef:
      name: letsencrypt-prod
    solvers:
    - http01:
        ingress:
          class: nginx
EOF
```

### Option 2: Self-Signed (Development)
```bash
# Generate self-signed certificate
openssl req -x509 -nodes -days 365 -newkey rsa:2048 \
  -keyout nginx/ssl/key.pem \
  -out nginx/ssl/cert.pem \
  -subj "/C=US/ST=State/L=City/O=Organization/CN=localhost"
```

## 📊 Monitoring Setup

### Prometheus Configuration
- **Port**: 9090
- **Targets**: Backend, Frontend, MySQL, Nginx
- **Metrics**: JVM, HTTP, Database, System

### Grafana Configuration
- **Port**: 3000
- **Default**: admin/admin
- **Dashboards**: Pre-configured for Magnus

### Access Monitoring
```bash
# Prometheus
http://localhost:9090

# Grafana
http://localhost:3000
```

## 🔄 Scaling Configuration

### Docker Compose
```yaml
# In docker-compose.cloud.yml
deploy:
  replicas: 3
  resources:
    limits:
      memory: 1G
      cpus: '1.0'
```

### Kubernetes
```yaml
# Auto-scaling
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
spec:
  minReplicas: 2
  maxReplicas: 10
  metrics:
  - type: Resource
    resource:
      name: cpu
      target:
        type: Utilization
        averageUtilization: 70
```

## 🚨 Health Checks

### Backend Health
```bash
curl http://localhost:8080/management/health
```

### Frontend Health
```bash
curl http://localhost:8081/
```

### Database Health
```bash
docker-compose exec db mysqladmin ping -h localhost
```

## 📝 Logs and Debugging

### View Logs
```bash
# Docker Compose
docker-compose -f docker-compose.cloud.yml logs -f

# Kubernetes
kubectl logs -f deployment/magnus-backend -n magnus
kubectl logs -f deployment/magnus-frontend -n magnus
```

### Debug Services
```bash
# Check service status
kubectl get pods -n magnus
kubectl get services -n magnus
kubectl get ingress -n magnus

# Port forwarding
kubectl port-forward service/magnus-backend 8080:8080 -n magnus
kubectl port-forward service/magnus-frontend 8081:80 -n magnus
```

## 🧹 Cleanup

### Docker Compose
```bash
docker-compose -f docker-compose.cloud.yml down -v
```

### Kubernetes
```bash
kubectl delete namespace magnus
```

## 🔧 Troubleshooting

### Common Issues

#### 1. Database Connection Failed
```bash
# Check MySQL status
docker-compose exec db mysqladmin ping -h localhost

# Check environment variables
docker-compose exec backend env | grep SPRING_DATASOURCE
```

#### 2. Frontend Can't Connect to Backend
```bash
# Check backend health
curl http://localhost:8080/management/health

# Check CORS configuration
curl -H "Origin: http://localhost:8081" \
     -H "Access-Control-Request-Method: POST" \
     -H "Access-Control-Request-Headers: X-Requested-With" \
     -X OPTIONS http://localhost:8080/api/authenticate
```

#### 3. SSL Certificate Issues
```bash
# Check certificate validity
openssl x509 -in nginx/ssl/cert.pem -text -noout

# Check nginx configuration
docker-compose exec nginx nginx -t
```

### Performance Tuning

#### JVM Settings
```bash
# In .env
JAVA_OPTS=-Xms1g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

#### Database Optimization
```yaml
# In kubernetes/mysql-deployment.yml
resources:
  requests:
    memory: "512Mi"
    cpu: "500m"
  limits:
    memory: "2Gi"
    cpu: "1000m"
```

## 📚 Additional Resources

- [Docker Compose Reference](https://docs.docker.com/compose/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Spring Boot Production Features](https://spring.io/guides/gs/spring-boot/)
- [Nginx Configuration](https://nginx.org/en/docs/)
- [Prometheus Monitoring](https://prometheus.io/docs/)

## 🆘 Support

For deployment issues:
1. Check the logs: `docker-compose logs` or `kubectl logs`
2. Verify configuration files
3. Check environment variables
4. Ensure all services are healthy
5. Review the troubleshooting section above