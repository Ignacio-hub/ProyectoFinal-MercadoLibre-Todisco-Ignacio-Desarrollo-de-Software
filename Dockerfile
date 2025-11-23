# ========================================
# ETAPA 1: BUILD (Compilación)
# USANDO MCR (Microsoft Container Registry) - La fuente más estable para Temurin Alpine
# ========================================
FROM mcr.microsoft.com/openjdk/jdk:17-alpine AS build

# La imagen ya es Alpine y ya trae el JDK 17. Las líneas 'apk' son redundantes y las eliminamos
# para evitar errores de conflictos o de sintaxis al instalar paquetes ya existentes.

# 1. Establecer el directorio de trabajo
WORKDIR /app

# 2. Copiar archivos de configuración de Gradle para el caching de dependencias
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
# Si usas settings.gradle, descomenta:
# COPY settings.gradle .

# 3. Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# 4. Descargar dependencias (usando caché)
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# 5. Copiar el código fuente completo
COPY src src

# 6. Compilar y generar el JAR ejecutable (omitiendo tests)
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución - Imagen Ligera)
# USANDO MCR (Microsoft Container Registry) para JRE ligero
# ========================================
FROM mcr.microsoft.com/openjdk/jre:17-alpine

# 1. Definir el directorio de trabajo
WORKDIR /app

# 2. Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# 3. Copiar el JAR generado de la etapa 'build'
# Tu versión es '1.0.0-SNAPSHOT'
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

# 4. Comando de ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
