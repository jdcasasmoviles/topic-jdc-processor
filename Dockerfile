FROM eclipse-temurin:21-jre
EXPOSE 8084
COPY "./target/topic-jdc-processor-1.0-SNAPSHOT-runner.jar" "app.jar"
ENTRYPOINT [ "java","-jar","app.jar" ]
