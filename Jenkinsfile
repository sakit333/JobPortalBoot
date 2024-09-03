pipeline {
    agent any

    environment {
        GIT_REPO_URL = 'https://github.com/sakit333/your-private-repo.git'
        GIT_CREDENTIALS = 'git-credentials-id'
        DOCKER_IMAGE_NAME = 'sakit333/webappproject'
        DOCKER_CREDENTIALS = 'dockerhub-credentials-id'
        MYSQL_CONTAINER_NAME = 'mysql-db'
        SPRING_CONTAINER_NAME = 'spring-app'
        MYSQL_ROOT_PASSWORD = '1234'
        MYSQL_DATABASE = 'jobportal'
        MYSQL_USERNAME = 'root'  
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', credentialsId: "${GIT_CREDENTIALS}", url: "${GIT_REPO_URL}"
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE_NAME} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry('', "${DOCKER_CREDENTIALS}") {
                        sh "docker push ${DOCKER_IMAGE_NAME}"
                    }
                }
            }
        }

        stage('Deploy Application') {
            steps {
                script {
                    // Stop and remove any existing containers
                    sh "docker stop ${MYSQL_CONTAINER_NAME} ${SPRING_CONTAINER_NAME} || true && docker rm -f ${MYSQL_CONTAINER_NAME} ${SPRING_CONTAINER_NAME} || true"
                    sh "docker rmi -f ${DOCKER_IMAGE_NAME} || true"

                    // Run MySQL container
                    sh """
                    docker run -it --name ${MYSQL_CONTAINER_NAME} \
                    -e MYSQL_ROOT_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                    -e MYSQL_DATABASE=${MYSQL_DATABASE} \
                    -d mysql:latest
                    """

                    // Run Spring application container
                    sh """
                    docker run -it --name ${SPRING_CONTAINER_NAME} \
                    --link ${MYSQL_CONTAINER_NAME}:mysql \
                    -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/${MYSQL_DATABASE} \
                    -e SPRING_DATASOURCE_USERNAME=${MYSQL_USERNAME} \    
                    -e SPRING_DATASOURCE_PASSWORD=${MYSQL_ROOT_PASSWORD} \
                    -p 8090:80 \
                    -d ${DOCKER_IMAGE_NAME}
                    """
                }
            }
        }
    }

    post {
        cleanup {
            cleanWs()
        }
    }
}
