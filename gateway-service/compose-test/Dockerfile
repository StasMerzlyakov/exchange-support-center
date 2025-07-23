FROM openjdk:21

COPY ./build/libs/gateway-service.jar /gateway.jar

ENV JAVA_OPTS=""

ENTRYPOINT exec java $JAVA_OPTS -jar /gateway.jar
