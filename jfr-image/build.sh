#!/bin/bash
#

#rm -rf build
pwd=`pwd`

mkdir -p build

pushd ../gateway-service
../gradlew clean bootJar
cp build/libs/gateway-service.jar ${pwd}/build/
popd
docker build ./ -f Dockerfile.gateway -t gateway-service:1.0



