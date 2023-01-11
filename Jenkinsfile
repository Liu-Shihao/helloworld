pipeline {
    agent any
    environment{
        harborHost = '192.168.153.131:80'
        harborRepo = 'helloworld'
        harborUser = 'admin'
        harborPasswd = 'Hardor12345'
    }

    // 存放所有任务的合集
    stages {

        stage('Pull Dode') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '${tag}']], extensions: [], userRemoteConfigs: [[url: 'https://gitee.com/L1692312138/jenkins-demo.git']]])
            }
        }

        stage('Build') {
            steps {
                sh '/var/jenkins_home/apache-maven-3.8.6/bin/mvn clean package -DskipTests'
            }
        }

//         stage('Sonar Scan') {
//             steps {
//                 sh '/var/jenkins_home/sonar-scanner/bin/sonar-scanner -Dsonar.sources=./ -Dsonar.projectname=${JOB_NAME} -Dsonar.projectKey=${JOB_NAME} -Dsonar.java.binaries=target/ -Dsonar.login=7d66af4b39cfe4f52ac0a915d4c9d5c513207098'
//             }
//         }

        stage('Build Images & Push Harbor') {
            steps {
                sh '''cp ./target/*.jar ./deploy/
                cd ./deploy
                docker build -t ${JOB_NAME}:${tag} .'''

                sh '''docker login -u ${harborUser} -p ${harborPasswd} ${harborHost}
                docker tag ${JOB_NAME}:${tag} ${harborHost}/${harborRepo}/${JOB_NAME}:${tag}
                docker push ${harborHost}/${harborRepo}/${JOB_NAME}:${tag}'''
            }
        }

        stage('Pull Images & Run') {
            steps {
                sshPublisher(publishers: [sshPublisherDesc(configName: 'testEnvironment', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "/usr/bin/deploy.sh $harborHost $harborRepo $JOB_NAME $tag $port ", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
    }
}