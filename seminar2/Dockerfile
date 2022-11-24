FROM gradle:latest

COPY ./ /home

WORKDIR /home

EXPOSE 8080

ENTRYPOINT ./gradlew bootRun -Dspring.profiles.active=release