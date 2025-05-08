FROM openjdk:17-jdk-full
LABEL authors="Abriel"
WORKDIR /app
COPY target/subscriptions-0.0.1-SNAPSHOT.jar subscriptions.jar
CMD ["java","-jar","subscriptions.jar"]