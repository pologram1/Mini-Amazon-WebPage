FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY source ./source
RUN mvn -B package
FROM tomcat:10.1-jdk17-temurin
COPY --from=build /app/target/MiniAmazon.war /usr/local/tomcat/webapps/MiniAmazon.war
EXPOSE 8080
