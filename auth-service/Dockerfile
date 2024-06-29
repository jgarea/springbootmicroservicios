FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
CMD ["/app/wait-for-it.sh", "config-server:8098", "--timeout=10", "--", "java", "-jar", "/app/auth-service.jar"]
# CMD ["java", "-jar", "auth-service.jar"]