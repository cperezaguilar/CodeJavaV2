/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    tools {
        maven 'M3'
    }
    stages {
        stage('build') {
            withMaven(maven: 'mvn') {
                sh "mvn clean package"
            }
        }
    }
}
