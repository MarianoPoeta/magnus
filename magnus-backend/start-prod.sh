#!/bin/bash

# Magnus Backend Production Startup Script
# This script builds and deploys the backend for production

set -e

echo "🚀 Magnus Backend Production Deployment..."

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

# Check environment variables
check_environment() {
    print_status "Checking production environment variables..."
    
    REQUIRED_VARS=(
        "SPRING_DATASOURCE_URL"
        "SPRING_DATASOURCE_USERNAME" 
        "SPRING_DATASOURCE_PASSWORD"
        "JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET"
    )
    
    MISSING_VARS=()
    
    for var in "${REQUIRED_VARS[@]}"; do
        if [ -z "${!var}" ]; then
            MISSING_VARS+=("$var")
        fi
    done
    
    if [ ${#MISSING_VARS[@]} -ne 0 ]; then
        print_error "Missing required environment variables:"
        for var in "${MISSING_VARS[@]}"; do
            echo "  - $var"
        done
        echo ""
        print_warning "Please set these variables in your environment or .env.prod file"
        print_warning "See environment.template for configuration examples"
        exit 1
    fi
    
    print_success "All required environment variables are set"
}

# Load environment file if it exists
load_environment() {
    if [ -f ".env.prod" ]; then
        print_status "Loading production environment from .env.prod"
        export $(grep -v '^#' .env.prod | xargs)
        print_success "Environment loaded"
    elif [ -f ".env.local" ]; then
        print_warning "Using .env.local file (consider creating .env.prod for production)"
        export $(grep -v '^#' .env.local | xargs)
    else
        print_warning "No .env file found. Using system environment variables only."
    fi
}

# Test database connection
test_database() {
    print_status "Testing database connection..."
    
    # Extract database details from JDBC URL
    DB_HOST=$(echo $SPRING_DATASOURCE_URL | sed -n 's/.*\/\/\([^:]*\).*/\1/p')
    DB_PORT=$(echo $SPRING_DATASOURCE_URL | sed -n 's/.*:\([0-9]*\)\/.*/\1/p')
    
    if [ -z "$DB_PORT" ]; then
        DB_PORT=3306  # Default MySQL port
    fi
    
    print_status "Checking database connectivity to $DB_HOST:$DB_PORT"
    
    # Test connection (requires nc or telnet)
    if command -v nc &> /dev/null; then
        if ! nc -z $DB_HOST $DB_PORT; then
            print_error "Cannot connect to database at $DB_HOST:$DB_PORT"
            print_warning "Please ensure the database server is running and accessible"
            exit 1
        fi
    else
        print_warning "Cannot verify database connectivity (nc not available)"
    fi
    
    print_success "Database connectivity verified"
}

# Build application
build_application() {
    print_status "Building application for production..."
    
    # Clean and build
    ./mvnw clean compile -Pprod -DskipTests
    
    print_success "Application compiled successfully"
    
    # Run tests if requested
    if [ "${SKIP_TESTS:-false}" != "true" ]; then
        print_status "Running tests..."
        ./mvnw test -Pprod
        print_success "All tests passed"
    else
        print_warning "Skipping tests (SKIP_TESTS=true)"
    fi
    
    # Package application
    print_status "Packaging application..."
    ./mvnw package -Pprod -DskipTests
    
    if [ -f "target/magnus-*.jar" ]; then
        print_success "Application packaged successfully"
        JAR_FILE=$(ls target/magnus-*.jar | head -1)
        print_status "Generated JAR: $JAR_FILE"
    else
        print_error "Failed to create JAR file"
        exit 1
    fi
}

# Set production profile
set_prod_profile() {
    export SPRING_PROFILES_ACTIVE=prod
    export JAVA_OPTS="${JAVA_OPTS:--Xmx2g -Xms1g -server -Djava.security.egd=file:/dev/./urandom}"
    print_status "Production profile activated"
    print_status "Java options: $JAVA_OPTS"
}

# Display production information
display_production_info() {
    print_success "Production build completed! 🎉"
    echo ""
    echo "📋 Production Configuration:"
    echo "   🌐 Profile:         $SPRING_PROFILES_ACTIVE"
    echo "   📦 JAR File:        $JAR_FILE"
    echo "   ☕ Java Options:    $JAVA_OPTS"
    echo "   🗄️  Database:       ${SPRING_DATASOURCE_URL}"
    echo ""
    echo "🚀 Starting production server..."
    echo "   📍 Application will be available at configured port"
    echo "   💚 Health check: /management/health"
    echo "   📊 Metrics: /management/metrics"
    echo ""
    print_warning "Press Ctrl+C to stop the server"
    echo ""
}

# Start production server
start_server() {
    java $JAVA_OPTS -jar $JAR_FILE
}

# Cleanup function
cleanup() {
    print_status "Shutting down production server..."
    exit 0
}

# Main execution
main() {
    print_status "Magnus Backend Production Deployment"
    echo "===================================="
    
    # Load environment
    load_environment
    
    # Perform checks
    check_java
    check_environment
    test_database
    
    # Build and configure
    build_application
    set_prod_profile
    
    # Display information
    display_production_info
    
    # Start server
    start_server
}

# Handle script interruption
trap cleanup SIGINT SIGTERM

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-tests)
            export SKIP_TESTS=true
            shift
            ;;
        --skip-build)
            export SKIP_BUILD=true
            shift
            ;;
        --help)
            echo "Magnus Backend Production Startup Script"
            echo ""
            echo "Usage: $0 [options]"
            echo ""
            echo "Options:"
            echo "  --skip-tests    Skip running tests during build"
            echo "  --skip-build    Skip build and use existing JAR"
            echo "  --help          Show this help message"
            echo ""
            echo "Environment Variables:"
            echo "  SPRING_DATASOURCE_URL      Database connection URL"
            echo "  SPRING_DATASOURCE_USERNAME Database username"
            echo "  SPRING_DATASOURCE_PASSWORD Database password"
            echo "  JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET JWT secret"
            echo ""
            echo "See environment.template for complete configuration options"
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            print_status "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Skip build if requested and JAR exists
if [ "${SKIP_BUILD:-false}" = "true" ]; then
    JAR_FILE=$(ls target/magnus-*.jar 2>/dev/null | head -1)
    if [ -n "$JAR_FILE" ]; then
        print_warning "Skipping build, using existing JAR: $JAR_FILE"
        load_environment
        check_java
        check_environment
        test_database
        set_prod_profile
        display_production_info
        start_server
    else
        print_error "No existing JAR file found. Cannot skip build."
        exit 1
    fi
else
    # Run full deployment
    main "$@"
fi 