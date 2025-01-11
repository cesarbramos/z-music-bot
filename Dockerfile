# Usa una imagen base de Java
FROM eclipse-temurin:21-jdk-jammy

RUN apt update \
  && apt install tzdata -y \
  && apt clean

ENV TZ="America/Bogota"

# Copia los archivos JAR generados en `build/libs/` al contenedor
ARG JAR_FILE=build/libs/z-music-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

EXPOSE 8080/tcp

ENTRYPOINT ["java","-jar","/app.jar"]