# 🚀 Quick Start Guide - AWS Deployment

**For:** Pet Clinic Application  
**Target:** AWS EC2 Instance  
**Time Required:** 30-45 minutes

---

## 📝 Before You Start

### Required Accounts
- [ ] AWS Account (Free tier eligible)
- [ ] GitHub Account

### Required Files
- [ ] AWS Key Pair (.pem file)
- [ ] This source code

---

## ⚡ 5-Step Deployment Process

### Step 1: Launch EC2 Instance (10 minutes)

1. **Login to AWS Console**
   - Go to: https://console.aws.amazon.com/ec2/

2. **Launch Instance**
   ```
   Click: Launch Instance
   
   Settings:
   - Name: pet-clinic-server
   - AMI: Ubuntu Server 22.04 LTS
   - Instance Type: t2.medium
   - Key Pair: Create new (save .pem file!)
   
   Security Group - Add Rules:
   - SSH (22) - Your IP
   - HTTP (80) - Anywhere
   - Custom TCP (8080) - Anywhere [Jenkins]
   - Custom TCP (8081) - Anywhere [Application]
   ```

3. **Launch and Wait**
   - Click "Launch Instance"
   - Wait for status: "Running"
   - **Note the Public IP address**

---

### Step 2: Connect & Setup EC2 (15 minutes)

1. **Connect to EC2**
   ```bash
   chmod 400 your-key.pem
   ssh -i your-key.pem ubuntu@<EC2-PUBLIC-IP>
   ```

