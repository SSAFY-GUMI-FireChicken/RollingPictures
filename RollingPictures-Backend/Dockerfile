FROM openjdk:11
ARG JAR_FILE=api-module/build/libs/*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
