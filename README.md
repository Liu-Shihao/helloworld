
# 一、手动方式
最原始的部署方式。
通过Maven打包，编写Dockerfile文件，将Jar包和Dockerfile文件上传到服务器。然后通过Docker命令将tar包制作成Docker镜像，将镜像push到镜像仓库。然后其他服务器从镜像仓库把镜像拉取下来，通过Docker命令创建容器，运行服务。


```shell
#制作镜像
docker build -t helloworld:v1.0.0 -f Dockerfile .  

#查看镜像
docker images |grep helloworld

#登录Haobor镜像仓库
docker login -u admin -p Harbor12345 192.168.153.131:80

#给镜像打上标签
docker tag helloworld:v1.0.0 192.168.153.131:80/repository/helloworld:v1.0.0

#推送镜像到镜像仓库
docker push 192.168.153.131:80/repository/helloworld:v1.0.0

#从镜像仓库拉去镜像
docker pull 192.168.153.131:80/repository/helloworld:v1.0.0

#创建容器
docker run -d -p 8889:8080 helloworld:v1.0.0 
```


# 二、Jenkins Pipeline + Harbor + Docker 方式
使用Jenkins Pipeline方式部署会比第一种手动使用Docker部署的方式简单很多。
我们需要编写Jenkinsfile文件，来完成我们部署的流程： 从Git仓库拉去代码 --》Maven打包 --》Sonar代码质量检测 --》制作镜像 --》上传镜像仓库 --》推送到目标服务器（执行目标服务器启动脚本deploy.sh）

```groovy
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
                sshPublisher(publishers: [sshPublisherDesc(configName: '131', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: "deploy.sh $harborHost $harborRepo $JOB_NAME $tag $container_port $host_port", execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: '')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
    }
    post {
      always {
        emailext body: '${FILE,path="email.html"}', subject: '【构建通知】：$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'liush99@foxmail.com'
      }
    }
}

```

# 三、Jenkins +K8s方式部署


编写Deployment资源文件,Jenkins拉去资源文件，将资源文件推送到k8s-master节点，并执行k8s命令进行部署，不在需要使用第二种方式编写复杂的shell脚本
可以完全实现CI/CD




配置Docker私服,创建一个K8s Secret
需要给k8s集群的master和worker节点添加docker配置
```shell
cat /etc/docker/daemon.json

{
  "registry-mirrors": ["https://iedolof4.mirror.aliyuncs.com"],
  "insecure-registries": ["http://192.168.153.131:80"]
}

systemctl restart docker
```

```groovy
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
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://gitee.com/L1692312138/jenkins-demo.git']]])
            }
        }

        stage('Maven Build') {
            steps {
                sh '/var/jenkins_home/apache-maven-3.8.6/bin/mvn clean package -DskipTests'
            }
        }

        stage('Push Harbor') {
            steps {
                sh '''cp ./target/*.jar ./deploy/
                cd ./deploy
                docker build -t ${JOB_NAME}:latest .'''

                sh '''docker login -u ${harborUser} -p ${harborPasswd} ${harborHost}
                docker tag ${JOB_NAME}:latest ${harborHost}/${harborRepo}/${JOB_NAME}:latest
                docker push ${harborHost}/${harborRepo}/${JOB_NAME}:latest'''
            }
        }

        stage('Publish Over SSH') {
            steps {
            sshPublisher(publishers: [sshPublisherDesc(configName: 'k8s', transfers: [sshTransfer(cleanRemote: false, excludes: '', execCommand: '', execTimeout: 120000, flatten: false, makeEmptyDirs: false, noDefaultExcludes: false, patternSeparator: '[, ]+', remoteDirectory: '', remoteDirectorySDF: false, removePrefix: '', sourceFiles: 'hello_deploy.yaml')], usePromotionTimestamp: false, useWorkspaceInPromotion: false, verbose: false)])
            }
        }
        stage('Deployment k8s') {
            steps {
            sh '''ssh root@192.168.153.128 kubectl apply -f /usr/local/k8s/hello_deploy.yaml
            ssh root@192.168.153.128 kubectl rollout restart Deployment pipeline -n test'''
            }
        }
    }
    post {
      always {
        emailext body: '${FILE,path="email.html"}', subject: '【构建通知】：$PROJECT_NAME - Build # $BUILD_NUMBER - $BUILD_STATUS!', to: 'liush99@foxmail.com'
      }
    }
}
```


可视化界面Kuboard
```shell
#安装Kuboard
kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3.yaml
kubectl apply -f https://addons.kuboard.cn/kuboard/kuboard-v3-swr.yaml

# 查看启动情况
kubectl get pods -n kuboard
kubectl get svc -A |grep kuboard
# 访问30080端口 ：http://192.168.153.128:30080   初始用户admin  初始密码：Kuboard123
```

