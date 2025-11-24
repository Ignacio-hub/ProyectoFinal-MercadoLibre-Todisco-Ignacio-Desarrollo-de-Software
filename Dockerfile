# ----------------------------------------------------------------------------------
# ETAPA 1: BUILD (Compilación)
# ----------------------------------------------------------------------------------
# Usamos JDK 17, ahora coincidiendo con tu build.gradle.
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# Copia los archivos necesarios para Gradle
COPY build.gradle gradlew ./
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/

# Copia el código fuente
COPY src ./src

# Asegura el permiso de ejecución del script de Gradle
RUN chmod +x ./gradlew

# Ejecuta la tarea 'bootJar'
RUN ./gradlew clean bootJar -x test

# Define el nombre y la ruta del JAR resultante
ARG JAR_FILE=build/libs/*.jar


# ----------------------------------------------------------------------------------
# ETAPA 2: RUN (Ejecución/Producción)
# ----------------------------------------------------------------------------------
# Usamos JRE 17 para la ejecución (ligeramente más pequeña que el JRE 21).
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia el JAR compilado
COPY --from=builder /app/${JAR_FILE} app.jar

# Define el comando de inicio (sin el parámetro opcional que decidiste omitir)
ENTRYPOINT ["java","-jar","/app/app.jar"]

# Puerto expuesto
EXPOSE 8080