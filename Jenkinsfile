pipeline { 

  environment {
    registryCredential = 'AET-Docker-Credential' // Jenkins Global Credential ID
    tagPrefix = 'dev-v'
    umsRegistry = "activedgetechnologies/usermangement-2-0"
    umsDockerImage = ''
  }

  agent any // use available executors
  
  tools {
    maven 'Maven3' // Jenkins configured Maven
    jdk 'Java8' // Jenkins configured JDK
    dockerTool 'LocalDocker' // Jenkins configured Docker. Note: Docker Build Pipeline MUST be installed.
  }

  stages {
    stage('Pull Git Repository from CICD branch') {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: '*/cicd']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'smartcloud', url: 'https://github.com/activedge-technologies/user-management-introspec-2.0-backend.git']]])
      }
    }
    stage('Maven Build Application') {
      steps { 
        script {
          sh 'mvn package'
        }
      }
    }
    stage('Docker Build API Image') {
      steps { 
        script {
          umsDockerImage = docker.build(umsRegistry + ":$tagPrefix$BUILD_NUMBER", "-f ./deployment/Dockerfile .")
        }
      }
    }
    stage('Push API Image') {
      steps { 
        script { 
          docker.withRegistry( '', registryCredential ) { // empty registry '' defaults to dockerhub
            umsDockerImage.push()
          }
        } 
      }
    }
    stage('HOUSE KEEPING...!') {
      steps {
        sh "docker rmi $umsRegistry:$tagPrefix$BUILD_NUMBER"
      }
    } 
    stage('Cleanup github repo on Jenkins server') {
      steps{
        sh 'rm -rf ./*'
      }
    }
    stage('Trigger ManifestUpdate') {
      steps {
        sh 'echo "======= Triggering updatemanifestjob ======="'
        build job: 'User-management-introspec-2.0-backend-pipeline-k8supdate', parameters: [string(name: 'DOCKERIMAGETAG', value: BUILD_NUMBER)]
      }
    }
  }
}