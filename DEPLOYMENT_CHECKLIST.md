# ✅ AWS Deployment Checklist

**Use this checklist to ensure you complete all required steps**

---

## 📋 Pre-Deployment

### Setup & Accounts
- [ ] AWS account created and verified
- [ ] GitHub account ready
- [ ] Source code on local machine
- [ ] All documents reviewed

---

## 🚀 Phase 1: AWS EC2 Setup

### Create EC2 Instance
- [ ] Logged into AWS Console
- [ ] Navigated to EC2 Dashboard
- [ ] Clicked "Launch Instance"
- [ ] Named instance: `pet-clinic-server`
- [ ] Selected: Ubuntu Server 22.04 LTS
- [ ] Selected: t2.medium instance type
- [ ] Created/selected key pair (.pem file)
- [ ] **Downloaded and saved .pem file securely**
- [ ] Configured security group with ports:
  - [ ] Port 22 (SSH) - Your IP
  - [ ] Port 80 (HTTP) - 0.0.0.0/0
  - [ ] Port 8080 (Jenkins) - 0.0.0.0/0
  - [ ] Port 8081 (Application) - 0.0.0.0/0
- [ ] Set storage: 20 GB
- [ ] Clicked "Launch Instance"
- [ ] Instance status: Running
- [ ] **Noted Public IP Address:** __________________

### Connect to EC2
- [ ] Changed .pem file permissions: `chmod 400 your-key.pem`
- [ ] Successfully connected via SSH
- [ ] Verified connection: `whoami` shows `ubuntu`

---

## 🛠️ Phase 2: Software Installation

### System Update
- [ ] Ran: `sudo apt update`
- [ ] Ran: `sudo apt upgrade -y`

### Install JDK 11
- [ ] Installed: `sudo apt install openjdk-11-jdk -y`
- [ ] Verified: `java -version` shows Java 11
- [ ] Set JAVA_HOME in ~/.bashrc
- [ ] Reloaded: `source ~/.bashrc`

### Install Docker
- [ ] Added Docker repository
- [ ] Installed Docker CE
- [ ] Added user to docker group: `sudo usermod -aG docker $USER`
- [ ] Added jenkins to docker group: `sudo usermod -aG docker jenkins`
- [ ] Started Docker: `sudo systemctl start docker`
- [ ] Enabled Docker: `sudo systemctl enable docker`
- [ ] **Logged out and logged back in**
- [ ] Verified: `docker ps` works without sudo

### Install Jenkins
- [ ] Added Jenkins repository key
- [ ] Added Jenkins apt repository
- [ ] Installed Jenkins
- [ ] Started Jenkins: `sudo systemctl start jenkins`
- [ ] Enabled Jenkins: `sudo systemctl enable jenkins`
- [ ] Verified: `sudo systemctl status jenkins` shows active
- [ ] **Got initial password:** `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`
- [ ] **Saved password:** __________________

### Install Git & Maven
- [ ] Installed Git: `sudo apt install git -y`
- [ ] Verified: `git --version`
- [ ] Installed Maven: `sudo apt install maven -y`
- [ ] Verified: `mvn -version`

---

## 📦 Phase 3: GitHub Setup

### Create Repository
- [ ] Logged into GitHub
- [ ] Created new repository: `pet-clinic`
- [ ] Set to Public
- [ ] **Repository URL:** __________________

### Push Code
On local machine:
- [ ] Navigated to project: `cd /Users/mekakarl/pet-clinic`
- [ ] Initialized git (if needed): `git init`
- [ ] Added all files: `git add .`
- [ ] Committed: `git commit -m "Initial commit for AWS deployment"`
- [ ] Added remote: `git remote add origin <YOUR-REPO-URL>`
- [ ] Pushed: `git push -u origin main`
- [ ] Verified: Code visible on GitHub

---

## 🔧 Phase 4: Jenkins Configuration

### Initial Setup
- [ ] Accessed: `http://<EC2-IP>:8080`
- [ ] Entered initial admin password
- [ ] Clicked "Install suggested plugins"
- [ ] Waited for plugins to install
- [ ] Created admin user
- [ ] Saved Jenkins URL
- [ ] Clicked "Start using Jenkins"

