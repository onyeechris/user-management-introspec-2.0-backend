pipeline { 

  environment {
    // registryCredential = 'AET-Docker-Credential' // Jenkins Global Credential ID
    registryCredential = 'digitalOceanRegistryCredential' // Jenkins Global Credential ID
    tagPrefix = 'v'
    // umsRegistry = "activedgetechnologies/usermangement-2-0"
    apiRegistry = "registry.digitalocean.com/activedgetechnologies/usermangement-2-0"
    apiDockerImage = ''
    // umsDockerImage = ''
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
      stage('Build') {
      steps {
        withCredentials([
          string(credentialsId: 'DIGITALOCEAN_ACCESS_TOKEN', variable: 'DIGITALOCEAN_ACCESS_TOKEN'),
        ]) {
          sh "doctl auth init --access-token $DIGITALOCEAN_ACCESS_TOKEN"
        }
      }
    }
        stage('Docker Build Images') {
      steps {
        script {
          sh "docker build -t ${apiRegistry}:${tagPrefix}${BUILD_NUMBER} -f deployment/Dockerfile ."
        }
      }
    }
    // stage('Docker Build API Image') {
    //   steps { 
    //     script {
    //       umsDockerImage = docker.build(umsRegistry + ":$tagPrefix$BUILD_NUMBER", "-f ./deployment/Dockerfile .")
    //     }
    //   }
    // }
        stage('Push Docker Image') {
      steps {
        script {
          sh "docker push ${apiRegistry}:${tagPrefix}${BUILD_NUMBER}"
        }
      }
    }
    // stage('Push API Image') {
    //   steps { 
    //     script { 
    //       docker.withRegistry( '', registryCredential ) { // empty registry '' defaults to dockerhub
    //         umsDockerImage.push()
    //       }
    //     } 
    //   }
    // }
    stage('HOUSE KEEPING...!') {
      steps {
        sh "docker rmi $apiRegistry:$tagPrefix$BUILD_NUMBER"
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
