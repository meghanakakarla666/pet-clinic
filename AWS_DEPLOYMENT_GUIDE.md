# AWS EC2 Deployment Guide - Pet Clinic Application

**Project:** Pet Clinic Management System for Dr. Shawn  
**Developer:** Bella Solutions  
**Date:** December 2025

---

## 📋 Table of Contents
1. [Prerequisites](#prerequisites)
2. [Step 1: Create AWS EC2 Instance](#step-1-create-aws-ec2-instance)
3. [Step 2: Configure EC2 Instance](#step-2-configure-ec2-instance)
4. [Step 3: Install Required Software](#step-3-install-required-software)
5. [Step 4: Setup Git Repository](#step-4-setup-git-repository)
6. [Step 5: Configure Jenkins](#step-5-configure-jenkins)
7. [Step 6: Build Docker Image](#step-6-build-docker-image)
8. [Step 7: Deploy Application](#step-7-deploy-application)
9. [Step 8: Verify Deployment](#step-8-verify-deployment)
10. [Troubleshooting](#troubleshooting)

---

## Prerequisites

Before you begin, ensure you have:
- ✅ AWS Account with EC2 access
- ✅ GitHub account for code repository
- ✅ Basic knowledge of Linux commands
- ✅ SSH client (Terminal, PuTTY)
- ✅ This project source code

---

## Step 1: Create AWS EC2 Instance

### 1.1 Launch EC2 Instance

1. **Login to AWS Console**: https://console.aws.amazon.com/
2. **Navigate to EC2**: Services → EC2 → Launch Instance
3. **Configure Instance:**
   ```
   Name: pet-clinic-server
   AMI: Ubuntu Server 22.04 LTS (Free tier eligible)
   Instance Type: t2.medium (minimum recommended)
   Key Pair: Create new or use existing (.pem file)
   ```

### 1.2 Configure Security Group

**Inbound Rules:**
```
Type            Protocol    Port Range    Source          Description
SSH             TCP         22            Your IP         SSH access
HTTP            TCP         80            0.0.0.0/0       HTTP access
Custom TCP      TCP         8080          0.0.0.0/0       Jenkins
Custom TCP      TCP         8081          0.0.0.0/0       Application
Custom TCP      TCP         3306          Your IP         MySQL (optional)
```

### 1.3 Storage Configuration
```
Size: 20 GB
Type: General Purpose SSD (gp3)
```

### 1.4 Launch Instance
- Review and launch
- **Save the .pem key file** securely
- Wait for instance to be in "Running" state

---

## Step 2: Configure EC2 Instance

### 2.1 Connect to EC2

**For Mac/Linux:**
```bash
chmod 400 your-key.pem
ssh -i your-key.pem ubuntu@<your-ec2-public-ip>
```

**For Windows (PuTTY):**
- Convert .pem to .ppk using PuTTYgen
- Use PuTTY with the .ppk file

### 2.2 Update System
```bash
sudo apt update
sudo apt upgrade -y
```

---

## Step 3: Install Required Software

### 3.1 Install JDK 11

```bash
# Install OpenJDK 11
sudo apt install openjdk-11-jdk -y

# Verify installation
java -version
javac -version

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$PATH:$JAVA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
```

### 3.2 Install Docker

```bash
# Install Docker
sudo apt install apt-transport-https ca-certificates curl software-properties-common -y
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io -y

# Add user to docker group
sudo usermod -aG docker $USER
sudo usermod -aG docker jenkins

# Start Docker
sudo systemctl start docker
sudo systemctl enable docker

# Verify installation
docker --version
docker run hello-world
```

**Important:** Log out and log back in for group changes to take effect!

### 3.3 Install Jenkins

```bash
# Add Jenkins repository
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

# Install Jenkins
sudo apt update
sudo apt install jenkins -y

# Start Jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins

# Check status
sudo systemctl status jenkins

# Get initial admin password
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

**Save this password!** You'll need it to unlock Jenkins.

### 3.4 Install Git & Maven

```bash
# Install Git
sudo apt install git -y
git --version

# Install Maven
sudo apt install maven -y
mvn -version
```

---

## Step 4: Setup Git Repository

### 4.1 Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `pet-clinic`
3. Description: Pet Clinic Management System
4. Public or Private (your choice)
5. Click "Create repository"

### 4.2 Push Code to GitHub

**On your local machine:**

```bash
cd /Users/mekakarl/pet-clinic

# Initialize git (if not already)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - Pet Clinic Application"

# Add remote repository
git remote add origin https://github.com/meghanakakarla666/pet-clinic.git

# Push to GitHub
git push -u origin main
```

**Note:** Replace `YOUR_USERNAME` with your GitHub username.

---

## Step 5: Configure Jenkins

### 5.1 Access Jenkins

1. Open browser: `http://<your-ec2-public-ip>:8080`
2. Enter the initial admin password from Step 3.3
3. Click "Install suggested plugins"
4. Create admin user
5. Save and finish

### 5.2 Install Required Jenkins Plugins

**Navigate to:** Manage Jenkins → Manage Plugins → Available

Install these plugins:
- ✅ Git Plugin
- ✅ GitHub Integration Plugin
- ✅ Docker Plugin
- ✅ Docker Pipeline Plugin
- ✅ Pipeline Plugin
- ✅ Maven Integration Plugin

Click "Install without restart"

### 5.3 Configure Jenkins Tools

**Navigate to:** Manage Jenkins → Global Tool Configuration

**Maven Configuration:**
```
Name: Maven-3.8
Install automatically: ✓
Version: 3.8.x
```

**JDK Configuration:**
```
Name: JDK-11
JAVA_HOME: /usr/lib/jvm/java-11-openjdk-amd64
Install automatically: ✗
```

**Docker Configuration:**
```
Name: Docker
Install automatically: ✓
```

Click "Save"

### 5.4 Add GitHub Credentials

**Navigate to:** Manage Jenkins → Manage Credentials → Global → Add Credentials

```
Kind: Username with password
Username: your-github-username
Password: your-github-personal-access-token
ID: github-credentials
Description: GitHub Access
```

**To create GitHub token:**
1. GitHub → Settings → Developer settings → Personal access tokens → Generate new token
2. Select scopes: `repo`, `admin:repo_hook`
3. Copy the token

### 5.5 Create Jenkins Pipeline

1. **Click "New Item"**
2. **Enter name:** `pet-clinic-pipeline`
3. **Select:** Pipeline
4. **Click OK**

5. **Configure Pipeline:**

**General:**
```
✓ GitHub project
Project url: https://github.com/meghanakakarla666/pet-clinic
```

**Build Triggers:**
```
✓ GitHub hook trigger for GITScm polling
```

**Pipeline:**
```
Definition: Pipeline script from SCM
SCM: Git
Repository URL: https://github.com/meghanakakarla666/pet-clinic.git
Credentials: github-credentials
Branch: */main
Script Path: Jenkinsfile
```

6. **Click "Save"**

---

## Step 6: Build Docker Image

### 6.1 Manually Build (First Time)

**On EC2 instance:**

```bash
# Clone repository
cd ~
git clone https://github.com/meghanakakarla666/pet-clinic.git
cd pet-clinic

# Build with Maven
mvn clean package -DskipTests

# Build Docker image
docker build -t pet-clinic:latest .

# Verify image
docker images | grep pet-clinic
```

### 6.2 Run Container Locally

```bash
# Run the container
docker run -d \
  --name pet-clinic-app \
  -p 8081:8081 \
  pet-clinic:latest

# Check container status
docker ps

# Check logs
docker logs pet-clinic-app

# Follow logs
docker logs -f pet-clinic-app
```

---

## Step 7: Deploy Application

### 7.1 Trigger Jenkins Build

**Option 1: Manual Trigger**
1. Go to Jenkins Dashboard
2. Click on `pet-clinic-pipeline`
3. Click "Build Now"

**Option 2: Automatic Trigger (GitHub Webhook)**
1. GitHub → Repository → Settings → Webhooks → Add webhook
2. Payload URL: `http://<your-ec2-public-ip>:8080/github-webhook/`
3. Content type: `application/json`
4. Select: "Just the push event"
5. Active: ✓
6. Click "Add webhook"

Now every git push will trigger Jenkins build!

### 7.2 Monitor Build

1. Click on build number (e.g., #1)
2. Click "Console Output"
3. Watch the build progress

**Expected Stages:**
```
✓ Checkout
✓ Build
✓ Docker Build
✓ Deploy to Staging
✓ Health Check
```

---

## Step 8: Verify Deployment

### 8.1 Check Application Status

```bash
# Check running containers
docker ps

# Check application logs
docker logs pet-clinic-staging

# Check health endpoint
curl http://localhost:8081/pet-clinic/actuator/health
```

### 8.2 Access Application

**From Browser:**
```
http://<your-ec2-public-ip>:8081/pet-clinic/
```

**Test Features:**
- ✓ Homepage loads
- ✓ View Owners
- ✓ View Pets
- ✓ View Consultations
- ✓ Add new owner
- ✓ Register new pet
- ✓ Create consultation

---

## Step 9: Setup MySQL (Optional - For Production)

### 9.1 Install MySQL in Docker

```bash
# Create network
docker network create petclinic-network

# Run MySQL container
docker run -d \
  --name petclinic-mysql \
  --network petclinic-network \
  -e MYSQL_ROOT_PASSWORD=rootpassword \
  -e MYSQL_DATABASE=petclinicdb \
  -e MYSQL_USER=petclinic \
  -e MYSQL_PASSWORD=petclinic123 \
  -p 3306:3306 \
  mysql:8.0

# Wait for MySQL to start
sleep 30

# Test connection
docker exec -it petclinic-mysql mysql -u petclinic -ppetclinic123 -e "SHOW DATABASES;"
```

### 9.2 Run Application with MySQL

```bash
# Stop H2 version
docker stop pet-clinic-staging
docker rm pet-clinic-staging

# Run with MySQL
docker run -d \
  --name pet-clinic-prod \
  --network petclinic-network \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://petclinic-mysql:3306/petclinicdb \
  -e SPRING_DATASOURCE_USERNAME=petclinic \
  -e SPRING_DATASOURCE_PASSWORD=petclinic123 \
  -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
  pet-clinic:latest

# Check logs
docker logs -f pet-clinic-prod
```

---

## Step 10: Submission Requirements

### 10.1 Documents to Prepare

**1. Source Code (ZIP)**
```bash
cd /Users/mekakarl/pet-clinic
zip -r pet-clinic-source-code.zip . -x "target/*" "*.class" ".git/*"
```

**2. Database Scripts**
- Already have: `src/main/resources/data.sql`
- Already have: `scripts/init.sql`

**3. Screenshots to Take:**

✓ **Screenshot 1:** AWS EC2 Instance Running
- Show EC2 dashboard with instance in "Running" state

✓ **Screenshot 2:** EC2 Security Group Configuration
- Show inbound rules

✓ **Screenshot 3:** SSH Connection to EC2
- Show terminal connected to EC2

✓ **Screenshot 4:** Software Versions
```bash
java -version
docker --version
jenkins --version
mvn -version
```

✓ **Screenshot 5:** Jenkins Dashboard
- Show Jenkins home page

✓ **Screenshot 6:** Jenkins Pipeline Configuration
- Show pipeline setup

✓ **Screenshot 7:** Successful Jenkins Build
- Show green checkmarks for all stages

✓ **Screenshot 8:** Docker Images
```bash
docker images
```

✓ **Screenshot 9:** Running Docker Containers
```bash
docker ps
```

✓ **Screenshot 10:** Application Homepage
- Show browser with application running on EC2 public IP

✓ **Screenshot 11:** Application Features Working
- Show owners list, pets list, etc.

✓ **Screenshot 12:** Database Connection
- Show H2 console or MySQL connection

---

## Troubleshooting

### Issue 1: Cannot connect to EC2

**Solution:**
```bash
# Check security group allows your IP on port 22
# Verify .pem file permissions
chmod 400 your-key.pem
```

### Issue 2: Jenkins won't start

**Solution:**
```bash
sudo systemctl status jenkins
sudo journalctl -u jenkins -f
# Check Java is installed
java -version
```

### Issue 3: Docker permission denied

**Solution:**
```bash
sudo usermod -aG docker $USER
sudo usermod -aG docker jenkins
# Log out and log back in
```

### Issue 4: Application won't build

**Solution:**
```bash
# Check Maven installation
mvn -version
# Try manual build
cd ~/pet-clinic
mvn clean package -DskipTests
```

### Issue 5: Cannot access application

**Solution:**
```bash
# Check container is running
docker ps
# Check logs
docker logs pet-clinic-staging
# Verify security group allows port 8081
# Try from EC2 itself
curl http://localhost:8081/pet-clinic/
```

### Issue 6: Port already in use

**Solution:**
```bash
# Find process using port
sudo lsof -i :8081
# Kill old container
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
```

---

## Quick Reference Commands

### Docker Commands
```bash
# List images
docker images

# List containers
docker ps -a

# Stop container
docker stop <container-name>

# Remove container
docker rm <container-name>

# View logs
docker logs <container-name>

# Execute command in container
docker exec -it <container-name> bash
```

### Jenkins Commands
```bash
# Restart Jenkins
sudo systemctl restart jenkins

# View Jenkins logs
sudo journalctl -u jenkins -f

# Check Jenkins status
sudo systemctl status jenkins
```

### Git Commands
```bash
# Pull latest changes
git pull origin main

# Push changes
git add .
git commit -m "your message"
git push origin main
```

---

## Success Checklist

Before submission, verify:

- [ ] EC2 instance is running
- [ ] JDK 11 installed and configured
- [ ] Docker installed and running
- [ ] Jenkins installed and accessible
- [ ] Git repository created and synced
- [ ] Jenkins pipeline configured
- [ ] Docker image built successfully
- [ ] Application deployed and accessible
- [ ] All features working (CRUD operations)
- [ ] Screenshots taken (12 minimum)
- [ ] Source code zipped
- [ ] Database scripts prepared
- [ ] Documentation complete

---

## Support Resources

- **AWS EC2 Documentation:** https://docs.aws.amazon.com/ec2/
- **Jenkins Documentation:** https://www.jenkins.io/doc/
- **Docker Documentation:** https://docs.docker.com/
- **Spring Boot Documentation:** https://spring.io/projects/spring-boot

---

**Deployment Date:** _____________  
**Deployed By:** _____________  
**EC2 Instance IP:** _____________  
**Application URL:** http://_____________:8081/pet-clinic/

---

*Good luck with your deployment! 🚀*

