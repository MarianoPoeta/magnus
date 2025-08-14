#!/bin/bash

# Magnus Kubernetes Deployment Script
# This script deploys the Magnus application to Kubernetes clusters

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
K8S_DIR="$PROJECT_ROOT/kubernetes"
NAMESPACE="magnus"

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
    log_info "Checking Kubernetes prerequisites..."
    
    # Check if kubectl is available
    if ! command -v kubectl &> /dev/null; then
        log_error "kubectl is not installed. Please install it and try again."
        exit 1
    fi
    
    # Check if kubectl can connect to cluster
    if ! kubectl cluster-info &> /dev/null; then
        log_error "Cannot connect to Kubernetes cluster. Please check your kubeconfig."
        exit 1
    fi
    
    # Check if helm is available (optional)
    if ! command -v helm &> /dev/null; then
        log_warning "helm is not installed. Some features may not work."
    fi
    
    log_success "Prerequisites check passed"
}

create_namespace() {
    log_info "Creating namespace: $NAMESPACE"
    
    if kubectl get namespace "$NAMESPACE" &> /dev/null; then
        log_info "Namespace $NAMESPACE already exists"
    else
        kubectl apply -f "$K8S_DIR/namespace.yml"
        log_success "Namespace $NAMESPACE created"
    fi
}

generate_secrets() {
    log_info "Generating Kubernetes secrets..."
    
    # Generate JWT secret
    JWT_SECRET=$(openssl rand -base64 64)
    
    # Generate MySQL password
    MYSQL_PASSWORD=$(openssl rand -base64 32 | tr -d "=+/" | cut -c1-25)
    
    # Update secrets file
    sed -i "s/<BASE64_ENCODED_PASSWORD>/$(echo -n "$MYSQL_PASSWORD" | base64)/" "$K8S_DIR/secrets.yml"
    sed -i "s/<BASE64_ENCODED_JWT_SECRET>/$(echo -n "$JWT_SECRET" | base64)/" "$K8S_DIR/secrets.yml"
    
    log_success "Secrets generated and updated"
}

deploy_secrets() {
    log_info "Deploying secrets..."
    kubectl apply -f "$K8S_DIR/secrets.yml"
    log_success "Secrets deployed"
}

deploy_configmaps() {
    log_info "Deploying configmaps..."
    kubectl apply -f "$K8S_DIR/configmap.yml"
    log_success "Configmaps deployed"
}

deploy_database() {
    log_info "Deploying database..."
    kubectl apply -f "$K8S_DIR/mysql-deployment.yml"
    
    # Wait for database to be ready
    log_info "Waiting for database to be ready..."
    kubectl wait --for=condition=ready pod -l app=magnus-mysql -n "$NAMESPACE" --timeout=300s
    
    log_success "Database deployed and ready"
}

deploy_backend() {
    log_info "Deploying backend..."
    kubectl apply -f "$K8S_DIR/backend-deployment.yml"
    
    # Wait for backend to be ready
    log_info "Waiting for backend to be ready..."
    kubectl wait --for=condition=ready pod -l app=magnus-backend -n "$NAMESPACE" --timeout=300s
    
    log_success "Backend deployed and ready"
}

deploy_frontend() {
    log_info "Deploying frontend..."
    kubectl apply -f "$K8S_DIR/frontend-deployment.yml"
    
    # Wait for frontend to be ready
    log_info "Waiting for frontend to be ready..."
    kubectl wait --for=condition=ready pod -l app=magnus-frontend -n "$NAMESPACE" --timeout=300s
    
    log_success "Frontend deployed and ready"
}

deploy_ingress() {
    log_info "Deploying ingress..."
    kubectl apply -f "$K8S_DIR/ingress.yml"
    log_success "Ingress deployed"
}

check_deployment_status() {
    log_info "Checking deployment status..."
    
    echo
    echo "Pod Status:"
    kubectl get pods -n "$NAMESPACE"
    
    echo
    echo "Service Status:"
    kubectl get services -n "$NAMESPACE"
    
    echo
    echo "Ingress Status:"
    kubectl get ingress -n "$NAMESPACE"
    
    echo
    echo "Deployment Status:"
    kubectl get deployments -n "$NAMESPACE"
}

show_access_info() {
    log_info "Deployment completed successfully!"
    echo
    echo "Access Information:"
    echo "  Namespace: $NAMESPACE"
    echo
    echo "To view logs:"
    echo "  kubectl logs -f deployment/magnus-backend -n $NAMESPACE"
    echo "  kubectl logs -f deployment/magnus-frontend -n $NAMESPACE"
    echo
    echo "To access services:"
    echo "  kubectl port-forward service/magnus-backend 8080:8080 -n $NAMESPACE"
    echo "  kubectl port-forward service/magnus-frontend 8081:80 -n $NAMESPACE"
    echo
    echo "To delete deployment:"
    echo "  kubectl delete namespace $NAMESPACE"
}

# Main execution
main() {
    log_info "Starting Magnus Kubernetes deployment..."
    
    check_prerequisites
    create_namespace
    generate_secrets
    deploy_secrets
    deploy_configmaps
    deploy_database
    deploy_backend
    deploy_frontend
    deploy_ingress
    check_deployment_status
    show_access_info
    
    log_success "Kubernetes deployment completed successfully!"
}

# Run main function
main "$@"