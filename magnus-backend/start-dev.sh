#!/bin/bash

# Magnus Backend Development Startup Script
# This script sets up the development environment and starts the backend server

set -e

echo "🚀 Starting Magnus Backend Development Environment..."

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if Java is installed
check_java() {
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed. Please install Java 17 or higher."
        exit 1
    fi
    
    JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "?(1\.)?\K\d+' | head -1)
    if [ "$JAVA_VERSION" -lt 17 ]; then
        print_error "Java 17 or higher is required. Current version: $JAVA_VERSION"
        exit 1
    fi
    
    print_success "Java $JAVA_VERSION detected"
}

# Check if Docker is running
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker is not installed. Please install Docker to run MySQL."
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        print_error "Docker is not running. Please start Docker."
        exit 1
    fi
    
    print_success "Docker is running"
}

# Start MySQL database
start_database() {
    print_status "Starting MySQL database..."
    
    # Check if MySQL container is already running
    if docker ps | grep -q "magnus-mysql"; then
        print_warning "MySQL container is already running"
    else
        print_status "Starting MySQL container..."
        npm run docker:db:up
        
        # Wait for database to be ready
        print_status "Waiting for database to be ready..."
        max_attempts=30
        attempt=0
        
        while [ $attempt -lt $max_attempts ]; do
            if docker exec magnus-mysql-1 mysql -e "SELECT 1" &> /dev/null; then
                print_success "Database is ready"
                break
            fi
            
            attempt=$((attempt + 1))
            echo -n "."
            sleep 2
        done
        
        if [ $attempt -eq $max_attempts ]; then
            print_error "Database failed to start within expected time"
            exit 1
        fi
    fi
}

# Install dependencies
install_dependencies() {
    print_status "Installing/updating Maven dependencies..."
    ./mvnw dependency:resolve -q
    print_success "Dependencies are up to date"
}

# Set development profile
set_dev_profile() {
    export SPRING_PROFILES_ACTIVE=dev,api-docs
    print_status "Development profile activated (dev,api-docs)"
}

# Display useful URLs
display_urls() {
    print_success "Backend started successfully! 🎉"
    echo ""
    echo "📋 Available URLs:"
    echo "   🌐 Application:     http://localhost:8080"
    echo "   📚 Swagger UI:      http://localhost:8080/swagger-ui"
    echo "   🔍 API Docs:        http://localhost:8080/v3/api-docs"
    echo "   💚 Health Check:    http://localhost:8080/management/health"
    echo "   📊 Metrics:         http://localhost:8080/management/metrics"
    echo ""
    echo "🔑 Default Login Credentials:"
    echo "   👤 Admin:           admin/admin"
    echo "   👤 User:            user/user"
    echo ""
    echo "📖 Documentation:"
    echo "   📁 API Guide:       API_INTEGRATION_GUIDE.md"
    echo "   🏗️  Architecture:    PROJECT_ARCHITECTURE.md"
    echo ""
}

# Main execution
main() {
    print_status "Magnus Backend Development Setup"
    echo "================================="
    
    # Perform checks
    check_java
    check_docker
    
    # Start services
    start_database
    install_dependencies
    set_dev_profile
    
    # Display information
    display_urls
    
    print_status "Starting Spring Boot application..."
    print_warning "Press Ctrl+C to stop the server"
    echo ""
    
    # Start the application
    ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev,api-docs
}

# Handle script interruption
trap 'echo -e "\n${YELLOW}[INFO]${NC} Shutting down..."; exit 0' SIGINT SIGTERM

# Run main function
main "$@" 