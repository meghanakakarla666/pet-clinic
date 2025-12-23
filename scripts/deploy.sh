#!/bin/bash

# Deployment script for Pet Clinic application
# This script pulls the latest code, builds, and deploys the application

set -e

echo "Starting deployment of Pet Clinic application..."

# Configuration
APP_NAME="pet-clinic"
GIT_REPO="https://github.com/meghanakakarla666/pet-clinic.git"
APP_DIR="/opt/petclinic"
DOCKER_IMAGE="pet-clinic"

# Navigate to application directory
cd $APP_DIR

# Pull latest code
echo "Pulling latest code..."
if [ -d ".git" ]; then
    git pull origin main
else
    git clone $GIT_REPO .
fi

# Build the application
echo "Building application..."
./mvnw clean package -DskipTests

# Stop existing containers
echo "Stopping existing containers..."
docker stop $APP_NAME || true
docker rm $APP_NAME || true

# Build new Docker image
echo "Building Docker image..."
docker build -t $DOCKER_IMAGE:latest .

# Run new container
echo "Starting new container..."
docker run -d \
    --name $APP_NAME \
    -p 8080:8080 \
    --restart unless-stopped \
    $DOCKER_IMAGE:latest

# Wait for application to start
echo "Waiting for application to start..."
sleep 30

# Health check
echo "Performing health check..."
if curl -f http://localhost:8080/actuator/health; then
    echo "Deployment successful! Application is running."
else
    echo "Deployment failed! Application health check failed."
    docker logs $APP_NAME
    exit 1
fi

echo "Pet Clinic application deployed successfully!"
echo "Access the application at: http://$(curl -s http://169.254.169.254/latest/meta-data/public-hostname):8080"
