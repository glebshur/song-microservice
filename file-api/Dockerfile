FROM openjdk:17-alpine
WORKDIR /app
COPY target/file-api-1.0.0-SNAPSHOT.jar ./file-api-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./file-api-1.0.0.jar" ]