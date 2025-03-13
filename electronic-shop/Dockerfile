
# Crear una nueva imagen basada en OpenJDK 17
FROM openjdk:17-jdk

WORKDIR /app

# Exponer el puerto que utilizará la aplicación
EXPOSE 8080

ADD ./target/electronic-shop-0.0.1-SNAPSHOT.jar ./app.jar
CMD java -Djava.security.egd=file:/dev/./urandon -jar app.jar



