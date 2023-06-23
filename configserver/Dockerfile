FROM openjdk:17-alpine
WORKDIR /app
COPY target/configserver-1.0.0.jar ./configserver-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./configserver-1.0.0.jar" ]