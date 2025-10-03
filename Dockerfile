# Use Maven + JDK image for build
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use JDK runtime image
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
# Render provides PORT env; Spring reads server.port=${PORT:9090}
ENTRYPOINT ["java","-jar","app.jar"]
