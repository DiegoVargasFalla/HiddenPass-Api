# Etapa de build
FROM gradle:8.5-jdk17 AS builder

USER root
WORKDIR /app

# Copiamos el proyecto completo
COPY . .

# Construir el .jar sin ejecutar los tests
RUN gradle --no-daemon clean build -x test

# Etapa de ejecución (imagen más liviana)
FROM arm64v8/eclipse-temurin:17-jdk

WORKDIR /app

# Copiar solo el .jar generado
COPY --from=builder /app/build/libs/app.jar app.jar

EXPOSE 8080

# Ejecutar la aplicación
CMD ["sh", "-c", "until nc -z db 3306; do sleep 1; done && java -jar app.jar"]