FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
RUN mvn -q -DskipTests dependency:go-offline
COPY src src
RUN mvn -q -Pprod -DskipTests package

FROM eclipse-temurin:17-jre
ENV JAVA_OPTS="-Xms256m -Xmx512m"
WORKDIR /app
COPY --from=build /workspace/target/*-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar"]

