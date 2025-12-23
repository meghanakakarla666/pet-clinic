# Pet Clinic - AWS EC2 Deployment Guide with CI/CD Pipeline

## 📋 Complete Step-by-Step Deployment Guide

This guide will walk you through deploying the Pet Clinic application on AWS EC2 with Jenkins CI/CD Pipeline and Docker containerization.

---

## 🎯 Project Overview

**Objective**: Deploy Pet Clinic Management System on AWS EC2 with automated CI/CD pipeline for Dr. Shawn's clinic.

**Technologies Used**:
- **Backend**: Spring Boot 2.7.14 with Java 11
- **Database**: MySQL 8.0
- **CI/CD**: Jenkins Pipeline
- **Containerization**: Docker
- **Cloud Platform**: AWS EC2
- **Build Tool**: Maven

---

## 📝 Prerequisites Checklist

Before starting, ensure you have:

- [ ] AWS Account with billing enabled
- [ ] AWS Access (Console or CLI configured)
- [ ] GitHub account
- [ ] Basic knowledge of Linux commands
- [ ] SSH client installed (Terminal, PuTTY, etc.)
- [ ] Git installed locally

---

## 🚀 PART 1: AWS EC2 Instance Setup

### Step 1.1: Launch EC2 Instance

1. **Login to AWS Console**
   - Navigate to EC2 Dashboard
   - Click "Launch Instance"

2. **Configure Instance**
   - **Name**: `pet-clinic-server`
   - **AMI**: Ubuntu Server 22.04 LTS (Free tier eligible)
   - **Instance Type**: `t2.medium` (minimum) or `t2.large` (recommended)
     - Jenkins + Docker + Maven requires at least 4GB RAM
   - **Key Pair**: Create new key pair
     - Name: `pet-clinic-key`
     - Type: RSA
     - Format: `.pem` (Mac/Linux) or `.ppk` (Windows/PuTTY)
     - **IMPORTANT**: Download and save securely!

3. **Configure Security Group**

   Create a new security group named `pet-clinic-sg` with these rules:

   | Type | Protocol | Port | Source | Description |
   |------|----------|------|--------|-------------|
   | SSH | TCP | 22 | My IP | SSH access |
   | HTTP | TCP | 80 | 0.0.0.0/0 | HTTP access |
   | Custom TCP | TCP | 8080 | 0.0.0.0/0 | Jenkins |
   | Custom TCP | TCP | 8081 | 0.0.0.0/0 | Pet Clinic App |
   | MySQL/Aurora | TCP | 3306 | sg-xxx (same security group) | MySQL access |

4. **Configure Storage**
   - Size: 20 GB minimum (30 GB recommended)
   - Type: gp3 (General Purpose SSD)

5. **Launch Instance**
   - Review settings
   - Click "Launch Instance"
   - Wait for instance state: `running`
   - Note down the **Public IPv4 address**

### Step 1.2: Connect to EC2 Instance

**For Mac/Linux:**
```bash
# Set key permissions
chmod 400 pet-clinic-key.pem

# Connect to EC2
ssh -i pet-clinic-key.pem ubuntu@YOUR-EC2-PUBLIC-IP
```

**For Windows (using PuTTY):**
1. Convert .pem to .ppk using PuTTYgen
2. Use PuTTY with:
   - Host: `ubuntu@YOUR-EC2-PUBLIC-IP`
   - Auth: Browse to your .ppk file

---

## 🔧 PART 2: EC2 Instance Configuration

### Step 2.1: Upload Setup Script

**On your local machine**, copy the setup script to EC2:

```bash
cd /path/to/pet-clinic
scp -i pet-clinic-key.pem scripts/ec2-setup.sh ubuntu@YOUR-EC2-PUBLIC-IP:~
```

### Step 2.2: Run Setup Script

**On EC2 instance:**

```bash
# Make script executable
chmod +x ec2-setup.sh

# Run setup script
./ec2-setup.sh
```

This script will install:
- ✓ OpenJDK 11
- ✓ Docker & Docker Compose
- ✓ Jenkins
- ✓ Git
- ✓ Maven
- ✓ Additional utilities

**Installation takes approximately 10-15 minutes.**

### Step 2.3: Post-Installation Steps

