FROM openjdk:17.0.1-jdk-slim
COPY /target/ai-email-writer-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
