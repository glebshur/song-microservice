FROM openjdk:17-alpine
WORKDIR /app
COPY target/songapi-1.0.0.jar ./songapi-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./songapi-1.0.0.jar" ]