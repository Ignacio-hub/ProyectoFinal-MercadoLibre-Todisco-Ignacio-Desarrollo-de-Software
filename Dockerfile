# ----------------------------------------------------------------------------------
# ETAPA 1: BUILD (Compilación)
# ----------------------------------------------------------------------------------
# Usamos JDK 17, que coincide con tu configuración de build.gradle.
FROM eclipse-temurin:17-jdk-focal AS builder

# Establece el directorio de trabajo
WORKDIR /app

# Copia build.gradle y el script ejecutable gradlew
COPY build.gradle gradlew ./

# **CORRECCIÓN CLAVE:** Copiamos todo el contenido de la carpeta 'gradle/' a '/app/gradle'
# Esto asegura que el archivo gradle-wrapper.properties (que contiene la URL de descarga) sea incluido.
COPY gradle/ ./gradle/

# Copia el código fuente
COPY src ./src

# Asegura el permiso de ejecución del script de Gradle
RUN chmod +x ./gradlew

# Ejecuta la tarea 'bootJar'
# Si quieres ver más detalles del error, podrías probar localmente sin el "-x test" para que Gradle imprima la razón.
RUN ./gradlew clean bootJar -x test
RUN find build/libs -name '*-SNAPSHOT.jar' ! -name '*-plain.jar' -exec mv {} app.jar \;
# Define el nombre y la ruta del JAR resultante
ARG JAR_FILE=app.jar


# ----------------------------------------------------------------------------------
# ETAPA 2: RUN (Ejecución/Producción)
# ----------------------------------------------------------------------------------
# Usamos JRE 17 para la ejecución final, manteniendo la imagen ligera.
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/${JAR_FILE} app.jar

# Define el comando de inicio (usando la configuración simplificada que solicitaste)
ENTRYPOINT ["java","-jar","/app/app.jar"]

# Puerto expuesto
EXPOSE 8080