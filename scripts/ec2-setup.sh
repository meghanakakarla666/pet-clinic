#!/bin/bash

###############################################################################
# EC2 Instance Setup Script for Pet Clinic Application
# This script installs and configures all required software on Ubuntu EC2
###############################################################################

set -e  # Exit on any error

echo "========================================="
echo "Pet Clinic EC2 Setup Script"
echo "========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}➜ $1${NC}"
}

# Check if running as root
if [ "$EUID" -eq 0 ]; then 
    print_error "Please do not run as root. Run as ubuntu user."
    exit 1
fi

print_info "Starting EC2 instance setup..."
echo ""

###############################################################################
# Step 1: Update System
###############################################################################
print_info "Step 1: Updating system packages..."
sudo apt update
sudo apt upgrade -y
print_success "System updated successfully"
echo ""

###############################################################################
# Step 2: Install JDK 11
###############################################################################
print_info "Step 2: Installing OpenJDK 11..."
sudo apt install openjdk-11-jdk -y

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc

java -version
print_success "JDK 11 installed successfully"
echo ""

###############################################################################
# Step 3: Install Docker
###############################################################################
print_info "Step 3: Installing Docker..."

# Install prerequisites
sudo apt install -y apt-transport-https ca-certificates curl software-properties-common

# Add Docker GPG key
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# Add Docker repository
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io

# Add user to docker group
sudo usermod -aG docker $USER

# Start Docker
sudo systemctl start docker
sudo systemctl enable docker

docker --version
print_success "Docker installed successfully"
echo ""

###############################################################################
# Step 4: Install Jenkins
###############################################################################
print_info "Step 4: Installing Jenkins..."

# Add Jenkins repository
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null

echo "deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/" | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

# Install Jenkins
sudo apt update
sudo apt install -y jenkins

# Add jenkins to docker group
sudo usermod -aG docker jenkins

# Start Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Wait for Jenkins to start
print_info "Waiting for Jenkins to start..."
sleep 30

sudo systemctl status jenkins --no-pager
print_success "Jenkins installed successfully"
echo ""

###############################################################################
# Step 5: Install Git
###############################################################################
print_info "Step 5: Installing Git..."
sudo apt install -y git
git --version
print_success "Git installed successfully"
echo ""

###############################################################################
# Step 6: Install Maven
###############################################################################
print_info "Step 6: Installing Maven..."
sudo apt install -y maven
mvn -version
print_success "Maven installed successfully"
echo ""

###############################################################################
# Step 7: Install useful tools
###############################################################################
print_info "Step 7: Installing additional tools..."
sudo apt install -y curl wget nano vim htop
print_success "Additional tools installed"
echo ""

###############################################################################
# Display Installation Summary
###############################################################################
echo ""
echo "========================================="
echo "Installation Complete!"
echo "========================================="
echo ""
echo "Installed Software Versions:"
echo "----------------------------"
echo -n "Java: "
java -version 2>&1 | head -1
echo -n "Docker: "
docker --version
echo -n "Git: "
git --version
echo -n "Maven: "
mvn -version 2>&1 | head -1
echo ""
echo "Jenkins Status:"
echo "----------------------------"
sudo systemctl status jenkins --no-pager | grep Active
echo ""
echo "Important Information:"
echo "----------------------------"
echo "1. Jenkins Initial Admin Password:"
echo ""
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
echo ""
echo "2. Access Jenkins at: http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8080"
echo ""
echo "3. IMPORTANT: You must log out and log back in for Docker group changes to take effect!"
echo "   Run: exit"
echo "   Then reconnect via SSH"
echo ""
echo "4. After reconnecting, verify Docker works without sudo:"
echo "   docker ps"
echo ""
echo "5. Next steps:"
echo "   - Open Jenkins in browser"
echo "   - Complete Jenkins setup wizard"
echo "   - Install suggested plugins"
echo "   - Clone your Git repository"
echo "   - Create Jenkins pipeline"
echo ""
print_success "Setup script completed successfully!"
echo ""
