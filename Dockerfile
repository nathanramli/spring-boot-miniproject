FROM openjdk:19-jdk-alpine3.15@sha256:feb0da897f6da086b1d5e01ba6a6872bdb08aeaf3416df6b1e955db481239537
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]