### Install Additional Plugins
- [ ] Went to: Manage Jenkins → Manage Plugins → Available
- [ ] Searched and installed:
  - [ ] Docker Plugin
  - [ ] Docker Pipeline Plugin
  - [ ] GitHub Integration Plugin
- [ ] Clicked "Install without restart"

### Configure Tools
- [ ] Went to: Manage Jenkins → Global Tool Configuration
- [ ] Configured Maven:
  - [ ] Name: Maven-3.8
  - [ ] Install automatically: ✓
- [ ] Configured JDK:
  - [ ] Name: JDK-11
  - [ ] JAVA_HOME: /usr/lib/jvm/java-11-openjdk-amd64
- [ ] Clicked "Save"

### Add GitHub Credentials
- [ ] Went to: Manage Jenkins → Manage Credentials → Global
- [ ] Clicked "Add Credentials"
- [ ] Created GitHub Personal Access Token:
  - [ ] GitHub → Settings → Developer settings → Personal access tokens
  - [ ] Generated new token (classic)
  - [ ] Selected scopes: `repo`, `admin:repo_hook`
  - [ ] **Saved token:** __________________
- [ ] Added credentials in Jenkins:
  - [ ] Kind: Username with password
  - [ ] Username: GitHub username
  - [ ] Password: GitHub token
  - [ ] ID: github-credentials
  - [ ] Clicked "Create"

### Create Pipeline
- [ ] Clicked "New Item"
- [ ] Name: `pet-clinic-pipeline`
- [ ] Type: Pipeline
- [ ] Clicked "OK"
- [ ] Configured:
  - [ ] General → GitHub project
  - [ ] Project URL: <YOUR-REPO-URL>
  - [ ] Build Triggers → GitHub hook trigger
  - [ ] Pipeline:
    - [ ] Definition: Pipeline script from SCM
    - [ ] SCM: Git
    - [ ] Repository URL: <YOUR-REPO-URL>
    - [ ] Credentials: github-credentials
    - [ ] Branch: */main
    - [ ] Script Path: Jenkinsfile
- [ ] Clicked "Save"

---

## 🏗️ Phase 5: Build & Deploy

### First Build
- [ ] Clicked "Build Now" on pet-clinic-pipeline
- [ ] Watched Console Output
- [ ] Verified stages completed:
  - [ ] Checkout ✓
  - [ ] Build ✓
  - [ ] Docker Build ✓
  - [ ] Deploy to Staging ✓
  - [ ] Health Check ✓
- [ ] Build status: SUCCESS

### Verify Deployment
On EC2:
- [ ] Checked containers: `docker ps`
- [ ] Container `pet-clinic-staging` is running
- [ ] Checked logs: `docker logs pet-clinic-staging`
- [ ] No errors in logs
- [ ] Tested health: `curl http://localhost:8081/pet-clinic/actuator/health`
- [ ] Health check returns: `{"status":"UP"}`

### Access Application
- [ ] Opened browser
- [ ] Navigated to: `http://<EC2-IP>:8081/pet-clinic/`
- [ ] Application loads successfully
- [ ] Homepage displays correctly

---

## 🧪 Phase 6: Feature Testing

### Test All Features
- [ ] Homepage loads with statistics
- [ ] Clicked "Manage Owners" - list displays
- [ ] Clicked "Add New Owner" - form loads
- [ ] Filled form and submitted - owner added successfully
- [ ] Clicked owner name - details page loads
- [ ] Clicked "Manage Pets" - list displays
- [ ] Clicked "Register Pet" - form loads
- [ ] Filled form and submitted - pet added successfully
- [ ] Clicked pet name - details page loads
- [ ] Clicked "Consultations" - list displays
- [ ] Clicked "New Consultation" - form loads
- [ ] Filled form and submitted - consultation added successfully
- [ ] Clicked consultation - details page loads
- [ ] Verified "Recent Owners" section shows data
- [ ] Verified "Recent Pets" section shows data
- [ ] All Quick Action buttons work

---

## 📸 Phase 7: Screenshot Collection

