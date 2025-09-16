FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar app.jar

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5004"

EXPOSE 5004

ENTRYPOINT ["java", "-jar", "app.jar"]