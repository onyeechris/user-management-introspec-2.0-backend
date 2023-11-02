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
        checkout([$class: 'GitSCM', branches: [[name: '*/SecOps']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'smartcloud', url: 'https://github.com/activedge-technologies/user-management-introspec-2.0-backend.git']]])
      }
    }

    stage('Sonar Analysis'){
            environment {
                scannerHome = tool 'sonar4.7'
            }

            steps {
                withSonarQubeEnv('sonar'){
                    sh '''${scannerHome}/bin/sonar-scanner -Dsonar.projectKey=user-management-introspec-2.0 \
                   -Dsonar.projectName=user-management-introspec-2.0 \
                   -Dsonar.projectVersion=1.0 \
                   -Dsonar.sources=src/ \
                   -Dsonar.java.binaries=src/test/java/com/activedge/usermgt/ \
                   -Dsonar.junit.reportsPath=target/surefire-reports/ \
                   -Dsonar.jacoco.reportsPath=target/jacoco.exec \
                   -Dsonar.java.checkstyle.reportPaths=target/checkstyle-result.xml'''
                }

            }
        }


    // stage('Dependency Vulnerability Scan') {
    //   steps {
    //     sh "mvn dependency-check:check"
    //     //sh "xvfb-run -a -s '-screen 0 1024x768x24' wkhtmltopdf --print-media-type target/dependency-check-report.html target/dependency-check-report.pdf"
    //     sh "cp ./target/dependency-check-report.xml ./"
    //     sh "ls -la"
    //   }
    //   // post {
    //   //   always {
    //   //     dependencyCheckPublisher pattern: 'target/dependency-check-report.xml'
    //   //   }
    //   // }
    // }
    // stage('Uploading Dependency Check Report To Defectdojo VMS') {
    //   steps {
    //     sh "chmod +x dependencycheck-defectdojoupload.sh"
    //     sh "./dependencycheck-defectdojoupload.sh"
    //     sh "ls -la target"
        
    //   }
    // }
    // stage('Maven Build Application') {
    //   steps { 
    //     script {
    //       sh 'mvn package'
    //     }
    //   }
    // }
    // stage('Docker Build API Image') {
    //   steps { 
    //     script {
    //       umsDockerImage = docker.build(umsRegistry + ":$tagPrefix$BUILD_NUMBER", "-f ./deployment/Dockerfile .")
    //     }
    //   }
    // }
    // stage('Push API Image') {
    //   steps { 
    //     script { 
    //       docker.withRegistry( '', registryCredential ) { // empty registry '' defaults to dockerhub
    //         umsDockerImage.push()
    //       }
    //     } 
    //   }
    // }
    // stage('HOUSE KEEPING...!') {
    //   steps {
    //     sh "docker rmi $umsRegistry:$tagPrefix$BUILD_NUMBER"
    //   }
    // } 
    // stage('Cleanup github repo on Jenkins server') {
    //   steps{
    //     sh 'rm -rf ./*'
    //   }
    // }
    // stage('Trigger ManifestUpdate') {
    //   steps {
    //     sh 'echo "======= Triggering updatemanifestjob ======="'
    //     build job: 'User-management-introspec-2.0-backend-pipeline-k8supdate', parameters: [string(name: 'DOCKERIMAGETAG', value: BUILD_NUMBER)]
    //   }
    // }
  }
}
