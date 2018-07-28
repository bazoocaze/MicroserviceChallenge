FROM openjdk:8-jre-alpine
VOLUME /tmp
# ARG JAR_FILE
# COPY ${JAR_FILE} /tmp/app.jar
COPY target/*.jar /tmp/app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=production","-jar","/tmp/app.jar"]
