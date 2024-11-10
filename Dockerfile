# Usa una imagen base de Java
FROM openjdk:21-jdk-slim
  
  # Define el directorio de trabajo
WORKDIR /app

ENV BOT_FILE='z-music-0.0.1-SNAPSHOT.jar'
ENV LAVALINK_FILE='Lavalink-lavasrc-user.jar'

# Instala wget
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Descarga el .jar de Lavalink
RUN wget https://github.com/cesarbramos/z-music-bot/releases/download/0.0.2/${LAVALINK_FILE}

RUN mkdir plugins

RUN wget "https://maven.lavalink.dev/releases/dev/lavalink/youtube/youtube-plugin/1.8.2/youtube-plugin-1.8.2.jar" -P /app/plugins
  
  # Copia los archivos JAR generados en `build/libs/` al contenedor
COPY build/libs/${BOT_FILE} /app/${BOT_FILE}

  # Comando para ejecutar ambos .jar en paralelo si es necesario.
CMD ["sh", "-c", "java -jar /app/${LAVALINK_FILE} & java -jar /app/${BOT_FILE}"]

# Si solo uno debe ejecutarse por defecto, ajusta el comando CMD
# CMD ["java", "-jar", "/app/app1.jar"]