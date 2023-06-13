FROM openjdk:17-alpine
WORKDIR /app
COPY target/enricher-service-1.0.0.jar ./enricher-service-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./enricher-service-1.0.0.jar" ]