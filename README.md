# Pet Clinic Management System

A comprehensive Spring Boot application for managing pet clinic operations with complete CI/CD pipeline deployment on AWS EC2.

## 🏥 Project Overview

This application was developed by Bella Solutions for Dr. Shawn's pet clinic to manage visits, pet information, owner details, and consultation fees. The system provides online access from anywhere through a web-based REST API with automated deployment on AWS cloud infrastructure.

## 🎯 Features

- **Owner Management**: Complete CRUD operations for pet owners
- **Pet Registration**: Manage pet information including breed, type, and medical history
- **Consultation Tracking**: Record and track consultation details and fees
- **RESTful API**: Comprehensive REST endpoints for all operations
- **Database Integration**: MySQL database with persistent storage
- **Health Monitoring**: Built-in health checks and monitoring endpoints
- **Containerized Deployment**: Docker-based deployment with Docker Compose support

## 🛠️ Technology Stack

- **Backend**: Spring Boot 2.7.14, Java 11
- **Database**: MySQL 8.0
- **Build Tool**: Maven 3.8+
- **Containerization**: Docker & Docker Compose
- **CI/CD**: Jenkins Pipeline
- **Cloud Platform**: AWS EC2
- **Testing**: JUnit, Spring Boot Test

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.8+
- MySQL 8.0 or higher
- Docker & Docker Compose (optional)
- AWS Account (for cloud deployment)
- Jenkins (for CI/CD)

## 🚀 Quick Start

### Local Development

1. **Clone the repository**
```bash
git clone <your-repository-url>
cd pet-clinic
```

2. **Setup MySQL Database**
```bash
# Create database
mysql -u root -p
CREATE DATABASE petclinicdb;
CREATE USER 'petclinic'@'localhost' IDENTIFIED BY 'petclinic123';
GRANT ALL PRIVILEGES ON petclinicdb.* TO 'petclinic'@'localhost';
FLUSH PRIVILEGES;
EXIT;
```

Or use Docker to run MySQL:
```bash
docker run -d \
  --name pet-clinic-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=petclinicdb \
  -e MYSQL_USER=petclinic \
  -e MYSQL_PASSWORD=petclinic123 \
  -p 3306:3306 \
  mysql:8.0
```

3. **Build the application**
```bash
mvn clean package
```

4. **Run locally**
```bash
java -jar target/pet-clinic-1.0.0.jar
```

5. **Access the application**
- Application: http://localhost:8081/pet-clinic
- Health Check: http://localhost:8081/pet-clinic/actuator/health

### Docker Deployment

1. **Build Docker image**
```bash
docker build -t pet-clinic:latest .
```

2. **Run with Docker Compose**
```bash
docker-compose up -d
```

## 🏗️ Project Structure

```
pet-clinic/
├── src/
│   ├── main/
│   │   ├── java/com/bellasolutions/petclinic/
│   │   │   ├── PetClinicApplication.java
│   │   │   ├── controller/          # REST Controllers
│   │   │   ├── entity/             # JPA Entities
│   │   │   ├── repository/         # Data Access Layer
│   │   │   └── service/            # Business Logic Layer
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
├── scripts/
│   ├── deploy.sh                   # Deployment script
│   ├── ec2-setup.sh               # EC2 configuration script
│   └── init.sql                   # Database initialization
├── Dockerfile                     # Docker configuration
├── Jenkinsfile                    # CI/CD pipeline configuration
├── docker-compose.yml             # Multi-container setup
└── pom.xml                        # Maven configuration
```

## 🔗 API Endpoints

### Owners
- `GET /api/owners` - Get all owners
- `GET /api/owners/{id}` - Get owner by ID
- `POST /api/owners` - Create new owner
- `PUT /api/owners/{id}` - Update owner
- `DELETE /api/owners/{id}` - Delete owner
- `GET /api/owners/search` - Search owners

### Pets
- `GET /api/pets` - Get all pets
- `GET /api/pets/{id}` - Get pet by ID
- `POST /api/pets` - Register new pet
- `PUT /api/pets/{id}` - Update pet
- `DELETE /api/pets/{id}` - Delete pet
- `GET /api/pets/owner/{ownerId}` - Get pets by owner

