FROM gradle:latest

COPY ./ /home

WORKDIR /home

EXPOSE 8080

RUN ./gradlew clean build -x test

ENTRYPOINT ./gradlew bootRun -Dspring.profiles.active=release
