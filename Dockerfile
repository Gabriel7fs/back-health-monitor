FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/back-health-monitor-1.0.0.jar back-health-monitor-1.0.0.jar
EXPOSE 8080
CMD ["java", "-jar", "back-health-monitor-1.0.0.jar"]