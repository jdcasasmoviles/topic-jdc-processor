FROM eclipse-temurin:21-jre
EXPOSE 8080
COPY "./target/topic-jdc-processor-1.0-SNAPSHOT.jar" "app.jar"
ENTRYPOINT [ "java","-jar","app.jar" ]