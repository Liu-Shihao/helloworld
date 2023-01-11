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