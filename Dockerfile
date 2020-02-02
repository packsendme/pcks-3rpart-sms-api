
FROM openjdk:8-jdk-alpine
EXPOSE 9097
COPY /target/packsendme-sms-server-0.0.1-SNAPSHOT.jar packsendme-sms-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/packsendme-sms-server-0.0.1-SNAPSHOT.jar"]