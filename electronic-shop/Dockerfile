
# Crear una nueva imagen basada en OpenJDK 17
FROM openjdk:17-jdk-slim

WORKDIR /app

ARG JAR_FILE=electronic-shop/target/electronic-shop-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} app_electronic.jar

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

# Establecer el punto de entrada para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app_electronic.jar"]
