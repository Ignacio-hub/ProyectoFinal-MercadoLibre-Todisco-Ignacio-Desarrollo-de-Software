# ==============================================================================
# ETAPA 1: BUILDER (Compilación)
# Utiliza una imagen con JDK para compilar el código.
# ==============================================================================
FROM eclipse-temurin:17-jdk-slim AS builder
# 1. Actualiza el índice
RUN apk update

# 2. Instala el paquete de Java
RUN apk add openjdk17
# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle para que Docker pueda cachear la descarga de dependencias
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
COPY settings.gradle .

# Copia solo los archivos fuente que inician el build (opcional, pero mejora el caché)
# Ejecuta un 'fake build' para descargar dependencias si es posible
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# Copia el código fuente completo
COPY src src

# Da permisos de ejecución al wrapper (necesario en Linux/Docker)
RUN chmod +x ./gradlew

# Compila el proyecto y genera el JAR final.
# Usamos el cache de Gradle y omitimos los tests para acelerar el build en Docker.
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ==============================================================================
# ETAPA 2: RUNTIME (Ejecución - Imagen Ligera)
# Utiliza una imagen con solo el JRE para el ambiente de producción.
# ==============================================================================
FROM openjdk:17-jre-slim

# Exponer el puerto de la aplicación (8080 o el puerto configurado en application.properties)
EXPOSE 8080

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR generado desde la etapa 'builder'
# Asumimos que tu JAR se genera con el formato {group}-{version}.jar (ej: mutant-1.0.0-SNAPSHOT.jar)
# Usamos comodín para hacerlo más robusto al cambiar la versión.
COPY --from=builder /app/build/libs/*-1.0.0-SNAPSHOT.jar app.jar

# Comando de ejecución de la aplicación
# Usa el puerto configurado en application.properties, pero aquí podemos sobrescribir el puerto
ENTRYPOINT ["java", "-jar", "app.jar"]