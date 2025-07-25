#!/bin/bash
#

#rm -rf build
mkdir -p build


# opentelemetry
echo ">>> try to load opentelemetry-javaagent"
if [ ! -f build/opentelemetry-javaagent.jar ]; then
    wget -O build/opentelemetry-javaagent.jar https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar &> /dev/null
    [[ $? -eq 0 ]] && echo "----- opentelemetry-agent loaded [OK]" || echo "----- can't load opentelemetry agent, [ERROR]" && exit 1
else
    echo "----- opentelemetry-javaagent already loaded [OK]"
fi

# gateway-servive
pushd ../gateway-service
../gradlew clean bootJar
cp build/libs/gateway-service.jar ../compose/build/
popd
docker build ./ -f Dockerfile.gateway -t gateway-service:1.0



