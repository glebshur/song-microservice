FROM openjdk:17-alpine
WORKDIR /app
COPY target/eureka-server-1.0.0.jar ./eureka-server-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./eureka-server-1.0.0.jar" ]