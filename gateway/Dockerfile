FROM openjdk:17-alpine
WORKDIR /app
COPY target/gateway-1.0.0.jar ./gateway-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./gateway-1.0.0.jar" ]