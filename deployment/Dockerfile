# Build stage
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package
# Debug: List what's in target directory
RUN ls -la target/

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app
# Debug: Copy and list files
COPY --from=build /app/target/*.jar app.jar
RUN ls -la /app/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Xmx500m", "-Xms500m", "-XX:MaxPermSize=800m", "-jar", "/app.jar"]





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