### Required Screenshots
- [ ] **01:** EC2 Dashboard showing instance running
- [ ] **02:** Security Group inbound rules
- [ ] **03:** Terminal showing SSH connection
- [ ] **04:** Software versions (run: `java -version && docker --version && git --version`)
- [ ] **05:** Jenkins Dashboard with pipeline
- [ ] **06:** Jenkins build success (green checkmarks)
- [ ] **07:** Docker containers (run: `docker ps`)
- [ ] **08:** Application homepage in browser
- [ ] **09:** Owners list page
- [ ] **10:** Add owner form with success message
- [ ] **11:** Pets list page
- [ ] **12:** Jenkins console output showing complete build

### Organize Screenshots
- [ ] Created folder: `screenshots/`
- [ ] Renamed files with numbers: 01-12
- [ ] All screenshots clear and readable

---

## 📦 Phase 8: Prepare Submission

### Source Code Package
- [ ] Zipped source code: 
  ```bash
  cd /Users/mekakarl/pet-clinic
  zip -r pet-clinic-source-code.zip . -x "target/*" "*.class" ".git/*"
  ```
- [ ] Verified zip file created
- [ ] File size reasonable (< 50MB)

### Database Scripts
- [ ] Verified `src/main/resources/data.sql` exists
- [ ] Verified `scripts/init-mysql.sql` exists
- [ ] Both files contain sample data

### Documentation
- [ ] README.md included
- [ ] AWS_DEPLOYMENT_GUIDE.md included
- [ ] QUICK_START_AWS.md included
- [ ] DATABASE_STATUS_REPORT.md included
- [ ] PROJECT_SUMMARY.md included
- [ ] DEPLOYMENT_CHECKLIST.md included

### Final Package Structure
```
submission/
├── pet-clinic-source-code.zip
├── database-scripts/
│   ├── data.sql
│   └── init-mysql.sql
├── screenshots/
│   ├── 01-ec2-instance-running.png
│   ├── 02-security-group-rules.png
│   ├── ... (all 12 screenshots)
│   └── 12-jenkins-console-output.png
└── documentation/
    ├── README.md
    ├── AWS_DEPLOYMENT_GUIDE.md
    ├── QUICK_START_AWS.md
    ├── DATABASE_STATUS_REPORT.md
    └── PROJECT_SUMMARY.md
```

- [ ] All files organized
- [ ] Ready for submission

---

## ✅ Final Verification

### Application Status
- [ ] Application running on EC2
- [ ] Accessible from public internet
- [ ] All CRUD operations work
- [ ] No errors in logs
- [ ] Health check passing

### Jenkins Status
- [ ] Pipeline configured correctly
- [ ] Build history shows success
- [ ] GitHub webhook configured (optional)

### Documentation Status
- [ ] All guides complete
- [ ] Screenshots captured
- [ ] Database scripts ready
- [ ] Source code packaged

### Submission Ready
- [ ] All required files prepared
- [ ] Package size verified
- [ ] Ready to upload

---

## 📊 Project Information

**EC2 Public IP:** __________________  
**Application URL:** http://________________:8081/pet-clinic/  
**Jenkins URL:** http://________________:8080  
**GitHub Repository:** __________________  
**Deployment Date:** __________________  
**Build Number:** __________________  

---

## 🎯 Success Criteria

All items below must be ✓ before submission:

- [ ] Application deployed on AWS EC2
- [ ] Jenkins CI/CD pipeline configured and working
- [ ] Docker container running application
- [ ] All features tested and working
- [ ] 12+ screenshots captured
- [ ] Source code zipped
- [ ] Database scripts prepared
- [ ] Documentation complete
- [ ] Submission package organized

---

## 📞 Pre-Submission Checklist

- [ ] Reviewed all documentation
- [ ] Tested application one final time
- [ ] Verified all screenshots are clear
- [ ] Checked zip file integrity
- [ ] Organized submission folder
- [ ] Ready to submit

---

## 🎉 Submission Complete!

**Date Submitted:** __________________  
**Submission Method:** __________________  
**Confirmation Number:** __________________  

---

**Congratulations on completing your AWS deployment! 🚀**

---

*Keep this checklist for your records*

