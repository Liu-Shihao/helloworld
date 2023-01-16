pipeline {
    agent any
    environment{
        harborHost = '192.168.153.131:80'
        harborRepo = 'helloworld'
        harborUser = 'admin'
        harborPasswd = 'Harbor12345'
    }

    // 存放所有任务的合集
    stages {

        stage('Pull Code') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '${tag}']], extensions: [], userRemoteConfigs: [[url: 'https://gitee.com/L1692312138/jenkins-demo.git']]])
            }
        }

//         stage('Sonar Scan') {
//             steps {
//                 sh '/var/jenkins_home/sonar-scanner/bin/sonar-scanner -Dsonar.sources=./ -Dsonar.projectname=${JOB_NAME} -Dsonar.projectKey=${JOB_NAME} -Dsonar.java.binaries=target/ -Dsonar.login=7d66af4b39cfe4f52ac0a915d4c9d5c513207098'
//             }
//         }

        stage('Maven Build') {
            steps {
                sh '/var/jenkins_home/apache-maven-3.8.6/bin/mvn clean package -DskipTests'
            }
        }

        stage('Push Harbor') {
            steps {
                sh '''cp ./target/*.jar ./deploy/
                cd ./deploy
                docker build -t ${JOB_NAME}:${tag} .'''

                sh '''docker login -u ${harborUser} -p ${harborPasswd} ${harborHost}
                docker tag ${JOB_NAME}:${tag} ${harborHost}/${harborRepo}/${JOB_NAME}:${tag}
                docker push ${harborHost}/${harborRepo}/${JOB_NAME}:${tag}'''
            }
        }

        stage('Publish Over SSH') {
            steps {
            sshPublisher(publishers: [sshPublisherDesc(configName: 'k8s', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'pipeline.yaml')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
        stage('Deployment k8s') {
            steps {
            sh 'ssh root@192.168.153.128 kubectl apply -f /usr/local/k8s/pipeline.yaml'
            }
        }
    }
    post {
      always {
        emailext body: '${FILE,path="email.html"}', subject: '【构建通知】：$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'liush99@foxmail.com'
      }
    }
}