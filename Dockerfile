# Usa una imagen base de Java
FROM eclipse-temurin:21-jdk-jammy

  # Define el directorio de trabajo
WORKDIR /app

ENV BOT_FILE='z-music-0.0.1-SNAPSHOT.jar'
ENV LAVALINK_FILE='Lavalink-lavasrc-user.jar'

RUN apt update -y && apt install tzdata -y

ENV TZ="America/Bogota"

# Descarga el .jar de Lavalink
RUN wget https://github.com/cesarbramos/z-music-bot/releases/download/0.0.2/${LAVALINK_FILE}

RUN mkdir plugins

  # Copia los archivos JAR generados en `build/libs/` al contenedor
COPY build/libs/${BOT_FILE} /app/${BOT_FILE}

EXPOSE 2333

  # Ejemplo de cómo ejecutar ambos .jar
  # Puedes ejecutar uno de ellos como principal, o ambos si necesitas que se ejecuten en paralelo.

  # Comando para ejecutar ambos .jar en paralelo si es necesario.
  # Usa '&&' si solo quieres ejecutar app2.jar después de que app1.jar termine
CMD ["sh", "-c", "java -jar /app/${LAVALINK_FILE} & java -jar /app/${BOT_FILE}"]

# Si solo uno debe ejecutarse por defecto, ajusta el comando CMD
# CMD ["java", "-jar", "/app/app1.jar"]