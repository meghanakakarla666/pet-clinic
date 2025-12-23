# Pet Clinic CI/CD Project Instructions

This is a comprehensive Spring Boot pet clinic management system with complete CI/CD pipeline deployment on AWS EC2.

## Project Requirements
- Spring Boot 2.7+ with Java 11
- Maven build system
- JPA/Hibernate for database operations
- REST API endpoints for pet, owner, and consultation management
- Jenkins CI/CD pipeline configuration
- Docker containerization
- AWS EC2 deployment with automated scripts

## Development Guidelines
- Follow Spring Boot best practices
- Use proper REST API design patterns
- Implement proper error handling and validation
- Include comprehensive testing
- Maintain clean, well-documented code

## Deployment Pipeline
- Jenkins builds from Git repository
- Maven package creates JAR file
- Docker containerizes the application
- Automated deployment to AWS EC2
- Environment-specific configurations

## Database Schema
- Pet entity with owner relationships
- Owner entity with contact information
- Consultation entity with fee tracking
- Proper JPA relationships and constraints
