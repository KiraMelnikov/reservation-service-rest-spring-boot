# Stage 1: Build the application
FROM eclipse-temurin:26-jdk AS build
LABEL authors="kirill"
WORKDIR /app

# Copy Maven wrapper and pom first to cache dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Stage 2: Run the application
FROM eclipse-temurin:26-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
