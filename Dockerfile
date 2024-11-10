# Usa una imagen base de Java
FROM openjdk:21-jdk-slim
  
  # Define el directorio de trabajo
WORKDIR /app
  
  # Copia ambos archivos JAR generados en `build/libs/` al contenedor
COPY build/libs/app1.jar /app/app1.jar
COPY build/libs/app2.jar /app/app2.jar
  
  # Ejemplo de cómo ejecutar ambos .jar
  # Puedes ejecutar uno de ellos como principal, o ambos si necesitas que se ejecuten en paralelo.
  
  # Comando para ejecutar ambos .jar en paralelo si es necesario.
  # Usa '&&' si solo quieres ejecutar app2.jar después de que app1.jar termine
CMD ["sh", "-c", "java -jar /app/app1.jar & java -jar /app/app2.jar"]

# Si solo uno debe ejecutarse por defecto, ajusta el comando CMD
# CMD ["java", "-jar", "/app/app1.jar"]