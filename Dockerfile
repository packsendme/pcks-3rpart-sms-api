
FROM openjdk:8-jdk-alpine
EXPOSE 9097
COPY /target/pcks-3rpart-sms-api-0.0.1-SNAPSHOT.jar pcks-3rpart-sms-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/pcks-3rpart-sms-api-0.0.1-SNAPSHOT.jar"]