2. **Download and Run Setup Script**
   ```bash
   # Update system first
   sudo apt update && sudo apt upgrade -y
   
   # Download setup script
   wget https://raw.githubusercontent.com/YOUR_USERNAME/pet-clinic/main/scripts/ec2-setup.sh
   
   # Make executable
   chmod +x ec2-setup.sh
   
   # Run setup script
   ./ec2-setup.sh
   ```

   **OR Manual Setup** (if wget doesn't work):
   ```bash
   # Copy the ec2-setup.sh script content
   nano ec2-setup.sh
   # Paste the script content
   # Save: Ctrl+X, then Y, then Enter
   
   chmod +x ec2-setup.sh
   ./ec2-setup.sh
   ```

3. **Important: Reconnect**
   ```bash
   # Exit SSH
   exit
   
   # Reconnect (for Docker group changes)
   ssh -i your-key.pem ubuntu@<EC2-PUBLIC-IP>
   
   # Verify Docker works
   docker ps
   ```

4. **Save Jenkins Password**
   ```bash
   sudo cat /var/lib/jenkins/secrets/initialAdminPassword
   ```
   **Copy this password!**

---

### Step 3: Setup GitHub Repository (5 minutes)

1. **Create GitHub Repository**
   - Go to: https://github.com/new
   - Name: `pet-clinic`
   - Public repository
   - Click "Create repository"

2. **Push Code to GitHub**
   
   **On your local machine:**
   ```bash
   cd /Users/mekakarl/pet-clinic
   
   # Initialize git (if needed)
   git init
   
   # Add all files
   git add .
   
   # Commit
   git commit -m "Initial commit for AWS deployment"
   
   # Add remote
   git remote add origin https://github.com/YOUR_USERNAME/pet-clinic.git
   
   # Push
   git push -u origin main
   ```

---

### Step 4: Configure Jenkins (10 minutes)

1. **Access Jenkins**
   - Open: `http://<EC2-PUBLIC-IP>:8080`
   - Enter password from Step 2
   - Click "Install suggested plugins"
   - Create admin user
   - Save and finish

2. **Install Docker Plugin**
   - Manage Jenkins → Manage Plugins → Available
   - Search: "Docker Pipeline"
   - Install without restart

3. **Add GitHub Credentials**
   - Manage Jenkins → Manage Credentials → Global → Add Credentials
   ```
   Kind: Username with password
   Username: your-github-username
   Password: your-github-token
   ID: github-credentials
   ```
   
   **Create GitHub Token:**
   - GitHub → Settings → Developer settings → Personal access tokens
   - Generate new token (classic)
   - Select: `repo`, `admin:repo_hook`
   - Copy token

4. **Create Pipeline**
   - New Item → Name: `pet-clinic-pipeline`
   - Type: Pipeline → OK
   
   **Configuration:**
   ```
   General:
   ☑ GitHub project
   URL: https://github.com/YOUR_USERNAME/pet-clinic
   
   Build Triggers:
   ☑ GitHub hook trigger for GITScm polling
   
   Pipeline:
   Definition: Pipeline script from SCM
   SCM: Git
   Repository URL: https://github.com/YOUR_USERNAME/pet-clinic.git
   Credentials: github-credentials
   Branch: */main
   Script Path: Jenkinsfile
   ```
   
   - Click "Save"

---

### Step 5: Build & Deploy (5 minutes)

1. **Trigger First Build**
   - Click "Build Now"
   - Watch Console Output
   
2. **Wait for Build to Complete**
   ```
   Expected stages:
   ✓ Checkout
   ✓ Build
   ✓ Docker Build
   ✓ Deploy to Staging
   ✓ Health Check
   ```

3. **Verify Deployment**
   ```bash
   # On EC2, check container
   docker ps
   
   # Check logs
   docker logs pet-clinic-staging
   
   # Test health
   curl http://localhost:8081/pet-clinic/actuator/health
   ```

4. **Access Application**
   - Open browser: `http://<EC2-PUBLIC-IP>:8081/pet-clinic/`
   - Test all features!

---

## ✅ Verification Checklist

Before taking screenshots:

- [ ] EC2 instance is running
- [ ] Can SSH into EC2
- [ ] Jenkins is accessible at port 8080
- [ ] Application is accessible at port 8081
- [ ] Can view owners, pets, consultations
- [ ] Can add new owner
- [ ] Can register new pet
- [ ] Can create new consultation

---

## 📸 Required Screenshots

Take these screenshots for submission:

1. **AWS EC2 Dashboard**
   - Show instance running with public IP

2. **EC2 Security Group**
   - Show inbound rules (22, 80, 8080, 8081)

3. **SSH Connection**
   - Terminal showing: `ubuntu@ip-xxx`

4. **Software Versions**
   ```bash
   java -version && docker --version && git --version
   ```

5. **Jenkins Dashboard**
   - Main page showing pet-clinic-pipeline

6. **Jenkins Pipeline Success**
   - Show build #1 with green checkmarks

7. **Docker Containers**
   ```bash
   docker ps
   ```

8. **Application Homepage**
   - Browser showing the pet clinic homepage

9. **Owners List**
   - Show list of owners

10. **Add New Owner Form**
    - Show the form and success message

11. **Pets List**
    - Show list of pets

12. **Console Output**
    - Jenkins build console showing success

---

## 🎯 Submission Package

### 1. Source Code
```bash
cd /Users/mekakarl/pet-clinic
zip -r pet-clinic-source-code.zip . \
  -x "target/*" \
  -x "*.class" \
  -x ".git/*" \
  -x "node_modules/*" \
  -x ".DS_Store"
```

### 2. Database Scripts
Already included:
- `src/main/resources/data.sql` (H2 initialization)
- `scripts/init-mysql.sql` (MySQL initialization)
- `scripts/init.sql` (Docker MySQL initialization)

### 3. Screenshots
Create a folder with all 12 screenshots named:
```
screenshots/
  01-ec2-instance-running.png
  02-security-group-rules.png
  03-ssh-connection.png
  04-software-versions.png
  05-jenkins-dashboard.png
  06-jenkins-build-success.png
  07-docker-containers.png
  08-application-homepage.png
  09-owners-list.png
  10-add-owner-success.png
  11-pets-list.png
  12-jenkins-console-output.png
```

### 4. Documentation
Include:
- `README.md`
- `AWS_DEPLOYMENT_GUIDE.md`
- `DATABASE_STATUS_REPORT.md`
- This `QUICK_START_AWS.md`

---

## 🔧 Troubleshooting

### Cannot connect to EC2
```bash
# Check security group allows your IP
# Verify .pem permissions
chmod 400 your-key.pem
```

### Jenkins not accessible
```bash
# Check Jenkins status
sudo systemctl status jenkins

# Restart if needed
sudo systemctl restart jenkins
```

### Docker permission denied
```bash
# Add to docker group
sudo usermod -aG docker $USER
sudo usermod -aG docker jenkins

# MUST logout and login again!
exit
# Then reconnect via SSH
```

### Application won't start
```bash
# Check logs
docker logs pet-clinic-staging

# Rebuild if needed
cd ~/pet-clinic
git pull
mvn clean package -DskipTests
docker build -t pet-clinic:latest .
```

### Port already in use
```bash
# Stop old containers
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)
```

---

## 🎓 Learning Outcomes

After completing this deployment, you will have:

✅ Created and configured AWS EC2 instance  
✅ Installed JDK, Docker, Jenkins, Git, Maven  
✅ Built Spring Boot application with Maven  
✅ Created Docker image  
✅ Set up Jenkins CI/CD pipeline  
✅ Deployed containerized application  
✅ Configured GitHub webhooks  
✅ Implemented health checks  
✅ Monitored application logs  

---

## 📞 Support

If you encounter issues:

1. Check the detailed `AWS_DEPLOYMENT_GUIDE.md`
2. Review `DATABASE_STATUS_REPORT.md` for database info
3. Check Jenkins console output for errors
4. Review Docker container logs

---

## 🎉 Success!

Once everything is working:

1. Test all CRUD operations
2. Take all required screenshots
3. Prepare submission package
4. Submit before deadline

**Your application is now running in the cloud! 🚀**

---

**Deployment Checklist:**

- [ ] EC2 instance created and running
- [ ] Software installed (JDK, Docker, Jenkins, Git, Maven)
- [ ] Code pushed to GitHub
- [ ] Jenkins configured and pipeline created
- [ ] Application built and dockerized
- [ ] Container deployed and running
- [ ] Application accessible from browser
- [ ] All features tested and working
- [ ] Screenshots captured
- [ ] Submission package prepared

---

*Good luck with your deployment!*

