pipeline {
    agent any
    
    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:${env.PATH}"
        DOCKER_IMAGE = 'pet-clinic'
        DOCKER_TAG = "${BUILD_NUMBER}"
        APP_PORT = '8081'
        MYSQL_HOST = 'host.docker.internal'
        MYSQL_DATABASE = 'petclinicdb'
        MYSQL_USER = 'petclinic'
        MYSQL_PASSWORD = 'petclinic123'
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
                    echo 'Building Pet Clinic Application with Maven...'
                    sh 'mvn clean package -DskipTests -s settings.xml'
                }
            }
        }
        
        stage('Test') {
            steps {
                script {
                    echo 'Running tests...'
                    sh 'mvn test -s settings.xml || true'
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
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
        
        stage('Stop Previous Container') {
            steps {
                script {
                    echo 'Stopping previous container if running...'
                    sh "docker stop ${DOCKER_IMAGE} || true"
                    sh "docker rm ${DOCKER_IMAGE} || true"
                }
            }
        }
        
        stage('Deploy Application') {
            steps {
                script {
                    echo 'Deploying Pet Clinic application...'
                    sh """
                        docker run -d \
                            --name ${DOCKER_IMAGE} \
                            -p ${APP_PORT}:8081 \
                            -e 'SPRING_DATASOURCE_URL=jdbc:mysql://${MYSQL_HOST}:3306/${MYSQL_DATABASE}?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true' \
                            -e SPRING_DATASOURCE_USERNAME=${MYSQL_USER} \
                            -e SPRING_DATASOURCE_PASSWORD=${MYSQL_PASSWORD} \
                            -e SPRING_JPA_HIBERNATE_DDL_AUTO=update \
                            --restart unless-stopped \
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                }
            }
        }
        
        stage('Health Check') {
            steps {
                script {
                    echo 'Waiting for application to start...'
                    sleep(45)
                    echo 'Performing health check...'
                    sh """
                        for i in {1..10}; do
                            if curl -f http://localhost:${APP_PORT}/pet-clinic/ > /dev/null 2>&1; then
                                echo 'Application is healthy!'
                                exit 0
                            fi
                            echo "Waiting for application (attempt \$i/10)..."
                            sleep 5
                        done
                        echo 'Health check failed!'
                        docker logs ${DOCKER_IMAGE}
                        exit 1
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            echo "Access application at: http://YOUR-EC2-IP:${APP_PORT}/pet-clinic/"
        }
        failure {
            echo 'Pipeline failed! Check logs for details.'
            sh 'docker logs ${DOCKER_IMAGE} || true'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}
