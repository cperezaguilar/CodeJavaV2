/* Requires the Docker Pipeline plugin */
pipeline {
    agent any
    tools {
        maven 'M3'
    }
    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "201.168.125.91:8081"
        NEXUS_REPOSITORY = "maven-snapshots"
        NEXUS_CREDENTIAL_ID = "Nexus"
    }
    stages {
        stage("Clone code from GitHub") {
            steps {
                script {
                    git branch: 'feature/release0001', url: 'https://github.com/Ventus-Technology/CobroJava';
                }
            }
        }
        stage('Maven Build') {
            steps {
                sh 'mvn clean package -DskipTests=true'              
            }
        }
    }
}
