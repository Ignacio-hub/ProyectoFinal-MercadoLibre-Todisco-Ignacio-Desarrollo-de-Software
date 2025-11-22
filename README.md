 🧬 **Mutant Detector API - Examen MercadoLibre**
 Proyecto Final.
🎯 **Objetivo del Proyecto**
Este proyecto es una API REST desarrollada en Spring Boot para resolver el desafío de detección de mutantes de Magneto. La API implementa un algoritmo eficiente para determinar si una secuencia de ADN es mutante basándose en la repetición de bases nitrogenadas.
Un ADN es considerado mutante si se encuentran más de una secuencia de cuatro letras idénticas (A, T, C, G) de forma horizontal, vertical u oblicua (diagonal).

👨‍💻**Información del Alumno**
Estudiante: Ignacio Todisco
Legajo: 51118
Materia: Desarrollo de Software
Año: 3er Año

📝 **Descripción del Proyecto por Niveles**
Este proyecto fue desarrollado para la detección de humanos mutantes analizando su secuencia de ADN, siguiendo un desafío incremental de tres niveles:

**Nivel 1: Algoritmo de Detección (Función Core)**
El objetivo principal fue desarrollar un algoritmo eficiente que implemente la función boolean isMutant(String[] dna).

**Entrada:** Un array de Strings que representan una matriz cuadrada (NxN) con las bases nitrogenadas (A, T, C, G).
**Regla de Mutación:** El algoritmo debe devolver true si encuentra más de una secuencia de cuatro letras iguales de forma horizontal, vertical u oblicua.
**Requisito Clave:** La función debe ser desarrollada de la manera más eficiente posible (implementando optimizaciones como la terminación anticipada).

**Nivel 2: Creación de la API REST**
El proyecto se expone como una API REST utilizando Spring Boot.

**Endpoint:** Se creó el servicio POST /mutant.
**Respuesta:** Debe devolver HTTP 200 OK si el ADN es mutante y HTTP 403 Forbidden si es humano.
**Arquitectura:** Se implementó una arquitectura limpia para separar las responsabilidades del controlador y la lógica de negocio.

**Nivel 3: Persistencia y Estadísticas**
Se añadieron requisitos de persistencia de datos y reporte de estadísticas.

**Persistencia:** Utilización de una base de datos H2 para almacenar los ADN verificados. Se implementó una estrategia para guardar solo un registro por ADN (utilizando el Hash SHA-256 como clave).
**Estadísticas:** Creación del servicio GET /stats.
**Salida de /stats:** Devuelve un JSON que contiene count_mutant_dna, count_human_dna y el ratio (mutantes/humanos).

🏗️**Arquitectura del Proyecto**
El proyecto sigue una arquitectura en capas sólida para asegurar que cada parte del código tenga una única responsabilidad. Esto facilita el testing, el mantenimiento y las optimizaciones.

**1. Controller:** Capa de Presentación. Recibe peticiones HTTP (POST /mutant, GET /stats) y retorna respuestas HTTP (200, 403, 400).

**2. Service:** Lógica de Negocio y Orquestación. Controla el flujo de la aplicación: calcula el Hash, verifica la caché y delega al Detector.

**3. Detector:** Lógica Core (Algoritmo). Contiene el algoritmo puro y optimizado (isMutant) para buscar las secuencias en la matriz de ADN.

**4. DTO:** Contrato de Datos. Define la estructura de los datos que entran y salen de la API (JSON). Incluye las validaciones custom (@ValidDnaSequence).

**5. Entity:** Modelo de Persistencia. Mapea los objetos Java a las tablas de la base de datos (ORM). Contiene el campo dnaHash como clave primaria para la deduplicación.

**6. Repository:** Acceso a Datos (JPA). Proporciona la interfaz para ejecutar consultas y operaciones básicas contra la base de datos (p. ej., countByIsMutant, existsByDnaHash).

**7	Configuración:** Capa Transversal (Infraestructura). Contiene la configuración necesaria para la documentación interactiva de la API con Swagger/OpenAPI.

**Docker:** El Dockerfile es esencial para este proyecto porque empaqueta y estandariza la Mutant Detector API para su despliegue, asegurando que la aplicación (Spring Boot con Gradle) se ejecute de manera idéntica en cualquier entorno. Este proceso utiliza una Construcción Multi-Etapa para optimizar el tamaño de la imagen final.

**Para construir la imagen:** se utiliza el comando **docker build -t mutant-detector-api**
**Para correr el contenedor y hacer la API accesible:** Se utiliza el comando **docker run -d -p 8080:8080 --name mutant-api mutant-detector-api**

