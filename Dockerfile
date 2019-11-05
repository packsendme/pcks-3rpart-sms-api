
FROM java:8
EXPOSE 9097
ADD /target/packsendme-sms-server-0.0.1-SNAPSHOT.jar packsendme-sms-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/packsendme-sms-server-0.0.1-SNAPSHOT.jar"]