FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/gateway-0.0.1-SNAPSHOT.jar /app/gateway.jar
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh
CMD ["/app/wait-for-it.sh", "config-server:8098", "--timeout=20", "--", "java", "-jar", "/app/gateway.jar"]
#CMD ["java", "-jar", "gateway.jar"]
