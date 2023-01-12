harbor_url=$1
harbor_project_name=$2
project_name=$3
tag=$4
container_port=$5
host_port=$6
imageName=$harbor_url/$harbor_project_name/$project_name:$tag

containerId=`docker ps -a | grep ${project_name} | awk '{print $1}'`
if [ "$containerId" != "" ] ; then
    docker stop $containerId
    docker rm $containerId
    echo "Delete Container Success"
fi

imageId=`docker images | grep ${project_name} | awk '{print $3}'`

if [ "$imageId" != "" ] ; then
    docker rmi -f $imageId
    echo "Delete Image Success"
fi
docker login -u admin -p Harbor12345 $harbor_url

docker pull $imageName

docker run -d -p $host_port:$container_port --name $project_name $imageName

echo "Start Container Success"
echo $project_name