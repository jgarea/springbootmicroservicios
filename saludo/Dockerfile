FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/saludo-0.0.1-SNAPSHOT.jar /app/saludo-service.jar
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
CMD ["/app/wait-for-it.sh", "config-server:8098", "--timeout=15", "--", "java", "-jar", "/app/saludo-service.jar"]