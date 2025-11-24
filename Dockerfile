# ----------------------------------------------------------------------------------
# ETAPA 1: BUILD (Compilación)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /app

# SOLUCIÓN: Solo copia build.gradle y los archivos del wrapper si no tienes settings.gradle
# También necesitamos los scripts de Gradle (gradlew y gradle-wrapper.jar)
COPY build.gradle gradlew ./
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/

# Copia el código fuente
COPY src ./src

# Asegúrate de que el script gradlew sea ejecutable
RUN chmod +x ./gradlew

# Ejecuta la tarea 'bootJar' de Gradle
RUN ./gradlew clean bootJar -x test

# Define el nombre y la ruta del JAR resultante
ARG JAR_FILE=build/libs/*.jar


# ----------------------------------------------------------------------------------
# ETAPA 2: RUN (Ejecución/Producción)
# ----------------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/${JAR_FILE} app.jar

# Define el comando de inicio
ENTRYPOINT ["java","-jar","/app/app.jar"]

EXPOSE 8080