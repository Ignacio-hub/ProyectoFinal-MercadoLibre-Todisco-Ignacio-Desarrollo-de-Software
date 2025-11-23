# ========================================
# ETAPA 1: BUILD (Compilación)
# Se usa temurin:17-jdk-alpine (Java 17, JDK)
# ========================================
FROM temurin:17-jdk-alpine AS build

# 1. Instalar dependencias del sistema (Solo si es necesario para el proyecto)
# En este caso, ya que usamos una base Alpine, instalamos OpenJDK 17.
# Sin embargo, el -jdk-alpine ya suele incluir el JDK, pero es buena práctica actualizar.
RUN apk update

# 2. Establecer el directorio de trabajo
WORKDIR /app

# 3. Copiar archivos de configuración de Gradle para el caching de dependencias
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
# No necesitas settings.gradle si no lo estás usando, si lo usas, descomenta:
# COPY settings.gradle .

# 4. Dar permisos de ejecución al script gradlew
RUN chmod +x ./gradlew

# 5. Descargar dependencias (usando caché)
RUN --mount=type=cache,target=/root/.gradle ./gradlew dependencies --no-daemon

# 6. Copiar el código fuente completo
COPY src src

# 7. Compilar y generar el JAR ejecutable (omitiendo tests para un build más rápido)
RUN --mount=type=cache,target=/root/.gradle ./gradlew bootJar -x test --no-daemon

# ========================================
# ETAPA 2: RUNTIME (Ejecución - Imagen Ligera)
# Se usa temurin:17-jre-alpine (Java 17, JRE ligero)
# ========================================
FROM temurin:17-jre-alpine

# 1. Definir el directorio de trabajo
WORKDIR /app

# 2. Documentar que la aplicación escucha en el puerto 8080
EXPOSE 8080

# 3. Copiar el JAR generado de la etapa 'build'
# Usamos un comodín con la versión '1.0.0-SNAPSHOT' que es la de tu proyecto
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

# 4. Comando de ejecución de la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
COPY --from=build /app/build/libs/*-1.0.0-SNAPSHOT.jar ./app.jar

