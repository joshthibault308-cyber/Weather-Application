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
    }
}