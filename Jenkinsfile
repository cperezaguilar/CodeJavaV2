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
        NEXUS_CREDENTIAL_ID = "nexus-user-credential"
    }
    stages {
        stage("Clone code from GitHub") {
            steps {
                script {
                    git branch: 'main', url: 'https://github.com/MTYTECH/CobroJava';
                }
            }
        }
        stage('Maven Build') {
            postGitHub commitId, 'pending', 'build', 'Build is running'
              try {
                sh  "${mvnHome}/bin/mvn clean package"
                postGitHub commitId, 'success', 'build', 'Build succeeded'
              } catch (error) {
                postGitHub commitId, 'failure', 'build', 'Build failed'
                throw error
              }
        }
        stage("Publish to Nexus Repository Manager") {
            steps {
                script {
                    pom = readMavenPom file: "pom.xml";
                    filesByGlob = findFiles(glob: "target/*.${pom.packaging}");
                    echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
                    artifactPath = filesByGlob[0].path;
                    artifactExists = fileExists artifactPath;
                    if(artifactExists) {
                        echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version}";
                        nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: pom.version,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: "pom.xml",
                                type: "pom"]
                            ]
                        );
                    } else {
                        error "*** File: ${artifactPath}, could not be found";
                    }
                }
            }
        }
        stage('Nexus Lifecycle Analysis') {
          postGitHub commitId, 'pending', 'analysis', 'Nexus Lifecycle Analysis is running'
        
          try {
            def policyEvaluation = nexusPolicyEvaluation iqApplication: 'Cobrojava - Mtytech', iqInstanceId: '2', iqStage: 'build'
        	postGitHub commitId, 'success', 'analysis', 'Nexus Lifecycle Analysis succeeded', "${policyEvaluation.applicationCompositionReportUrl}"
          } catch (error) {
            def policyEvaluation = error.policyEvaluation
            postGitHub commitId, 'failure', 'analysis', 'Nexus Lifecycle Analysis failed', "${policyEvaluation.applicationCompositionReportUrl}"
            throw error
          }
        }
    }
}

def postGitHub(commitId, state, context, description, targetUrl) {
  def payload = JsonOutput.toJson(
    state: state,
    context: context,
    description: description,
    target_url: targetUrl
  )
  sh "curl -H \"Authorization: token ${GITHUBAPITOKEN}\" --request POST --data '${payload}'
  https://api.github.com/repos/${PROJECT}/statuses/${commitId} > /dev/null"
}
