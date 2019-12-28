FROM maven:3.5.2-jdk-8-alpine AS MAVEN_BUILD
ARG JAR_NAME=everhome-0.0.1-SNAPSHOT.jar
COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package -DskipTests=true
FROM hypriot/rpi-java
WORKDIR /app
COPY --from=MAVEN_BUILD /build/target/everhome-0.0.1-SNAPSHOT.jar /app/
ENTRYPOINT ["java","-jar","everhome-0.0.1-SNAPSHOT.jar"]