1. **Note Jenkins Initial Password**
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```
   **Copy this password** - you'll need it for Jenkins setup.

2. **Logout and Login Again** (required for Docker group changes)
   ```bash
   exit
   ssh -i pet-clinic-key.pem ubuntu@YOUR-EC2-PUBLIC-IP
   ```

3. **Verify Installations**
   ```bash
   java -version        # Should show OpenJDK 11
   docker --version     # Should show Docker version
   docker ps           # Should work without sudo
   mvn -version        # Should show Maven version
   systemctl status jenkins  # Should show active (running)
   ```

---

## 🗄️ PART 3: MySQL Database Setup

### Step 3.1: Install MySQL Server

```bash
# Install MySQL
sudo apt update
sudo apt install mysql-server -y

# Start MySQL service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure MySQL installation
sudo mysql_secure_installation
```

**MySQL Secure Installation Prompts:**
- Validate Password Component: `N`
- Remove anonymous users: `Y`
- Disallow root login remotely: `Y`
- Remove test database: `Y`
- Reload privilege tables: `Y`

### Step 3.2: Create Pet Clinic Database

```bash
# Login to MySQL
sudo mysql

# Run these SQL commands:
```

```sql
-- Create database
CREATE DATABASE petclinicdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'petclinic'@'localhost' IDENTIFIED BY 'petclinic123';

-- Grant privileges
GRANT ALL PRIVILEGES ON petclinicdb.* TO 'petclinic'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SHOW DATABASES;
SELECT user, host FROM mysql.user WHERE user='petclinic';

-- Exit
EXIT;
```

### Step 3.3: Test Database Connection

```bash
# Test connection
mysql -u petclinic -ppetclinic123 -e "USE petclinicdb; SHOW TABLES;"
```

---

## 📦 PART 4: GitHub Repository Setup

### Step 4.1: Create GitHub Repository

1. Go to GitHub.com
2. Click "New Repository"
3. **Repository Name**: `pet-clinic`
4. **Visibility**: Public
5. **Do NOT** initialize with README (we'll push existing code)
6. Click "Create Repository"

### Step 4.2: Push Code to GitHub

**On your local machine:**

```bash
cd /path/to/pet-clinic

# Initialize git (if not already done)
git init

# Add all files
git add .

# Commit
git commit -m "Initial commit - Pet Clinic with MySQL"

# Add remote
git remote add origin https://github.com/YOUR-USERNAME/pet-clinic.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### Step 4.3: Verify Repository

- Go to your GitHub repository
- Verify all files are uploaded
- Check that `Dockerfile`, `Jenkinsfile`, and `scripts/` are present

---

## 🔨 PART 5: Jenkins Configuration

### Step 5.1: Access Jenkins

1. Open browser: `http://YOUR-EC2-PUBLIC-IP:8080`
2. **Unlock Jenkins**:
   - Paste the initial admin password you saved earlier
   - If lost, retrieve it: `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`

### Step 5.2: Install Plugins

1. **Select**: "Install suggested plugins"
2. Wait for installation (5-10 minutes)
3. If any plugin fails, continue and install manually later

### Step 5.3: Create Admin User

- Username: `admin`
- Password: `your-secure-password`
- Full name: `Pet Clinic Admin`
- Email: `your-email@example.com`
- Click "Save and Continue"

### Step 5.4: Jenkins Instance Configuration

- **Jenkins URL**: `http://YOUR-EC2-PUBLIC-IP:8080/`
- Click "Save and Finish"
- Click "Start using Jenkins"

### Step 5.5: Install Additional Plugins

1. Go to **Manage Jenkins** → **Manage Plugins**
2. Click **Available** tab
3. Search and install:
   - [ ] Docker Pipeline
   - [ ] GitHub Integration
   - [ ] Pipeline
   - [ ] Git
4. Click "Install without restart"
5. Check "Restart Jenkins when installation is complete"

### Step 5.6: Configure Jenkins Global Tools

1. Go to **Manage Jenkins** → **Global Tool Configuration**

2. **JDK Configuration**:
   - Click "Add JDK"
   - Name: `JDK-11`
   - Uncheck "Install automatically"
   - JAVA_HOME: `/usr/lib/jvm/java-11-openjdk-amd64`

