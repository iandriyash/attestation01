# === Сборка ===
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -e -DskipTests dependency:go-offline
COPY . .
RUN mvn -q -DskipTests package

# === Запуск ===
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar /app/app.jar
ENV JAVA_TOOL_OPTIONS="-Dfile.encoding=UTF-8"
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
