FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/tripDuo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} tripDuo.jar
ENTRYPOINT ["java", "-jar", "/tripDuo.jar"]

