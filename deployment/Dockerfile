FROM ppc64le/openjdk:8u201-jdk-alpine3.9
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-XX:+PrintFlagsFinal", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Xmx600m", "-Xms600m", "-XX:MaxPermSize=1g", "-jar", "/app.jar"]
