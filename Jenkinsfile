pipeline { 

    environment {
        registryCredential = 'AET-Docker-Credential' // Jenkins Global Credential ID
        tagPrefix = 'dev-v1.0.'
        umsRegistry = "activedgetechnologies/usermangement"
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
                    sh 'mvn clean package'
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
                        // umsDockerImage.push('latest')
                    }
                } 
            }
        }
        

        stage('HOUSE KEEPING...!') {
            steps {
                sh "docker rmi $umsRegistry:$tagPrefix$BUILD_NUMBER"
            }
        } 

    }

}