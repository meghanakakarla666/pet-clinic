# Pet Clinic Management System - Project Documentation

**Project By:** Meghana Kakarla  
**Date:** December 23, 2025  
**Institution:** Integration and Deployment Phase-End Project

---

## 📋 Table of Contents

1. [Project Overview](#project-overview)
2. [Problem Statement](#problem-statement)
3. [Technologies Used](#technologies-used)
4. [System Architecture](#system-architecture)
5. [Features Implemented](#features-implemented)
6. [Database Migration](#database-migration)
7. [CI/CD Pipeline](#cicd-pipeline)
8. [Containerization](#containerization)
9. [Local Setup Guide](#local-setup-guide)
10. [Testing Instructions](#testing-instructions)
11. [Challenges Faced](#challenges-faced)
12. [Future Enhancements](#future-enhancements)
13. [Screenshots](#screenshots)
14. [Conclusion](#conclusion)

---

## 1. Project Overview

The Pet Clinic Management System is a comprehensive web application designed to help veterinary clinics manage their operations efficiently. The system enables clinic staff to:

- **Manage Pet Owners**: Add, view, edit, and delete pet owner information
- **Manage Pets**: Register pets with their details, associate them with owners
- **Manage Consultations**: Record veterinary consultations, treatments, and fees
- **Track Medical History**: View complete medical history for each pet
- **Generate Reports**: View statistics and revenue information

### Real-World Scenario

Dr. Shawn runs a pet clinic and needs to record visits and other details associated with pets and their owners visiting his clinic. This software solution, developed by Bella Solutions, is hosted on an AWS EC2 instance to provide online access from anywhere using CI/CD Pipeline and Docker containerization.

---

## 2. Problem Statement

**Objective:** To build an infrastructure to host software on an AWS EC2 instance for a clinic to have online data access for pets, their owners, and consultation fees.

**Requirements:**
1. Migrate database from H2 (in-memory) to MySQL (persistent)
2. Build CI/CD Pipeline using Jenkins
3. Containerize the application using Docker
4. Deploy on AWS EC2 instance
5. Implement CRUD operations for all entities
6. Ensure data persistence and reliability

---

## 3. Technologies Used

### Backend Technologies
- **Spring Boot 2.7.14** - Main application framework
- **Java 11** - Programming language
- **Spring Data JPA** - ORM for database operations
- **Hibernate** - JPA implementation
- **Maven 3.8+** - Build and dependency management

### Frontend Technologies
- **Thymeleaf** - Server-side template engine
- **HTML5 & CSS3** - Markup and styling
- **JavaScript** - Client-side interactivity

### Database
- **MySQL 8.0** - Production database
- **HikariCP** - Connection pooling

### DevOps & Deployment
- **Docker** - Containerization platform
- **Docker Compose** - Multi-container orchestration
- **Jenkins** - CI/CD automation server
- **Git & GitHub** - Version control

### Cloud Platform
- **AWS EC2** - Cloud hosting (Ubuntu 22.04 LTS)

---

## 4. System Architecture

### Application Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT BROWSER                        │
│                     (Web Interface)                          │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/HTTPS
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                  SPRING BOOT APPLICATION                     │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐            │
│  │ Controller │  │  Service   │  │ Repository │            │
│  │   Layer    │─▶│   Layer    │─▶│   Layer    │            │
│  └────────────┘  └────────────┘  └────────────┘            │
│         │                                │                   │
│         │ Thymeleaf Views               │ JPA/Hibernate     │
│         ▼                                ▼                   │
│  ┌────────────┐              ┌────────────────────┐         │
│  │   HTML     │              │   Entity Classes   │         │
│  │ Templates  │              │ (Owner/Pet/Consult)│         │
│  └────────────┘              └────────────────────┘         │
└─────────────────────────────────────┬───────────────────────┘
                                      │ JDBC
                                      ▼
                        ┌──────────────────────────┐
                        │    MySQL Database        │
                        │   (Port 3306)            │
                        └──────────────────────────┘
```

### Deployment Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                         GITHUB REPOSITORY                    │
│                    (Source Code Storage)                     │
└──────────────────────────┬──────────────────────────────────┘
                           │ Git Push/Poll
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                      JENKINS SERVER                          │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │ Checkout │─▶│  Build   │─▶│  Docker  │─▶│  Deploy  │   │
│  │  Stage   │  │  (Maven) │  │  Build   │  │  Stage   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└──────────────────────────┬──────────────────────────────────┘
                           │ Docker Run
                           ▼
┌─────────────────────────────────────────────────────────────┐
│                   DOCKER CONTAINER                           │
│  ┌────────────────────────────────────────────────────┐     │
│  │       Pet Clinic Application (Port 8081)           │     │
│  │                                                      │     │
│  │  - Spring Boot Application                          │     │
│  │  - Thymeleaf Views                                  │     │
│  │  - REST APIs                                        │     │
│  └────────────────────────────────────────────────────┘     │
└──────────────────────────┬──────────────────────────────────┘
                           │ MySQL Connection
                           ▼
                ┌──────────────────────────┐
                │   MySQL Server           │
                │   (host.docker.internal) │
                └──────────────────────────┘
```

---

## 5. Features Implemented

### 5.1 Owner Management
- **Add New Owners**: Register pet owners with contact information
- **View Owner Details**: See complete owner profile with their pets
- **Edit Owner Information**: Update owner details
- **Delete Owners**: Remove owners from the system (with confirmation)
- **Search Owners**: Find owners by name, city, or phone

**Fields:**
- First Name & Last Name
- Address & City
- Phone Number

### 5.2 Pet Management
- **Register Pets**: Add new pets with complete details
- **View Pet Profiles**: See pet information and medical history
- **Edit Pet Information**: Update pet details
- **Delete Pets**: Remove pets from system (with confirmation)
- **Associate with Owners**: Link pets to their owners

**Fields:**
- Pet Name
- Type (Dog, Cat, Bird, etc.)
- Breed
- Birth Date
- Owner (Foreign Key)

### 5.3 Consultation Management
- **Record Consultations**: Log veterinary visits
- **View Consultation History**: Complete medical records
- **Delete Consultations**: Remove consultation records (with confirmation)
- **Track Fees**: Monitor consultation costs
- **Filter by Date/Pet**: Search consultation records

**Fields:**
- Consultation Date
- Description (Symptoms/Diagnosis)
- Treatment Prescribed
- Consultation Fee
- Pet (Foreign Key)

### 5.4 Dashboard Features
- **Statistics Overview**: Total owners, pets, consultations
- **Recent Activity**: Latest additions to the system
- **Quick Actions**: Fast access to common operations
- **Revenue Tracking**: Total consultation fees

### 5.5 Safety Features
- **Confirmation Dialogs**: JavaScript confirmation before delete operations
- **Flash Messages**: Success/error notifications after operations
- **Data Validation**: Form validation for all inputs
- **Error Handling**: Graceful error management

---

## 6. Database Migration

### 6.1 Initial State (H2 Database)
- In-memory database
- Data lost on application restart
- Limited production capabilities
- Good for development/testing only

### 6.2 Migration to MySQL

#### Changes Made:

**1. pom.xml**
```xml
<!-- Added MySQL Connector -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.33</version>
    <scope>runtime</scope>
</dependency>
```

**2. application.properties**
```properties
# MySQL Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/petclinicdb
spring.datasource.username=petclinic
spring.datasource.password=petclinic123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
```

**3. Database Schema**
- Created `petclinicdb` database
- Tables: `owners`, `pets`, `consultations`
- Foreign key relationships established
- Indexes added for performance

**4. Data Initialization**
- Created `data.sql` for initial data
- Sample owners, pets, and consultations
- SQL compatible with MySQL syntax

### 6.3 Benefits of MySQL
✅ **Persistent Storage**: Data survives application restarts  
✅ **Production Ready**: Reliable for real-world use  
✅ **Scalability**: Handles large datasets efficiently  
✅ **ACID Compliance**: Data integrity guaranteed  
✅ **Backup & Recovery**: Easy data backup capabilities  

---

## 7. CI/CD Pipeline

### 7.1 Jenkins Pipeline Configuration

**Jenkinsfile Stages:**

1. **Checkout Stage**
   - Pulls latest code from GitHub
   - Ensures working with latest version

2. **Build Stage**
   - Compiles Java code using Maven
   - Runs `mvn clean package`
   - Creates JAR file (43MB)

3. **Test Stage**
   - Executes unit tests
   - Validates code quality
   - `mvn test`

4. **Docker Build Stage**
   - Creates Docker image
   - Tags with build number
   - Example: `pet-clinic:10`

5. **Stop Previous Container**
   - Gracefully stops old container
   - Removes old container
   - Prevents port conflicts

6. **Deploy Application**
   - Starts new Docker container
   - Exposes port 8081
   - Connects to MySQL
   - Sets environment variables

7. **Health Check**
   - Waits for application startup
   - Checks `/actuator/health` endpoint
   - Verifies successful deployment

### 7.2 Jenkins Configuration

**Environment Variables:**
```groovy
DOCKER_IMAGE = 'pet-clinic'
DOCKER_TAG = "${BUILD_NUMBER}"
APP_PORT = '8081'
MYSQL_HOST = 'host.docker.internal'
MYSQL_DATABASE = 'petclinicdb'
MYSQL_USER = 'petclinic'
MYSQL_PASSWORD = 'petclinic123'
PATH = "/opt/homebrew/bin:/usr/local/bin:${env.PATH}"
```

**Build Triggers:**
- Poll SCM: `H/5 * * * *` (Every 5 minutes)
- GitHub webhook integration
- Manual build trigger

### 7.3 Post-Build Actions
- Email notifications on failure
- Container logs on error
- Build artifacts retention

---

## 8. Containerization

### 8.1 Dockerfile

```dockerfile
FROM eclipse-temurin:11-jre
WORKDIR /app

# Copy application JAR
COPY target/pet-clinic-1.0.0.jar app.jar

# Expose application port
EXPOSE 8081

# Health check configuration
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:8081/pet-clinic/actuator/health || exit 1

# JVM optimization for containers
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
```

### 8.2 Docker Compose Configuration

```yaml
version: '3.8'
services:
  pet-clinic:
    build: .
    ports:
      - "8081:8081"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/petclinicdb
      - SPRING_DATASOURCE_USERNAME=petclinic
      - SPRING_DATASOURCE_PASSWORD=petclinic123
    depends_on:
      - mysql
    networks:
      - petclinic-network

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_DATABASE=petclinicdb
      - MYSQL_USER=petclinic
      - MYSQL_PASSWORD=petclinic123
      - MYSQL_ROOT_PASSWORD=rootpassword
    volumes:
      - mysql_data:/var/lib/mysql
    ports:
      - "3306:3306"
    networks:
      - petclinic-network

volumes:
  mysql_data:

networks:
  petclinic-network:
    driver: bridge
```

### 8.3 Docker Benefits

✅ **Consistency**: Same environment everywhere (dev/staging/prod)  
✅ **Portability**: Run anywhere Docker is installed  
✅ **Isolation**: Application isolated from host system  
✅ **Resource Efficiency**: Lightweight compared to VMs  
✅ **Quick Deployment**: Fast container startup (~30 seconds)  
✅ **Version Control**: Image versioning with tags  

---

## 9. Local Setup Guide

### 9.1 Prerequisites
```bash
# Check Java version
java -version  # Should be Java 11

# Check Maven version
mvn -version   # Should be 3.6+

# Check Docker version
docker --version

# Check MySQL
mysql --version
```

### 9.2 Database Setup
```bash
# Start MySQL
brew services start mysql

# Create database
mysql -u root -p
CREATE DATABASE petclinicdb;
CREATE USER 'petclinic'@'localhost' IDENTIFIED BY 'petclinic123';
GRANT ALL PRIVILEGES ON petclinicdb.* TO 'petclinic'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

### 9.3 Application Startup

**Option 1: Direct JAR Execution**
```bash
cd pet-clinic
mvn clean package -DskipTests
java -jar target/pet-clinic-1.0.0.jar
```

**Option 2: Docker**
```bash
# Build image
docker build -t pet-clinic:latest .

# Run container
docker run -d \
  --name pet-clinic \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/petclinicdb \
  -e SPRING_DATASOURCE_USERNAME=petclinic \
  -e SPRING_DATASOURCE_PASSWORD=petclinic123 \
  pet-clinic:latest
```

**Option 3: Docker Compose**
```bash
docker-compose up -d
```

### 9.4 Jenkins Setup
```bash
# Install Jenkins
brew install jenkins-lts

# Start Jenkins
brew services start jenkins-lts

# Access Jenkins
open http://localhost:8080

# Initial password
cat ~/.jenkins/secrets/initialAdminPassword
```

### 9.5 Access URLs

| Service | URL | Purpose |
|---------|-----|---------|
| Application | http://localhost:8081/pet-clinic/ | Main application |
| Jenkins | http://localhost:8080 | CI/CD dashboard |
| MySQL | localhost:3306 | Database server |
| Health Check | http://localhost:8081/pet-clinic/actuator/health | App status |

---

## 10. Testing Instructions

### 10.1 Manual Testing

**Test Owner Management:**
1. Go to: http://localhost:8081/pet-clinic/owners
2. Click "Add New Owner"
3. Fill form: John Smith, 123 Main St, New York, 555-1234
4. Submit and verify success message
5. Click "View" to see details
6. Click "Edit" to modify information
7. Click "Delete" and confirm to remove

**Test Pet Management:**
1. Go to: http://localhost:8081/pet-clinic/pets
2. Click "Add New Pet"
3. Fill form: Name=Max, Type=Dog, Breed=Labrador
4. Select owner from dropdown
5. Submit and verify success message
6. Click "View" to see pet profile
7. Click "Edit" to modify
8. Click "Delete" and confirm

**Test Consultations:**
1. Go to: http://localhost:8081/pet-clinic/consultations
2. Click "New Consultation"
3. Select pet from dropdown
4. Enter description: "Annual checkup"
5. Enter fee: 100
6. Submit and verify
7. View consultation details
8. Delete consultation

### 10.2 Testing Checklist

- [ ] All pages load without errors
- [ ] Dashboard shows correct statistics
- [ ] Owner CRUD operations work
- [ ] Pet CRUD operations work
- [ ] Consultation CRUD operations work
- [ ] Delete confirmations appear
- [ ] Success messages display correctly
- [ ] Error handling works properly
- [ ] Navigation menu functions
- [ ] Forms validate input
- [ ] Database persists data
- [ ] Application restarts successfully

### 10.3 Jenkins Build Testing

1. Make code change in GitHub
2. Push to repository
3. Wait ~5 minutes for Jenkins poll
4. Verify build starts automatically
5. Check build console output
6. Verify new Docker image created
7. Check container restart
8. Test application functionality
9. Verify data persistence

---

## 11. Challenges Faced

### 11.1 Maven Repository Issue
**Problem:** Maven couldn't resolve Spring Boot parent POM due to Splunk internal repository configuration.

**Solution:** 
- Temporarily disabled custom Maven settings
- Used Maven Central repository
- Built JAR successfully
- Restored original settings

### 11.2 Docker Networking
**Problem:** Container couldn't connect to MySQL on host using `localhost`.

**Solution:** 
- Changed MySQL host from `localhost` to `host.docker.internal`
- Docker's special DNS name for host machine
- Updated Jenkinsfile environment variable

### 11.3 Service Method Names
**Problem:** Compilation errors - `delete()` method not found in services.

**Solution:**
- Services use `deleteById(Long id)` method
- Updated WebController to use correct method name
- Recompiled and redeployed

### 11.4 Shell Parsing in Jenkins
**Problem:** Docker run command failed due to `&` characters in MySQL URL.

**Solution:**
- Enclosed entire URL in single quotes
- Prevented shell from parsing special characters
- Command executed successfully

### 11.5 Port Conflicts
**Problem:** Tomcat and Jenkins both trying to use port 8080.

**Solution:**
- Stopped Tomcat service
- Restarted Jenkins
- Confirmed Jenkins accessible on port 8080

### 11.6 Docker Image Deprecation
**Problem:** `openjdk:11-jre-slim` image not found.

**Solution:**
- Updated to `eclipse-temurin:11-jre`
- Official replacement for OpenJDK
- Rebuilt Docker image

---

## 12. Future Enhancements

### 12.1 Functional Enhancements
- 📧 Email notifications for appointment reminders
- 📊 Advanced reporting and analytics dashboard
- 📱 Mobile-responsive design improvements
- 🔍 Advanced search and filtering capabilities
- 📅 Appointment scheduling system
- 💊 Prescription and medication tracking
- 🏥 Integration with external lab systems
- 👨‍⚕️ Veterinarian management module

### 12.2 Technical Enhancements
- 🔐 User authentication and authorization (Spring Security)
- 🌐 RESTful API with Swagger documentation
- ⚡ Redis caching for improved performance
- 🔄 Real-time updates with WebSocket
- 📦 Kubernetes orchestration for scaling
- 🔒 HTTPS/SSL certificate configuration
- 🧪 Comprehensive unit and integration tests (JUnit, Mockito)
- 📈 Application monitoring (Prometheus, Grafana)

### 12.3 Deployment Enhancements
- ☁️ AWS RDS for managed database
- 🚀 Blue-green deployment strategy
- 🔄 Automated backup and disaster recovery
- 📊 CloudWatch monitoring and alerts
- 🌍 Multi-region deployment
- 🔧 Infrastructure as Code (Terraform)

---

## 13. Screenshots

### Required Screenshots for Submission:

1. **Application Dashboard**
   - URL: http://localhost:8081/pet-clinic/
   - Shows: Statistics cards, recent activity

2. **Owners Management**
   - URL: http://localhost:8081/pet-clinic/owners
   - Shows: List with View, Edit, Delete buttons

3. **Pets Management**
   - URL: http://localhost:8081/pet-clinic/pets
   - Shows: Pet directory with CRUD operations

4. **Consultations Management**
   - URL: http://localhost:8081/pet-clinic/consultations
   - Shows: Consultation records with delete functionality

5. **Add Owner Form**
   - Shows: Form with validation

6. **Delete Confirmation Dialog**
   - Shows: JavaScript confirmation before delete

7. **Success Message**
   - Shows: Green success banner after operation

8. **Jenkins Pipeline**
   - URL: http://localhost:8080/job/pet-clinic-pipeline/
   - Shows: Build history and status

9. **Jenkins Build Console**
   - Shows: Build stages and logs

10. **Docker Containers**
    - Command: `docker ps`
    - Shows: Running container with image version

11. **Docker Images**
    - Command: `docker images | grep pet-clinic`
    - Shows: Built Docker images

12. **MySQL Database**
    - Shows: Database tables and data

13. **GitHub Repository**
    - URL: https://github.com/meghanakakarla666/pet-clinic
    - Shows: Commits and code structure

---

## 14. Conclusion

### 14.1 Project Summary

The Pet Clinic Management System successfully demonstrates the implementation of a full-stack web application with modern DevOps practices. The project accomplishes all stated objectives:

✅ **Database Migration**: Successfully migrated from H2 to MySQL for persistent storage  
✅ **CI/CD Implementation**: Configured Jenkins pipeline with automated build and deployment  
✅ **Containerization**: Dockerized application for consistent deployment  
✅ **Full CRUD Operations**: Implemented create, read, update, delete for all entities  
✅ **Cloud Ready**: Prepared for AWS EC2 deployment with comprehensive documentation  

### 14.2 Learning Outcomes

Through this project, I gained hands-on experience with:

1. **Spring Boot**: Building RESTful web applications with proper layered architecture
2. **Database Management**: Schema design, migrations, and JPA/Hibernate ORM
3. **DevOps Practices**: CI/CD pipelines, automated testing, and continuous deployment
4. **Docker**: Containerization, image building, and container orchestration
5. **Jenkins**: Pipeline configuration, build automation, and deployment strategies
6. **Cloud Computing**: AWS EC2 setup, deployment, and server management
7. **Version Control**: Git workflows, branching strategies, and collaboration
8. **Problem Solving**: Debugging complex issues across multiple technologies

### 14.3 Industry Relevance

This project demonstrates skills directly applicable to modern software development:

- **Microservices Architecture**: Understanding containerization and service isolation
- **DevOps Culture**: Bridging development and operations through automation
- **Agile Practices**: Iterative development and continuous improvement
- **Cloud-Native Applications**: Building scalable, portable solutions
- **Full-Stack Development**: Frontend, backend, and database integration

### 14.4 Professional Application

The skills and knowledge gained from this project are valuable for:

- Software Development Engineer positions
- DevOps Engineer roles
- Full-Stack Developer opportunities
- Cloud Solutions Architect positions
- System Integration specialist roles

---

## 15. References

### Technologies Documentation
- Spring Boot: https://spring.io/projects/spring-boot
- Docker: https://docs.docker.com/
- Jenkins: https://www.jenkins.io/doc/
- MySQL: https://dev.mysql.com/doc/
- AWS EC2: https://docs.aws.amazon.com/ec2/

### GitHub Repository
- **Project Repository**: https://github.com/meghanakakarla666/pet-clinic
- **Author**: Meghana Kakarla
- **License**: MIT

---

## 16. Appendix

### A. Project Structure
```
pet-clinic/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/bellasolutions/petclinic/
│   │   │       ├── controller/
│   │   │       │   └── WebController.java
│   │   │       ├── entity/
│   │   │       │   ├── Owner.java
│   │   │       │   ├── Pet.java
│   │   │       │   └── Consultation.java
│   │   │       ├── repository/
│   │   │       │   ├── OwnerRepository.java
│   │   │       │   ├── PetRepository.java
│   │   │       │   └── ConsultationRepository.java
│   │   │       ├── service/
│   │   │       │   ├── OwnerService.java
│   │   │       │   ├── PetService.java
│   │   │       │   └── ConsultationService.java
│   │   │       └── PetClinicApplication.java
│   │   └── resources/
│   │       ├── templates/
│   │       │   ├── index.html
│   │       │   ├── layout.html
│   │       │   ├── owners/
│   │       │   ├── pets/
│   │       │   └── consultations/
│   │       ├── application.properties
│   │       └── data.sql
│   └── test/
├── target/
├── scripts/
│   ├── deploy.sh
│   ├── ec2-setup.sh
│   └── init-mysql.sql
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── pom.xml
├── README.md
└── PROJECT_WRITEUP.md
```

### B. Database Schema

**Owners Table:**
```sql
CREATE TABLE owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    phone VARCHAR(20)
);
```

**Pets Table:**
```sql
CREATE TABLE pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    birth_date DATE,
    owner_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES owners(id)
);
```

**Consultations Table:**
```sql
CREATE TABLE consultations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    consultation_date DATETIME NOT NULL,
    description TEXT,
    treatment TEXT,
    fee DECIMAL(10,2),
    pet_id BIGINT,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);
```

### C. Environment Variables

```bash
# Database Configuration
MYSQL_URL=jdbc:mysql://localhost:3306/petclinicdb
MYSQL_USER=petclinic
MYSQL_PASSWORD=petclinic123

# Application Configuration
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=production

# Docker Configuration
DOCKER_IMAGE=pet-clinic
DOCKER_TAG=latest

# Jenkins Configuration
JENKINS_HOME=/var/jenkins_home
```

### D. Useful Commands

```bash
# Maven Commands
mvn clean                    # Clean build artifacts
mvn compile                  # Compile source code
mvn test                     # Run tests
mvn package                  # Create JAR file
mvn spring-boot:run         # Run application

# Docker Commands
docker build -t pet-clinic .              # Build image
docker run -d -p 8081:8081 pet-clinic    # Run container
docker ps                                 # List containers
docker logs pet-clinic                    # View logs
docker stop pet-clinic                    # Stop container
docker rm pet-clinic                      # Remove container
docker images                             # List images

# MySQL Commands
mysql -u petclinic -p petclinicdb        # Connect to database
SHOW TABLES;                              # List tables
SELECT COUNT(*) FROM owners;              # Count owners

# Git Commands
git status                                # Check status
git add .                                 # Stage changes
git commit -m "message"                   # Commit changes
git push origin main                      # Push to GitHub
```

---

**End of Project Documentation**

---

**Submitted By:** Meghana Kakarla  
**Date:** December 23, 2025  
**Project:** Pet Clinic Management System with CI/CD and Docker  
**Institution:** Integration and Deployment Phase-End Project

