FROM openjdk:17-jdk-slim
LABEL authors="Abriel"
WORKDIR /app
COPY target/subscibutions-0.0.1-SNAPSHOT.jarc subscibutions.jar
CMD ["java","-jar","subscibutions.jar"]