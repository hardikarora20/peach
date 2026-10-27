# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -B dependency:go-offline
COPY src ./src
RUN mvn -q -B package -DskipTests

# ---------- Run stage ----------
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
# Keeps memory under Render's 512 MB free limit
ENTRYPOINT ["sh", "-c", "java -XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k -jar app.jar"]