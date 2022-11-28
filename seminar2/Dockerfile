FROM openjdk:11

ENV APP_HOME=/usr/app
WORKDIR ${APP_HOME}

EXPOSE 8080

# COPY <src> <dest>
ARG JAR_FILE=./build/libs/seminar2-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

# build하고 나서 실행시킬 때 사용하는 명령어
CMD ["java", "-jar", "app.jar"]
