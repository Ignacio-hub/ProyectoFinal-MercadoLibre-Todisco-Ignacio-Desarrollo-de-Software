# 🧬 API Detector de Mutantes: Proyecto Final MercadoLibre

## 🎯 Objetivo del Proyecto

Este proyecto es una API REST desarrollada en Spring Boot para resolver el desafío de detección de mutantes de Magneto. La API implementa un algoritmo eficiente para determinar si una secuencia de ADN es mutante basándose en la repetición de bases nitrogenadas.

Un ADN es considerado mutante si se encuentran más de una secuencia de cuatro letras idénticas (A, T, C, G) de forma horizontal, vertical u oblicua (diagonal).

***

## Información del Alumno

| Campo | Detalle |
| :--- | :--- |
| **Estudiante** | Ignacio Todisco |
| **Legajo** | 51118 |
| **Materia** | Desarrollo de Software |
| **Año** | 3er Año |

***

## Descripción del Proyecto

La API cumple con los tres niveles del desafío:

### Nivel 1

El objetivo principal fue desarrollar un algoritmo eficiente que implemente la función `boolean isMutant(String[] dna)`.

* **Entrada:** Un array de Strings que representan una matriz cuadrada (NxN) con las bases nitrogenadas (A, T, C, G).
* **Regla de Mutación:** El algoritmo debe devolver `true` si encuentra más de una secuencia de cuatro letras iguales de forma horizontal, vertical u oblicua.
* **Requisito Clave:** La función debe ser desarrollada de la manera más eficiente posible (implementando optimizaciones como la terminación anticipada).

### Nivel 2

El proyecto se expone como una API REST utilizando Spring Boot.

* **Endpoint:** Se creó el servicio `POST /mutant`.
* **Respuesta:** Debe devolver **HTTP 200 OK** si el ADN es mutante y **HTTP 403 Forbidden** si es humano.
* **Arquitectura:** Se implementó una arquitectura limpia para separar las responsabilidades del controlador y la lógica de negocio.

### Nivel 3

Se añadieron requisitos de persistencia de datos y reporte de estadísticas.

* **Persistencia:** Utilización de una base de datos **H2** para almacenar los ADN verificados. Se implementó una estrategia para guardar solo un registro por ADN (utilizando el Hash SHA-256 como clave).
* **Estadísticas:** Creación del servicio **`GET /stats`**.
* **Salida de /stats:** Devuelve un JSON que contiene `count_mutant_dna`, `count_human_dna` y el `ratio` (`mutantes/humanos`).

***

## 🏗️ Arquitectura del Proyecto

El proyecto sigue una arquitectura en capas sólida para asegurar que cada parte del código tenga una única responsabilidad. Esto facilita el testing, el mantenimiento y las optimizaciones.

1.  **Controller:** Capa de Presentación. Recibe peticiones HTTP (`POST /mutant`, `GET /stats`) y retorna respuestas HTTP (200, 403, 400).
2.  **Service:** Lógica de Negocio y Orquestación. Controla el flujo de la aplicación: calcula el Hash, verifica la caché y delega al Detector.
3.  **Detector:** Lógica Core (Algoritmo). Contiene el algoritmo puro y optimizado (`isMutant`) para buscar las secuencias en la matriz de ADN.
4.  **DTO:** Contrato de Datos. Define la estructura de los datos que entran y salen de la API (JSON). Incluye las validaciones custom (`@ValidDnaSequence`).
5.  **Entity:** Modelo de Persistencia. Mapea los objetos Java a las tablas de la base de datos (ORM). Contiene el campo `dnaHash` como clave primaria para la deduplicación.
6.  **Repository:** Acceso a Datos (JPA). Proporciona la interfaz para ejecutar consultas y operaciones básicas contra la base de datos (p. ej., `countByIsMutant`, `existsByDnaHash`).
7.  **Config:** Capa Transversal (Infraestructura). Contiene la configuración necesaria para la documentación interactiva de la API con Swagger/OpenAPI.

***

## 🚀 Instalación y Ejecución Local

### Prerrequisitos
* **Java Development Kit (JDK):** Versión 17 o superior.
* **Git:** Para clonar el repositorio.

### Paso 1: Clonar el Repositorio

git clone https://github.com/Ignacio-hub/ProyectoFinal-MercadoLibre-Todisco-Ignacio-Desarrollo-de-Software.git cd ProyectoFinal_MercadoLibre

**Paso 2: Compilar y Ejecutar la Aplicación** El proyecto utiliza el wrapper de Gradle para su ejecución.
**Windows:** gradlew.bat bootRun
**Mac/Linux:** ./gradlew bootRun

🧪 **Ejecución de Tests y Cobertura**
Para ejecutar todos los tests y generar el reporte de cobertura con JaCoCo (que verifica el requisito de Code Coverage > 80%):
**Windows (PowerShell):**	.\gradlew.bat clean test
**Mac/Linux:**	./gradlew clean test

**gradlew test:** Solo ejecuta los tests (más rápido, usa resultados previos).
**gradlew clean test:** Asegura que el entorno de ejecución esté limpio, lo cual es ideal para generar reportes de cobertura precisos y evitar fallos extraños por archivos antiguos.

🌐 **Uso de la API (Endpoints)**
Cuando el programa este corriendo podemos acceder a los siguientes sitios para comprobar que funciona correctamente
📄 **Acceso a la Documentación y la Base de Datos**
**Swagger UI:** http://localhost:8080/swagger-ui.html
**URL Render:** https://proyectofinal-mercadolibre-todisco.onrender.com/swagger-ui.html
**H2 Console:** http://localhost:8080/h2-console
**URL Render:** https://proyectofinal-mercadolibre-todisco.onrender.com/h2-console

**Datos Para Ingresar a la Base de Datos**
**JDBC URL:** jdbc:h2:mem:testdb
**User Name:** sa
**Password:** (Dejar vacio)

