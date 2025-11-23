# ========================================
# ETAPA 1: BUILD (Compilación)
# ADAPTADO A JAVA 17: Usamos la versión 17 de Temurin en Alpine
# ========================================
FROM eclipse-temurin:17-jdk-alpine AS build


# 1. Eliminar la actualización de paquetes, ya que la imagen base ya es completa.
# RUN apk update
# RUN apk add openjdk17
# Ya no son necesarios.

# 2. Establecer el directorio de trabajo
WORKDIR /app

# 3. Copiar archivos de configuración de Gradle para el caching de dependencias
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
# COPY settings.gradle .

# 4. Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# 5. Copiar el código fuente completo
COPY src src

# 6. Compilar y generar el JAR ejecutable (usando caché y omitiendo tests)
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# ADAPTADO A JAVA 17
# ========================================
FROM eclipse-temurin:17-jre-alpine

# 1. Definir el directorio de trabajo
WORKDIR /app

# 2. Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# 3. Copiar el JAR generado en la ETAPA 1 (build)
# CORRECCIÓN VITAL: Se ajusta el nombre del JAR a la versión 1.0.0-SNAPSHOT de tu proyecto
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

# 4. Comando de ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]