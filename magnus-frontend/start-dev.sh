#!/bin/bash

# Magnus Frontend Development Startup Script
# This script sets up the development environment and starts the frontend server

set -e

echo "🚀 Starting Magnus Frontend Development Environment..."

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

# Check if Node.js is installed
check_node() {
    if ! command -v node &> /dev/null; then
        print_error "Node.js is not installed. Please install Node.js 18 or higher."
        exit 1
    fi
    
    NODE_VERSION=$(node -v | grep -oP '\d+' | head -1)
    if [ "$NODE_VERSION" -lt 18 ]; then
        print_error "Node.js 18 or higher is required. Current version: $(node -v)"
        exit 1
    fi
    
    print_success "Node.js $(node -v) detected"
}

# Check if npm is installed
check_npm() {
    if ! command -v npm &> /dev/null; then
        print_error "npm is not installed. Please install npm."
        exit 1
    fi
    
    print_success "npm $(npm -v) detected"
}

# Setup environment file
setup_environment() {
    if [ ! -f ".env" ]; then
        print_status "Creating .env file from template..."
        
        # Create basic .env file
        cat > .env << EOF
# Magnus Frontend Development Environment
VITE_API_URL=http://localhost:8080/api
VITE_WS_URL=ws://localhost:8080/websocket
VITE_ENVIRONMENT=development
VITE_APP_NAME=Magnus
VITE_ENABLE_DEBUG_PANEL=true
EOF
        print_success "Environment file created"
    else
        print_success "Environment file already exists"
    fi
}

# Install dependencies
install_dependencies() {
    print_status "Installing/updating npm dependencies..."
    
    # Check if node_modules exists and package-lock.json is newer
    if [ -f "package-lock.json" ] && [ -d "node_modules" ]; then
        if [ "package-lock.json" -nt "node_modules" ]; then
            print_status "Package-lock.json is newer, running npm ci..."
            npm ci
        else
            print_status "Dependencies are up to date"
        fi
    else
        print_status "Installing dependencies..."
        npm install
    fi
    
    print_success "Dependencies are ready"
}

# Check backend connectivity
check_backend() {
    print_status "Checking backend connectivity..."
    
    BACKEND_URL="http://localhost:8080"
    MAX_ATTEMPTS=3
    ATTEMPT=0
    
    while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
        if curl -s -f "$BACKEND_URL/management/health" &> /dev/null; then
            print_success "Backend is running and accessible"
            return 0
        fi
        
        ATTEMPT=$((ATTEMPT + 1))
        if [ $ATTEMPT -lt $MAX_ATTEMPTS ]; then
            print_warning "Backend not accessible, retrying in 2 seconds... (attempt $ATTEMPT/$MAX_ATTEMPTS)"
            sleep 2
        fi
    done
    
    print_warning "Backend is not accessible at $BACKEND_URL"
    print_warning "Make sure the backend is running before starting the frontend"
    print_warning "You can start the backend with: ./start-dev.sh (in magnus-backend directory)"
    
    read -p "Continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
}

# Display development information
display_dev_info() {
    print_success "Frontend development environment ready! 🎉"
    echo ""
    echo "📋 Development URLs:"
    echo "   🌐 Frontend:        http://localhost:3000"
    echo "   🔧 Backend API:     http://localhost:8080/api"
    echo "   📚 Backend Docs:    http://localhost:8080/swagger-ui"
    echo "   💚 Health Check:    http://localhost:8080/management/health"
    echo ""
    echo "🔑 Default Login Credentials:"
    echo "   👤 Admin:           admin/admin"
    echo "   👤 User:            user/user"
    echo ""
    echo "📖 Available Commands:"
    echo "   🏗️  Build:           npm run build"
    echo "   🧪 Test:            npm run test"
    echo "   🔍 Lint:            npm run lint"
    echo "   📦 Preview:         npm run preview"
    echo ""
    echo "📁 Important Files:"
    echo "   🔧 Environment:     .env"
    echo "   ⚙️  Vite Config:     vite.config.ts"
    echo "   🎨 Tailwind:        tailwind.config.ts"
    echo ""
}

# Start development server
start_dev_server() {
    print_status "Starting Vite development server..."
    print_warning "Press Ctrl+C to stop the server"
    echo ""
    
    # Start the development server
    npm run dev
}

# Cleanup function
cleanup() {
    print_status "Shutting down development server..."
    exit 0
}

# Main execution
main() {
    print_status "Magnus Frontend Development Setup"
    echo "================================="
    
    # Perform checks
    check_node
    check_npm
    
    # Setup environment
    setup_environment
    install_dependencies
    check_backend
    
    # Display information
    display_dev_info
    
    # Start server
    start_dev_server
}

# Handle script interruption
trap cleanup SIGINT SIGTERM

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        --skip-backend-check)
            SKIP_BACKEND_CHECK=true
            shift
            ;;
        --install-only)
            INSTALL_ONLY=true
            shift
            ;;
        --help)
            echo "Magnus Frontend Development Startup Script"
            echo ""
            echo "Usage: $0 [options]"
            echo ""
            echo "Options:"
            echo "  --skip-backend-check  Skip checking if backend is running"
            echo "  --install-only        Only install dependencies and exit"
            echo "  --help                Show this help message"
            echo ""
            exit 0
            ;;
        *)
            print_error "Unknown option: $1"
            print_status "Use --help for usage information"
            exit 1
            ;;
    esac
done

# Handle special modes
if [ "${INSTALL_ONLY:-false}" = "true" ]; then
    print_status "Install-only mode"
    check_node
    check_npm
    setup_environment
    install_dependencies
    print_success "Dependencies installed successfully!"
    exit 0
fi

if [ "${SKIP_BACKEND_CHECK:-false}" = "true" ]; then
    check_backend() {
        print_warning "Skipping backend connectivity check"
    }
fi

# Run main function
main "$@" 