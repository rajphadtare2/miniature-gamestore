FROM openjdk:17

COPY target/*.jar app.jar

# For remote debugging
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5003"

EXPOSE 5003

ENTRYPOINT ["java","-jar","/app.jar"]

