FROM openjdk:17
ARG JAR_FILE=target/tripDuo-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /app/tripduo.jar
ENTRYPOINT ["java", "-jar", "/app/tripduo.jar"]
