FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/*.jar app.jar
COPY wait-for-it.sh .

RUN chmod +x wait-for-it.sh

EXPOSE 8080

ENTRYPOINT ["./wait-for-it.sh", "db", "5432", "--", "java", "-jar", "app.jar"]