3. **Maven Configuration**:
   - Click "Add Maven"
   - Name: `Maven-3.8`
   - Uncheck "Install automatically"
   - MAVEN_HOME: `/usr/share/maven`

4. **Git Configuration**:
   - Usually auto-detected
   - Path: `/usr/bin/git`

5. Click "Save"

---

## 🚢 PART 6: Jenkins Pipeline Setup

### Step 6.1: Create Jenkins Pipeline Job

1. From Jenkins Dashboard, click **New Item**
2. **Enter item name**: `pet-clinic-pipeline`
3. **Select**: Pipeline
4. Click "OK"

### Step 6.2: Configure Pipeline

**General Tab:**
- Description: `Pet Clinic CI/CD Pipeline with MySQL and Docker`
- [x] GitHub project
- Project URL: `https://github.com/YOUR-USERNAME/pet-clinic/`

**Build Triggers:**
- [x] GitHub hook trigger for GITScm polling
- [x] Poll SCM (optional for backup)
  - Schedule: `H/5 * * * *` (every 5 minutes)

**Pipeline Tab:**
- Definition: `Pipeline script from SCM`
- SCM: `Git`
- Repository URL: `https://github.com/YOUR-USERNAME/pet-clinic.git`
- Credentials: None (for public repo)
- Branch Specifier: `*/main`
- Script Path: `Jenkinsfile`

Click "Save"

### Step 6.3: Configure GitHub Webhook (Optional)

1. Go to your GitHub repository
2. Settings → Webhooks → Add webhook
3. Payload URL: `http://YOUR-EC2-PUBLIC-IP:8080/github-webhook/`
4. Content type: `application/json`
5. Events: `Just the push event`
6. Click "Add webhook"

---

## 🎯 PART 7: Build and Deploy

### Step 7.1: Run First Build

1. Go to Jenkins Dashboard
2. Click on `pet-clinic-pipeline`
3. Click **Build Now**
4. Watch the build progress in **Build History**
5. Click on build number → **Console Output** to see logs

### Step 7.2: Pipeline Stages

The pipeline will execute these stages:

1. **Checkout**: Clone code from GitHub
2. **Build**: Compile with Maven
3. **Test**: Run unit tests
4. **Docker Build**: Create Docker image
5. **Stop Previous Container**: Clean up old deployment
6. **Deploy Application**: Run new container with MySQL
7. **Health Check**: Verify application is running

**Expected build time**: 5-10 minutes (first build)

### Step 7.3: Verify Deployment

**Check Docker containers:**
```bash
docker ps

# Should see pet-clinic container running
```

**Check application logs:**
```bash
docker logs pet-clinic
```

**Test application:**
```bash
curl http://localhost:8081/pet-clinic/
```

### Step 7.4: Access Application

**Open in browser:**
```
http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/
```

**Test pages:**
- Homepage: `http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/`
- Owners: `http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/owners`
- Pets: `http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/pets`
- Consultations: `http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/consultations`

**Test API:**
```bash
curl http://YOUR-EC2-PUBLIC-IP:8081/pet-clinic/api/owners
```

---

## 🔍 PART 8: Verification & Testing

### Step 8.1: Database Verification

```bash
# Check database tables
mysql -u petclinic -ppetclinic123 -e "USE petclinicdb; SHOW TABLES;"

# Check data count
mysql -u petclinic -ppetclinic123 -e "
USE petclinicdb;
SELECT 'Owners' as Table_Name, COUNT(*) as Count FROM owners
UNION ALL
SELECT 'Pets', COUNT(*) FROM pets
UNION ALL
SELECT 'Consultations', COUNT(*) FROM consultations;
"
```

**Expected output:**
- owners: 5 records
- pets: 7 records
- consultations: 10+ records

### Step 8.2: Application Health Check

```bash
# Check application health
curl http://localhost:8081/pet-clinic/

# Check Docker container status
docker ps | grep pet-clinic

# Check application logs
docker logs pet-clinic | tail -50
```

### Step 8.3: Jenkins Pipeline Verification

1. Go to Jenkins Pipeline
2. Verify all stages show ✓ (green checkmark)
3. Check Console Output for any errors
4. Verify build artifacts

---

