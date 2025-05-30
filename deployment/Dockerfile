# Build stage
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:11-jre-slim
WORKDIR /app
# Copy the specific JAR file (adjust the pattern as needed)
COPY --from=build /app/target/*-SNAPSHOT.jar app.jar
# Or try copying all JARs and renaming the main one
# COPY --from=build /app/target/ ./target/
# RUN cp target/*-SNAPSHOT.jar app.jar
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
