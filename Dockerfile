FROM gradle:7.0.2-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean build --no-daemon

FROM bellsoft/liberica-openjre-alpine:11.0.10

ENV ws.endpoint=http://localhost:9080/ws
ENV rs.endpoint=http://localhost:9080
ENV server.port=9081

RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/sb-api.jar /app/app.jar

EXPOSE 9081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]