## 📸 PART 9: Screenshots for Submission

### Required Screenshots:

1. **AWS EC2 Dashboard**
   - Show running instance with public IP
   - Instance details page

2. **EC2 Security Group**
   - Show all inbound rules (ports 22, 80, 8080, 8081, 3306)

3. **SSH Terminal**
   - Show `docker ps` output
   - Show `systemctl status jenkins` output
   - Show MySQL connection test

4. **Jenkins Dashboard**
   - Show installed plugins
   - Show pipeline job list

5. **Jenkins Pipeline Build**
   - Show successful build with all green stages
   - Show console output

6. **Jenkins Pipeline Stages View**
   - Show stage view with all stages completed

7. **Application Homepage**
   - Screenshot of Pet Clinic homepage in browser
   - Show URL bar with EC2 public IP

8. **Application Pages**
   - Owners list page
   - Pets list page
   - Consultations list page

9. **API Response**
   - Screenshot of `/api/owners` JSON response
   - Can use browser or Postman

10. **Database Verification**
    - MySQL query showing tables
    - Query showing data counts

11. **Docker Containers**
    - `docker ps` showing pet-clinic container running
    - `docker images` showing pet-clinic image

12. **Application Logs**
    - `docker logs pet-clinic` showing successful startup

---

## 📦 PART 10: Project Submission Package

### Files to Include in Submission:

```
pet-clinic-submission.zip/
├── source-code/
│   ├── src/
│   ├── Dockerfile
│   ├── Jenkinsfile
│   ├── docker-compose.yml
│   ├── pom.xml
│   ├── settings.xml
│   └── scripts/
│       ├── ec2-setup.sh
│       ├── deploy.sh
│       └── init-mysql.sql
├── database/
│   └── init-mysql.sql
├── screenshots/
│   ├── 01-ec2-instance.png
│   ├── 02-security-group.png
│   ├── 03-ssh-terminal.png
│   ├── 04-jenkins-dashboard.png
│   ├── 05-jenkins-pipeline.png
│   ├── 06-pipeline-stages.png
│   ├── 07-app-homepage.png
│   ├── 08-owners-page.png
│   ├── 09-pets-page.png
│   ├── 10-consultations-page.png
│   ├── 11-api-response.png
│   ├── 12-database-verification.png
│   ├── 13-docker-containers.png
│   └── 14-application-logs.png
├── documentation/
│   ├── AWS_EC2_DEPLOYMENT_GUIDE.md
│   ├── README.md
│   └── PROJECT_REPORT.md
└── README.txt (Brief overview)
```

### Create Submission Package:

```bash
# On your local machine
cd /path/to/pet-clinic

# Create submission directory
mkdir pet-clinic-submission
cd pet-clinic-submission

# Create subdirectories
mkdir source-code database screenshots documentation

# Copy source code (exclude target, .git, etc.)
cp -r ../src source-code/
cp ../Dockerfile ../Jenkinsfile ../docker-compose.yml source-code/
cp ../pom.xml ../settings.xml source-code/
cp -r ../scripts source-code/

# Copy database scripts
cp ../scripts/init-mysql.sql database/

# Add your screenshots to screenshots/

# Copy documentation
cp ../AWS_EC2_DEPLOYMENT_GUIDE.md documentation/
cp ../README.md documentation/

# Create README.txt
cat > README.txt << 'EOF'
PET CLINIC AWS EC2 DEPLOYMENT PROJECT
=====================================

Student: [Your Name]
Date: [Submission Date]
Project: Pet Clinic Management System with CI/CD Pipeline

Contents:
- source-code/: Complete Spring Boot application source code
- database/: MySQL initialization scripts
- screenshots/: All required screenshots demonstrating working deployment
- documentation/: Deployment guides and project documentation

Deployment URLs:
- Application: http://[YOUR-EC2-IP]:8081/pet-clinic/
- Jenkins: http://[YOUR-EC2-IP]:8080/
- GitHub: https://github.com/[YOUR-USERNAME]/pet-clinic

Technologies Used:
- Spring Boot 2.7.14
- MySQL 8.0
- Jenkins CI/CD
- Docker
- AWS EC2
- Maven

All requirements completed successfully.
EOF

# Create zip file
cd ..
zip -r pet-clinic-submission.zip pet-clinic-submission/

echo "Submission package created: pet-clinic-submission.zip"
```

