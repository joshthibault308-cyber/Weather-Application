pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
            dir('my-app') {
                bat 'mvn clean package'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
        stage('Test') {
            steps {
            dir('my-app') {
               bat 'mvn test || exit 0'
               }
               junit '**/target/surefire-reports/*.xml'
            }
        }
        stage('Deploy') {
            when {
                expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                    }
                }
            steps {
            dir('my-app') {
                bat 'mvn install'
                }
            }
        }
    }
}