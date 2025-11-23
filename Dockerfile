# ========================================
# ETAPA 1: BUILD (Compilación)
# CORRECCIÓN: Usamos el nombre completo del repositorio adoptopenjdk
# ========================================
FROM adoptopenjdk/openjdk17:jdk-alpine AS build 

# Actualizar y limpiar. Dado que adoptopenjdk/openjdk17 ya incluye el JDK,
# la línea apk add openjdk17 es redundante e incorrecta. La quitamos.
RUN apk update

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Gradle para el caching de dependencias
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .

# Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# Descargar dependencias (usando caché)
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# Copiar el código fuente completo
COPY src src

# Compilar y generar el JAR ejecutable (omitiendo tests para un build más rápido)
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución - Imagen Ligera)
# CORRECCIÓN: Usamos el nombre completo del repositorio adoptopenjdk
# ========================================
FROM adoptopenjdk/openjdk17:jre-alpine

# Definir el directorio de trabajo
WORKDIR /app

# Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# Copiar el JAR generado de la etapa 'build'
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

# Comando de ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
