FROM openjdk:17-jdk-slim

LABEL maintainer="BAS nan@gmail.com"

EXPOSE 8081

COPY target/stock-ms.jar stock-ms.jar

ENTRYPOINT ["java", "-jar", "stock-ms.jar"]