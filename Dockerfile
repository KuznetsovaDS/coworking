FROM openjdk:17-jre-slim

COPY ./app.jar /

ENTRYPOINT ["java", "-jar", "/app.jar"]