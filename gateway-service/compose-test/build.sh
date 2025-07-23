#!/bin/bash
#


pushd ../
../gradlew clean bootJar

docker build ./ -f compose-test/Dockerfile -t gateway-service:1.0
popd


