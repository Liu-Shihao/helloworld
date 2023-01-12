
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

# 三、Jenkins +K8s方式部署