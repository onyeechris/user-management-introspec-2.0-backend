pipeline {
  agent any
  
  stages {
    stage("Clone application") {
      steps {
        checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'francis_github', url: 'https://github.com/activedge-technologies/argocd.git']]])
      }
    }
    stage('Update Payassist-backend Deployment file') {
      steps {
        script {
          catchError(buildResult: 'SUCCESS', stageResult: 'FAILURE') {
            withCredentials([usernamePassword(credentialsId: 'francis_github', passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
              sh "git config user.email francisnwachukwu100@gmail.com"
              sh "git config user.name francis-nwachukwu"
              sh "cat settlement-files/user-mgt-backend.yaml"
              sh "sed -i 's+activedgetechnologies/usermangement:.*+activedgetechnologies/usermangement:dev-v1.0.${DOCKERIMAGETAG}+g' settlement-files/user-mgt-backend.yaml"
              sh "cat settlement-files/user-mgt-backend.yaml"
              sh "git add ."
              sh "git commit -m 'User management backend.yaml docker image update done by Jenkins Job: ${env.BUILD_NUMBER}'"
              sh "git push https://${GIT_USERNAME}:${GIT_PASSWORD}@github.com/activedge-technologies/argocd.git HEAD:main"
            }
          }
        }
      }
    }
  }
}