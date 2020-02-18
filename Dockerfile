FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
VOLUME /tmp/input/
VOLUME /tmp/output/
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=deploy-container","/app.jar"]