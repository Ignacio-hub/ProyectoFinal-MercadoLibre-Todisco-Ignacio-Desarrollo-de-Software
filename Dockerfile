# ----------------------------------------------------------------------------------
# ETAPA 1: BUILD (Compilación)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# Copia los archivos de configuración de Gradle y el código fuente.
COPY build.gradle settings.gradle ./
COPY src ./src

# Ejecuta la tarea 'bootJar' de Gradle para compilar el proyecto
# y crear el JAR ejecutable.
RUN ./gradlew clean bootJar -x test

# Define el nombre y la ruta del JAR resultante para la siguiente etapa.
ARG JAR_FILE=build/libs/*.jar


# ----------------------------------------------------------------------------------
# ETAPA 2: RUN (Ejecución/Producción)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia el JAR compilado desde la etapa 'builder' a la imagen final.
COPY --from=builder /app/${JAR_FILE} app.jar

# Define el comando que se ejecutará al iniciar el contenedor.
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/app.jar"]

# Expón el puerto que usa tu aplicación (ej. 8080).
EXPOSE 8080