# ========================================
# ETAPA 1: BUILD (Compilación)
# CORRECCIÓN: Usando el repositorio 'temurin' para evitar "not found"
# ========================================
FROM temurin:21-jdk-alpine as build

# Alpine usa 'apk' como gestor de paquetes. Como temurin ya trae el JDK,
# solo necesitamos actualizar el índice y luego se instala OpenJDK si no lo trae.
# Las imágenes de Temurin generalmente ya incluyen lo necesario, pero estas líneas 
# aseguran que se encuentre la toolchain.
RUN apk update && apk add openjdk21

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración y wrapper para caching.
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
# Si tienes settings.gradle
# COPY settings.gradle .

# Ejecuta un 'fake build' para descargar dependencias y aprovechar el cache de Gradle
# Se monta el volumen de cache de Gradle para la descarga de dependencias
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# Copiar el código fuente completo
COPY src src

# Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# Compilar y generar el JAR ejecutable. Omitimos tests para un build más rápido.
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución)
# CORRECCIÓN: Usando el repositorio 'temurin' para el JRE ligero
# ========================================
FROM temurin:21-jre-alpine

# Definir el directorio de trabajo
WORKDIR /app

# Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# Copiar el JAR generado de la etapa 'build'. Usamos comodín para el nombre del archivo.
# ASUME que la versión es *-1.0.0-SNAPSHOT.jar. Ajustar si el nombre final es diferente.
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

# Comando de ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
