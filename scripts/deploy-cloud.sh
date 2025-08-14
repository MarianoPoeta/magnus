#!/bin/bash

# Magnus Cloud Deployment Script
# This script deploys the Magnus application to cloud environments

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_ROOT="$(dirname "$SCRIPT_DIR")"
ENV_FILE="$PROJECT_ROOT/.env"
CLOUD_ENV_FILE="$PROJECT_ROOT/.env.cloud"

# Functions
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

check_prerequisites() {
    log_info "Checking prerequisites..."
    
    # Check if Docker is running
    if ! docker info > /dev/null 2>&1; then
        log_error "Docker is not running. Please start Docker and try again."
        exit 1
    fi
    
    # Check if docker-compose is available
    if ! command -v docker-compose &> /dev/null; then
        log_error "docker-compose is not installed. Please install it and try again."
        exit 1
    fi
    
    # Check if .env file exists
    if [ ! -f "$ENV_FILE" ]; then
        log_warning ".env file not found. Creating from template..."
        cp "$CLOUD_ENV_FILE" "$ENV_FILE"
        log_warning "Please edit .env file with your actual values before continuing."
        exit 1
    fi
    
    log_success "Prerequisites check passed"
}

generate_secrets() {
    log_info "Generating secrets..."
    
    # Generate JWT secret if not set
    if ! grep -q "JWT_BASE64_SECRET=" "$ENV_FILE" || grep -q "your_jwt_secret_here" "$ENV_FILE"; then
        JWT_SECRET=$(openssl rand -base64 64)
        sed -i "s/your_jwt_secret_here/$JWT_SECRET/" "$ENV_FILE"
        log_success "Generated JWT secret"
    fi
    
    # Generate MySQL password if not set
    if ! grep -q "MYSQL_ROOT_PASSWORD=" "$ENV_FILE" || grep -q "your_secure_password_here" "$ENV_FILE"; then
        MYSQL_PASSWORD=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-25)
        sed -i "s/your_secure_password_here/$MYSQL_PASSWORD/" "$ENV_FILE"
        log_success "Generated MySQL password"
    fi
    
    # Generate Grafana password if not set
    if ! grep -q "GRAFANA_ADMIN_PASSWORD=" "$ENV_FILE" || grep -q "your_grafana_password_here" "$ENV_FILE"; then
        GRAFANA_PASSWORD=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-25)
        sed -i "s/your_grafana_password_here/$GRAFANA_PASSWORD/" "$ENV_FILE"
        log_success "Generated Grafana password"
    fi
}

build_images() {
    log_info "Building Docker images..."
    
    cd "$PROJECT_ROOT"
    
    # Build backend image
    log_info "Building backend image..."
    docker build -t magnus-backend:latest ./magnus-backend
    
    # Build frontend image
    log_info "Building frontend image..."
    docker build -t magnus-frontend:latest ./magnus-frontend
    
    log_success "Images built successfully"
}

deploy_application() {
    log_info "Deploying application..."
    
    cd "$PROJECT_ROOT"
    
    # Stop existing containers
    log_info "Stopping existing containers..."
    docker-compose -f docker-compose.cloud.yml down --remove-orphans
    
    # Start services
    log_info "Starting services..."
    docker-compose -f docker-compose.cloud.yml up -d
    
    # Wait for services to be ready
    log_info "Waiting for services to be ready..."
    sleep 30
    
    # Check service health
    check_service_health
    
    log_success "Application deployed successfully"
}

check_service_health() {
    log_info "Checking service health..."
    
    # Check backend health
    if curl -f http://localhost:8080/management/health > /dev/null 2>&1; then
        log_success "Backend is healthy"
    else
        log_error "Backend health check failed"
        return 1
    fi
    
    # Check frontend health
    if curl -f http://localhost:8081/ > /dev/null 2>&1; then
        log_success "Frontend is healthy"
    else
        log_error "Frontend health check failed"
        return 1
    fi
    
    # Check database health
    if docker-compose -f docker-compose.cloud.yml exec -T db mysqladmin ping -h localhost > /dev/null 2>&1; then
        log_success "Database is healthy"
    else
        log_error "Database health check failed"
        return 1
    fi
}

deploy_monitoring() {
    log_info "Deploying monitoring stack..."
    
    cd "$PROJECT_ROOT"
    
    # Create monitoring directories
    mkdir -p monitoring/grafana/dashboards
    mkdir -p monitoring/grafana/datasources
    
    # Start monitoring services
    docker-compose -f docker-compose.cloud.yml up -d prometheus grafana
    
    log_success "Monitoring stack deployed"
}

show_deployment_info() {
    log_info "Deployment completed successfully!"
    echo
    echo "Service URLs:"
    echo "  Frontend: http://localhost:8081"
    echo "  Backend API: http://localhost:8080"
    echo "  Prometheus: http://localhost:9090"
    echo "  Grafana: http://localhost:3000 (admin/admin)"
    echo
    echo "Database:"
    echo "  MySQL: localhost:3306"
    echo "  Database: magnus"
    echo
    echo "To view logs:"
    echo "  docker-compose -f docker-compose.cloud.yml logs -f"
    echo
    echo "To stop services:"
    echo "  docker-compose -f docker-compose.cloud.yml down"
}

# Main execution
main() {
    log_info "Starting Magnus cloud deployment..."
    
    check_prerequisites
    generate_secrets
    build_images
    deploy_application
    deploy_monitoring
    show_deployment_info
    
    log_success "Deployment completed successfully!"
}

# Run main function
main "$@"