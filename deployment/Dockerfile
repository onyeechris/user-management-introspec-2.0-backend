# Build stage
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app

# Create a non-root user for security
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copy the JAR file and set proper permissions
COPY --from=build /app/target/usermgt-0.0.1-SNAPSHOT.jar app.jar
RUN chmod +x app.jar && chown appuser:appuser app.jar

# Switch to non-root user
USER appuser

# Use modern JVM options compatible with Java 11
ENTRYPOINT ["java", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Xms512m", \
    "-Xmx512m", \
    "-jar", "app.jar"]



# # Build stage
# FROM maven:3.8-openjdk-11 AS build
# WORKDIR /app
# COPY pom.xml .
# COPY src ./src
# RUN mvn clean package

# # Run stage
# FROM openjdk:11-jre-slim
# WORKDIR /app
# COPY --from=build /app/target/*.jar app.jar
# ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Xmx500m", "-Xms500m", "-XX:MaxPermSize=800m", "-jar", "/app.jar"]
