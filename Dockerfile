# Etapa de build
FROM gradle:8.2.1-jdk17 AS builder

WORKDIR /app

COPY . .

#Contruir el .jar sin ejecutar test
RUN gradle build -x test

#Etapa de ejecución
FROM arm64v8/eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]