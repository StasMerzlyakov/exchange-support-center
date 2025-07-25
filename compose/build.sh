#!/bin/bash
#

rm -rf build
mkdir build


# gateway-servive
pushd ../gateway-service
../gradlew clean bootJar
cp build/libs/gateway-service.jar ../compose/build/
popd
docker build ./ -f Dockerfile.gateway -t gateway-service:1.0



