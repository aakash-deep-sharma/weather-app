pipeline {

    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven-3.9'
    }

    environment {
        IMAGE_NAME = "aakashdeepsharma/weather-app"
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout Source') {
            steps {
                git branch: 'main',
                    credentialsId: 'github',
                    url: 'https://github.com/aakash-deep-sharma/weather-app.git'
            }
        }

        stage('Verify Tools') {
            steps {

                bat 'java -version'
                bat 'mvn -version'
                bat 'node -v'
                bat 'npm -v'
                bat 'git --version'
                bat 'docker version'
            }
        }

        stage('Build Parent Project') {

            steps {

                dir('com.pub.sapient') {
                    bat 'mvn clean install'
                }
            }

        }
        
        stage('Build FE Project') {

            steps {

                dir('com.pub.sapient.fe') {
                    bat 'mvn clean install -Pnpm'
                    
                }
            }

        }
        
        stage('Build BE Project') {

            steps {

                dir('com.pub.sapient.be') {
                    bat 'mvn clean install -Pdocker'
                    
                }
            }

        }
        
        stage('Read Project Version') {
            steps {
                dir('com.pub.sapient') {
        
                    bat '''
                        @echo off
                        mvn help:evaluate -Dexpression=project.version -q -DforceStdout > version.txt
                    '''
        
                    script {
                        env.PROJECT_VERSION = readFile('version.txt').trim()
                    }
        
                    echo "Project Version = ${env.PROJECT_VERSION}"
                }
             }
        }
        
        stage('Docker Login') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    bat 'docker login -u %DOCKER_USERNAME% -p %DOCKER_PASSWORD%'
                }
            }
        }
        
        stage('Push Docker Image'){
            steps{
                bat 'docker login'
                
                bat "docker push aakashdeepsharma/weather-app:${env.PROJECT_VERSION}"

                bat "docker tag aakashdeepsharma/weather-app:${env.PROJECT_VERSION} aakashdeepsharma/weather-app:latest"
                
                bat "docker push aakashdeepsharma/weather-app:latest"
                
            }
        }
        
        stage('Deploy') {

            steps {

                bat '''
                docker compose down
                docker compose pull
                docker compose up -d
                '''

            }

        }

    }

 post {

        always {

            bat 'docker image prune -f'

        }

        success {

            echo 'Application successfully deployed.'

        }

        failure {

            echo 'Pipeline failed.'

        }

    }

}
