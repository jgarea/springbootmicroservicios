FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/saludo-0.0.1-SNAPSHOT.jar /app/saludo-service.jar
CMD ["java", "-jar", "saludo-service.jar"]