FROM gradle:8-jdk17 AS builder
WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM amazoncorretto:17
COPY --from=builder /app/build/libs/discodeit-1.2-M8.jar /app.jar

ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8
ENV JVM_OPT=""

EXPOSE 80
ENTRYPOINT ["sh", "-c", "java $JVM_OPT -jar app.jar --spring.application.name=$PROJECT_NAME --app.version=$PROJECT_VERSION"]