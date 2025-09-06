FROM gradle:9.0-jdk21-alpine AS builder

WORKDIR /ms-auth

COPY build.gradle settings.gradle main.gradle gradlew gradlew.bat gradle.properties ./
# COPY gradlew ./
COPY gradle/ gradle/

RUN ./gradlew dependencies --no-daemon

COPY applications/ applications/
COPY domain/ domain/
COPY infrastructure/ infrastructure/

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine

COPY --from=builder /ms-auth/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS  -jar app.jar" ]
