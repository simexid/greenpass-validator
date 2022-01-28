FROM maven:3.8.4-openjdk-11-slim AS build
WORKDIR /tmp
ADD pom.xml /tmp
RUN mvn verify clean --fail-never
COPY . /tmp
RUN mvn -v
RUN mvn clean package -DskipTests

FROM openjdk:11-jdk-slim
ARG JAR_FILE=/tmp/target/*.jar
COPY --from=build ${JAR_FILE} greenpassbe.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/greenpassbe.jar"]