### Consultations
- `GET /api/consultations` - Get all consultations
- `GET /api/consultations/{id}` - Get consultation by ID
- `POST /api/consultations` - Create consultation
- `PUT /api/consultations/{id}` - Update consultation
- `DELETE /api/consultations/{id}` - Delete consultation
- `GET /api/consultations/pet/{petId}` - Get consultations by pet
- `GET /api/consultations/reports/fees` - Get fee reports

## 🏭 AWS EC2 Deployment

### Step 1: Launch EC2 Instance

1. Launch Amazon Linux 2 EC2 instance
2. Configure security groups:
   - SSH (22): Your IP
   - HTTP (80): 0.0.0.0/0
   - Custom TCP (8080): 0.0.0.0/0 (Jenkins)
   - Custom TCP (8081): 0.0.0.0/0 (Application)

### Step 2: Setup EC2 Environment

```bash
# Copy and run the setup script
scp -i your-key.pem scripts/ec2-setup.sh ec2-user@your-ec2-ip:~
ssh -i your-key.pem ec2-user@your-ec2-ip
chmod +x ec2-setup.sh
./ec2-setup.sh
```

### Step 3: Configure Jenkins

1. Access Jenkins: `http://your-ec2-ip:8080`
2. Get initial password: `sudo cat /var/lib/jenkins/secrets/initialAdminPassword`
3. Install suggested plugins
4. Create admin user
5. Configure Git repository
6. Create pipeline job using Jenkinsfile

### Step 4: Deploy Application

```bash
# Use the deployment script
./scripts/deploy.sh deploy
```

## 🔧 Configuration

### Database Configuration

The application uses MySQL by default. Configure via environment variables:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/petclinicdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=petclinic
spring.datasource.password=petclinic123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

### Environment Variables

You can override the default database settings using these environment variables:
- `MYSQL_URL`: Database connection URL (default: jdbc:mysql://localhost:3306/petclinicdb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true)
- `MYSQL_USER`: Database username (default: petclinic)
- `MYSQL_PASSWORD`: Database password (default: petclinic123)
- `SPRING_PROFILES_ACTIVE`: Set to `prod` for production
- `JAVA_OPTS`: JVM options (e.g., `-Xms512m -Xmx1024m`)

**Example:**
```bash
export MYSQL_URL=jdbc:mysql://your-server:3306/petclinicdb
export MYSQL_USER=your_username
export MYSQL_PASSWORD=your_password
java -jar target/pet-clinic-1.0.0.jar
```

## 🧪 Testing

**Run tests:**
```bash
mvn test
```

**Run with coverage:**
```bash
mvn test jacoco:report
```

## 📊 Monitoring

- **Health Check**: `/actuator/health`
- **Application Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`

## 🔐 Security

- Input validation using Bean Validation
- SQL injection prevention through JPA
- CORS configuration for cross-origin requests
- Secure database credentials management

## 🚀 CI/CD Pipeline

The Jenkins pipeline includes:

1. **Checkout**: Source code retrieval
2. **Build**: Maven compilation
3. **Test**: Unit and integration tests
4. **Package**: JAR file creation
5. **Docker Build**: Container image creation
6. **Deploy**: Automated deployment to EC2
7. **Health Check**: Application readiness verification

## 📝 Sample Data

The application includes sample data for:
- 5 pet owners
- 9 pets (dogs and cats)
- 15 consultation records

## 🐛 Troubleshooting

### Common Issues

1. **Port conflicts**: Ensure port 8081 is available
2. **Memory issues**: Increase JVM heap size with `-Xmx`
3. **Database connection**: 
   - Verify MySQL service is running: `sudo systemctl status mysql` or `brew services list | grep mysql`
   - Check database credentials in application.properties
   - Ensure the database `petclinicdb` exists
   - Verify network connectivity to MySQL server
4. **Docker permissions**: Add user to docker group
5. **MySQL connection errors**: If you see "Access denied" errors, verify the user has proper privileges

### Logs

```bash
# Application logs
sudo journalctl -u pet-clinic-service -f

# Docker logs
docker logs pet-clinic-app

# Jenkins logs
sudo journalctl -u jenkins -f
```

## 📞 Support

For support and questions:
- Check the logs for error details
- Verify all prerequisites are installed
- Ensure security groups and firewall rules are configured correctly

## 📄 License

This project is developed by Bella Solutions for Dr. Shawn's Pet Clinic.

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

---

**Developed by Bella Solutions** for efficient pet clinic management with modern DevOps practices.
