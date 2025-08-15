FROM openjdk:17

COPY target/*.jar app.jar

# For remote debugging
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5002"

EXPOSE 5002

ENTRYPOINT ["java","-jar","/app.jar"]

