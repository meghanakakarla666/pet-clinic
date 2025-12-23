#!/bin/bash

###############################################################################
# Deployment script for Pet Clinic application
# This script pulls the latest code, builds, and deploys the application
###############################################################################

set -e

echo "========================================="
echo "Pet Clinic Deployment Script"
echo "========================================="

# Configuration
APP_NAME="pet-clinic"
GIT_REPO="https://github.com/YOUR-GITHUB-USERNAME/pet-clinic.git"
APP_DIR="/opt/petclinic"
DOCKER_IMAGE="pet-clinic"
APP_PORT="8081"

# MySQL Configuration
MYSQL_HOST="localhost"
MYSQL_DATABASE="petclinicdb"
MYSQL_USER="petclinic"
MYSQL_PASSWORD="petclinic123"

# Create application directory if doesn't exist
if [ ! -d "$APP_DIR" ]; then
    echo "Creating application directory..."
    sudo mkdir -p $APP_DIR
    sudo chown $USER:$USER $APP_DIR
fi

# Navigate to application directory
cd $APP_DIR

# Pull latest code
echo "Pulling latest code from repository..."
if [ -d ".git" ]; then
    git pull origin main
else
    git clone $GIT_REPO .
fi

# Build the application
echo "Building application with Maven..."
mvn clean package -DskipTests -s settings.xml

# Stop existing containers
echo "Stopping existing containers..."
docker stop $APP_NAME 2>/dev/null || true
docker rm $APP_NAME 2>/dev/null || true

# Remove old images (optional - keeps only last 2 versions)
echo "Cleaning up old Docker images..."
docker images | grep $DOCKER_IMAGE | tail -n +3 | awk '{print $3}' | xargs -r docker rmi || true

# Build new Docker image
echo "Building Docker image..."
docker build -t $DOCKER_IMAGE:latest .

# Run new container with MySQL connection
echo "Starting new container with MySQL configuration..."
docker run -d \
    --name $APP_NAME \
    -p $APP_PORT:8081 \
    -e SPRING_DATASOURCE_URL="jdbc:mysql://${MYSQL_HOST}:3306/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true" \
    -e SPRING_DATASOURCE_USERNAME=$MYSQL_USER \
    -e SPRING_DATASOURCE_PASSWORD=$MYSQL_PASSWORD \
    -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
    --restart unless-stopped \
    $DOCKER_IMAGE:latest

# Wait for application to start
echo "Waiting for application to start..."
sleep 45

# Health check
echo "Performing health check..."
MAX_ATTEMPTS=10
ATTEMPT=1

while [ $ATTEMPT -le $MAX_ATTEMPTS ]; do
    echo "Health check attempt $ATTEMPT/$MAX_ATTEMPTS..."
    if curl -f http://localhost:$APP_PORT/pet-clinic/ > /dev/null 2>&1; then
        echo "✓ Deployment successful! Application is running."
        echo ""
        echo "========================================="
        echo "Deployment Information:"
        echo "========================================="
        echo "Application URL: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):$APP_PORT/pet-clinic/"
        echo "Container Name: $APP_NAME"
        echo "Port: $APP_PORT"
        echo "Database: $MYSQL_DATABASE"
        echo ""
        echo "To view logs: docker logs $APP_NAME"
        echo "To stop app: docker stop $APP_NAME"
        echo "========================================="
        exit 0
    fi
    sleep 5
    ATTEMPT=$((ATTEMPT + 1))
done

echo "✗ Deployment failed! Application health check failed."
echo "Showing application logs:"
docker logs $APP_NAME
exit 1
