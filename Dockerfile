FROM eclipse-temurin:21-jre
EXPOSE 8084
COPY target/quarkus-app/ /app/
WORKDIR /app
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
