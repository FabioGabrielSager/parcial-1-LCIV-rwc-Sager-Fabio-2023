FROM openjdk:17
COPY target/parcial-1-rwc-2023-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]