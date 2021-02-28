FROM maven:3.6-jdk-11 AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean package

FROM openjdk:14
COPY --from=build /workspace/target/*.war cookbook.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","cookbook.war"]