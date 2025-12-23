#!/bin/bash

###############################################################################
# Run Pet Clinic Application with MySQL Database
# This script sets up and runs the application with MySQL
###############################################################################

set -e

echo "========================================="
echo "Pet Clinic - MySQL Setup & Run"
echo "========================================="
echo ""

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m'

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}➜ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    print_error "Docker is not running. Please start Docker first."
    exit 1
fi

print_success "Docker is running"
echo ""

# Stop and remove any existing containers
print_info "Cleaning up existing containers..."
docker-compose -f docker-compose-mysql.yml down 2>/dev/null || true
print_success "Cleanup complete"
echo ""

# Build the application
print_info "Building application with Maven..."
mvn clean package -DskipTests -q
print_success "Application built successfully"
echo ""

# Start MySQL and Application
print_info "Starting MySQL database..."
docker-compose -f docker-compose-mysql.yml up -d mysql

print_info "Waiting for MySQL to be ready..."
sleep 10

# Check MySQL health
for i in {1..30}; do
    if docker exec petclinic-mysql mysqladmin ping -h localhost -u root -prootpassword --silent 2>/dev/null; then
        print_success "MySQL is ready!"
        break
    fi
    echo -n "."
    sleep 2
    if [ $i -eq 30 ]; then
        print_error "MySQL failed to start after 60 seconds"
        docker-compose -f docker-compose-mysql.yml logs mysql
        exit 1
    fi
done
echo ""

# Start the application
print_info "Starting Pet Clinic application..."
docker-compose -f docker-compose-mysql.yml up -d pet-clinic

print_info "Waiting for application to start..."
sleep 10

# Check application health
for i in {1..30}; do
    if curl -s http://localhost:8081/pet-clinic/actuator/health > /dev/null 2>&1; then
        print_success "Application is ready!"
        break
    fi
    echo -n "."
    sleep 2
    if [ $i -eq 30 ]; then
        print_error "Application failed to start after 60 seconds"
        docker-compose -f docker-compose-mysql.yml logs pet-clinic
        exit 1
    fi
done
echo ""

# Display status
echo ""
echo "========================================="
echo "✅ Pet Clinic with MySQL is Running!"
echo "========================================="
echo ""
echo "Application URL:"
echo "  🌐 http://localhost:8081/pet-clinic/"
echo ""
echo "Database Info:"
echo "  📊 Database: petclinicdb"
echo "  👤 User: petclinic"
echo "  🔑 Password: petclinic123"
echo "  🔌 Port: 3306"
echo ""
echo "Useful Commands:"
echo "  📋 View logs: docker-compose -f docker-compose-mysql.yml logs -f"
echo "  🔍 Check status: docker-compose -f docker-compose-mysql.yml ps"
echo "  🛑 Stop all: docker-compose -f docker-compose-mysql.yml down"
echo "  💾 Stop & remove data: docker-compose -f docker-compose-mysql.yml down -v"
echo ""
echo "MySQL Direct Access:"
echo "  docker exec -it petclinic-mysql mysql -u petclinic -ppetclinic123 petclinicdb"
echo ""
print_success "Setup complete!"