---

## 🔧 PART 11: Troubleshooting

### Common Issues and Solutions:

#### Issue 1: Jenkins Build Fails - Maven Repository

**Error**: Cannot resolve dependencies

**Solution**:
```bash
# SSH to EC2
cd /opt/petclinic
mvn clean package -s settings.xml -U
```

#### Issue 2: Docker Permission Denied

**Error**: `permission denied while trying to connect to Docker daemon`

**Solution**:
```bash
# Add user to docker group
sudo usermod -aG docker $USER
sudo usermod -aG docker jenkins

# Restart services
sudo systemctl restart docker
sudo systemctl restart jenkins

# Logout and login again
exit
ssh -i pet-clinic-key.pem ubuntu@YOUR-EC2-PUBLIC-IP
```

#### Issue 3: Application Cannot Connect to MySQL

**Error**: `Communications link failure`

**Solution**:
```bash
# Check MySQL is running
sudo systemctl status mysql

# Verify database exists
mysql -u petclinic -ppetclinic123 -e "SHOW DATABASES;"

# Check MySQL logs
sudo tail -f /var/log/mysql/error.log
```

#### Issue 4: Port Already in Use

**Error**: `Port 8081 is already in use`

**Solution**:
```bash
# Find process using port
sudo lsof -i :8081

# Stop container
docker stop pet-clinic
docker rm pet-clinic

# Or kill process
kill -9 [PID]
```

#### Issue 5: Out of Memory

**Error**: `Java heap space` or `Cannot allocate memory`

**Solution**:
- Upgrade to t2.large instance (8GB RAM)
- Or modify JVM settings in Dockerfile

#### Issue 6: Health Check Fails

**Error**: Pipeline fails at health check stage

**Solution**:
```bash
# Check container logs
docker logs pet-clinic

# Check if app is running
docker exec -it pet-clinic curl http://localhost:8081/pet-clinic/

# Increase health check timeout in Jenkinsfile
```

---

## 📊 PART 12: Monitoring & Maintenance

### View Application Logs:
```bash
# Real-time logs
docker logs -f pet-clinic

# Last 100 lines
docker logs pet-clinic --tail 100
```

### Restart Application:
```bash
docker restart pet-clinic
```

### Database Backup:
```bash
# Backup database
mysqldump -u petclinic -ppetclinic123 petclinicdb > backup_$(date +%Y%m%d).sql

# Restore database
mysql -u petclinic -ppetclinic123 petclinicdb < backup_20231201.sql
```

### Update Application:
```bash
# Trigger Jenkins build
# Or run deployment script
cd /opt/petclinic
./scripts/deploy.sh
```

---

## ✅ Final Checklist Before Submission

- [ ] EC2 instance running with correct security groups
- [ ] Jenkins accessible and pipeline configured
- [ ] MySQL database created with sample data
- [ ] Application deployed and accessible via browser
- [ ] All 4 pages working (Home, Owners, Pets, Consultations)
- [ ] API endpoints returning data
- [ ] Docker containers running
- [ ] GitHub repository public and complete
- [ ] All 14 screenshots captured and named correctly
- [ ] Source code zipped
- [ ] Database script included
- [ ] Documentation complete
- [ ] README.txt created with URLs and information

---

## 🎓 Learning Outcomes Achieved

✓ AWS EC2 instance setup and configuration
✓ Linux server administration
✓ Jenkins CI/CD pipeline implementation
✓ Docker containerization
✓ MySQL database management
✓ Git version control
✓ Spring Boot application deployment
✓ Security group and firewall configuration
✓ DevOps best practices
✓ Cloud infrastructure management

---

## 📞 Support

If you encounter issues:

1. Check troubleshooting section
2. Review Jenkins console output
3. Check Docker logs
4. Verify MySQL connection
5. Check EC2 security groups
6. Review application logs

---

**Congratulations! You have successfully deployed Pet Clinic on AWS EC2 with complete CI/CD pipeline!** 🎉

---

**Project Developed by**: Bella Solutions  
**For**: Dr. Shawn's Pet Clinic  
**Date**: December 2025

