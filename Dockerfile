FROM amazoncorretto:25 AS builder

WORKDIR /workspace

RUN yum update -y && \
    yum install -y findutils && \
    rm -rf /var/cache/yum

COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

RUN chmod +x gradlew
RUN ./gradlew clean bootJar --no-daemon

FROM amazoncorretto:25

WORKDIR /app

COPY --from=builder /workspace/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]