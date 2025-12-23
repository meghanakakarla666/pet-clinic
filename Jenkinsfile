pipeline {
    agent any
    
    environment {
        DOCKER_IMAGE = 'pet-clinic'
        DOCKER_TAG = "${BUILD_NUMBER}"
        DOCKER_REGISTRY = 'your-docker-registry' // Update with your registry
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                script {
                    sh 'chmod +x mvnw'
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    sh './mvnw test'
                }
            }
            post {
                always {
                    publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'target/site/jacoco',
                        reportFiles: 'index.html',
                        reportName: 'Coverage Report'
                    ])
                }
            }
        }
        
        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                    sh "docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest"
                }
            }
        }
        
        stage('Docker Push') {
            steps {
                script {
                    // Push to Docker registry (configure credentials in Jenkins)
                    // sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                    // sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:latest"
                    echo "Docker push step - Configure with your registry credentials"
                }
            }
        }
        
        stage('Deploy to Staging') {
            steps {
                script {
                    sh "docker stop pet-clinic-staging || true"
                    sh "docker rm pet-clinic-staging || true"
                    sh "docker run -d --name pet-clinic-staging -p 8081:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    sleep(30) // Wait for application to start
                    sh "curl -f http://localhost:8081/actuator/health || exit 1"
                }
            }
        }
        
        stage('Deploy to Production') {
            when {
                branch 'main'
            }
            steps {
                script {
                    input message: 'Deploy to Production?', ok: 'Deploy'
                    sh "docker stop pet-clinic-prod || true"
                    sh "docker rm pet-clinic-prod || true"
                    sh "docker run -d --name pet-clinic-prod -p 8080:8080 ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            emailext (
                subject: "SUCCESS: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Good news! The build ${env.BUILD_URL} completed successfully.",
                to: "dr.shawn@petclinic.com"
            )
        }
        failure {
            emailext (
                subject: "FAILURE: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
                body: "Bad news! The build ${env.BUILD_URL} failed.",
                to: "dr.shawn@petclinic.com"
            )
        }
    }
}
