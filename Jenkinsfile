pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                bat 'dotnet build'
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
        stage('Test') {
            steps {
               bat 'make check || exit 0'
               junit '**/target/*.xml'
            }
        }
        stage('Deploy') {
            when {
                expression {
                    currentBuild.result == null || currentBuild.result == 'SUCCESS'
                    }
                }
            steps {
                bat 'make publish'
            }
        }
    }
}