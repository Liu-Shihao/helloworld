Just A HelloWorld Server.
#手动方式
将Jar包和Dockerfile文件上传到服务器
```shell
docker build -t helloworld:v1.0.1 -f Dockerfile .  #制作镜像
docker images #查看镜像
```
然后将镜像上传到镜像仓库
```shell

docker login --username=liushihaowyt registry.cn-shanghai.aliyuncs.com

docker tag cae29d247a89 registry.cn-shanghai.aliyuncs.com/liushihao_docker_repository/helloworld:v1.0.1
docker push registry.cn-shanghai.aliyuncs.com/liushihao_docker_repository/helloworld:v1.0.1

docker pull registry.cn-shanghai.aliyuncs.com/liushihao_docker_repository/helloworld:v1.0.1

docker run -d -p 8889:8080 helloworld:v1.0.1 # 创建容器

```

# Jenkins + Harbor + K8s 方式

```Groovy
pipeline {
    agent any

    stages {
        stage('拉取Git代码') {
            steps {
                echo '拉取Git代码'
            }
        }

        stage('检测代码质量') {
            steps {
                echo '检测代码质量'
            }
        }

        stage('构建代码') {
            steps {
                echo '构建代码'
            }
        }

        stage('制作自定义镜像并发布Harbor') {
            steps {
                echo '制作自定义镜像并发布Harbor'
            }
        }

        stage('基于Harbor部署工程') {
            steps {
                echo '基于Harbor部署工程'
            }
        }
    }
}
```