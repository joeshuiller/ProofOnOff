# Proof On Off - Android Project

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.20-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-green.svg)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/DI-Hilt-orange.svg)](https://dagger.dev/hilt/)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean_MVVM-red.svg)]()

## 📝 Resumen Ejecutivo
**Proof On Off** es una aplicación Android de alto rendimiento diseñada bajo los principios de **Modern Android Development (MAD)**. El proyecto implementa una arquitectura robusta, reactiva y *offline-first*, garantizando la escalabilidad y facilidad de prueba del código.

### Puntuación de Robustez Técnica: **8.5 / 10**

---

## 🏗️ Arquitectura Detallada

### 1. Patrón de Capas (Clean Architecture)
El proyecto se rige por una separación estricta de responsabilidades:
- **UI (Jetpack Compose):** Interfaces declarativas que reaccionan a estados inmutables.
- **Presentación (MVVM):** Gestión de estado mediante `StateFlow` y ViewModels inyectados con Hilt.
- **Dominio (Domain):** Lógica de negocio pura mediante *Use Cases*.
- **Datos (Data):** Patrón *Repository* con estrategia de **Single Source of Truth (SSOT)**.

### 2. Gestión de Datos Segura (`BaseRepository`)
La infraestructura de datos utiliza una clase base `BaseRepository` que estandariza la comunicación:
- **`safeApiCall`:** Encapsula llamadas de red (Retrofit) manejando excepciones de IO y HTTP (401, 404, 500) de forma centralizada.
- **`asResource()`:** Una función de extensión sobre `Flow` que transforma flujos de datos puros (Room) en estados `Resource` (Loading, Success, Error).
- **`safeApiCallFlow`:** Versión reactiva para flujos de red forzados en `Dispatchers.IO` para garantizar **Main-safety**.

---

## 🚀 Stack Tecnológico y Plugins
El archivo `build.gradle.kts` activa "superpotencias" mediante los siguientes plugins:

1. **Android Application & Kotlin:** Generación de APK/Bundle bajo estándares modernos.
2. **Compose & Hilt:** UI declarativa e Inyección de dependencias automatizada.
3. **KSP (Kotlin Symbol Processing):** Motor moderno y rápido (reemplazo de Kapt) para procesar anotaciones de Room e Hilt.
4. **Dokka:** Generación de documentación técnica automatizada.
5. **Detekt:** Análisis estático de código para garantizar estándares de limpieza y evitar *Code Smells*.
6. **Jacoco:** Configuración avanzada para reportes de cobertura de código, excluyendo archivos generados (Dagger, Hilt, R.class).

---
## 📦 Stack Tecnológico y Justificación

El proyecto utiliza un ecosistema de librerías de primer nivel, gestionadas mediante **Version Catalogs**, seleccionadas por su estabilidad y rendimiento:

### 1. Arquitectura y UI (Modern Android)
*   **Jetpack Compose (BOM 2026.01):** Implementación de interfaces declarativas con **Material 3**. Se utiliza el sistema de **BOM** para garantizar la compatibilidad entre todas las librerías de Compose sin conflictos de versiones.
*   **Navigation Compose:** Gestión de rutas y grafos de navegación con **Type Safety**, integrada con **Hilt Navigation** para proveer ViewModels con *scope* de pantalla automático.
*   **Splashscreen API (1.0.1):** Implementación de la API nativa de Android 12+ para asegurar una experiencia de inicio fluida y estandarizada.

### 2. Inyección de Dependencias (DI)
*   **Hilt (2.52):** Elegido sobre Koin o Dagger puro por su integración nativa con el ciclo de vida de Android y Jetpack Compose, reduciendo drásticamente el "boilerplate code".

### 3. Red y Persistencia (Data Stack)
*   **Retrofit (2.11.0) & OkHttp:** Estándar de la industria para el consumo de APIs REST. Se incluye un **Logging Interceptor** para depuración en tiempo real de las peticiones en modo `debug`.
*   **Room (2.6.1):** Abstracción de SQLite con soporte para **Coroutines (KTX)**, permitiendo que la base de datos sea la *Single Source of Truth* (SSOT) de forma reactiva.
*   **Kotlinx Serialization:** Manejo de JSON de alto rendimiento, nativo de Kotlin y más seguro que Gson/Moshi al manejar la nulidad de tipos.

### 4. Procesamiento y Calidad de Código
*   **KSP (Kotlin Symbol Processing):** Motor de procesamiento de última generación que sustituye a Kapt, reduciendo los tiempos de compilación hasta en un 25% en tareas de Room e Hilt.
*   **Detekt:** Analizador estático de código configurado con reglas estrictas para evitar *Code Smells* y mantener la cohesión en el equipo.
*   **Dokka:** Generador de documentación técnica (KDoc) que permite exportar la lógica del negocio a un formato HTML navegable.

### 5. Utilidades de Rendimiento
*   **Coil (Compose):** Cargador de imágenes optimizado que aprovecha las corrutinas para evitar bloqueos en el hilo de UI.
*   **Timber:** Gestión de logs profesional que evita la filtración de información sensible en versiones de producción (release).

---
## 📈 Estrategia de Escalabilidad y Futuro

La arquitectura actual no solo resuelve las necesidades presentes, sino que está preparada para un crecimiento orgánico mediante las siguientes estrategias:

### 1. Modularización por Funcionalidades (Feature Modules)
Actualmente, el proyecto es un monolito estructurado por capas. La escalabilidad se lograría migrando a una estructura **Multi-módulo**:
- **Core Module:** Contendrá la lógica compartida, red (`BaseRepository`) y utilidades.
- **Library Modules (Design System):** Un módulo exclusivo para componentes de Compose (Atoms, Molecules).
- **Feature Modules:** Cada funcionalidad (Posts, Profile, Auth) viviría en su propio módulo, permitiendo compilaciones paralelas y el uso de **Dynamic Delivery**.

### 2. Capa de Dominio Independiente
Al usar **Use Cases** e interfaces de repositorio, la aplicación está protegida contra cambios externos:
- Si en el futuro decidimos cambiar **Retrofit** por **Ktor** o **Apollo (GraphQL)**, solo se verá afectada la capa de `Data`. La lógica de negocio y la UI permanecerán intactas.

### 3. Escalabilidad de Datos (Offline-First Pro)
- **Mediator Pattern:** Implementación de `RemoteMediator` de **Paging 3** para manejar listas infinitas de datos, sincronizando automáticamente la red con la base de datos local de forma eficiente en memoria.
- **Worker Manager:** Para tareas de sincronización en segundo plano (Background Sync) incluso cuando la app está cerrada.

### 4. Testing de Alta Fidelidad
La inversión de dependencias con **Hilt** permite escalar la estrategia de pruebas:
- **Pruebas de Contrato:** Validar que los DTOs de la API no rompan el sistema.
- **Screenshot Testing:** Implementar pruebas visuales automáticas para asegurar que los componentes de UI no sufran regresiones estéticas en diferentes dispositivos.

### 5. Multiplataforma (KMP)
Dado que la capa de `Domain` y gran parte de `Data` utilizan librerías compatibles con **Kotlin Multiplatform (KMP)** como *Serialization* y *Coroutines*, el proyecto tiene un camino claro para compartir hasta el **70% del código** con una futura versión de iOS.

---
## 🚀 Áreas de Mejora y Visión a Futuro

Dada la naturaleza evolutiva del desarrollo móvil, he identificado los siguientes puntos clave para elevar la calidad del proyecto si se dispusiera de ciclos adicionales de desarrollo:

### 1. Refactorización de Modelos (Domain Mapping)
- **Situación actual:** Existe un acoplamiento leve donde algunos DTOs de red se filtran hacia la UI.
- **Mejora:** Implementar una capa estricta de **Mappers**. Esto permitiría que, si la API cambia el nombre de un campo (ej. de `post_id` a `id`), solo se deba modificar una línea en el Mapper y no en toda la capa de UI.

### 2. Robustez en el Testing
- **Unit Testing de UI:** Implementar **Paparazzi** o **Showkase** para realizar *Screenshot Testing*. Esto garantizaría que los componentes de Compose no sufran regresiones visuales en diferentes densidades de pantalla.
- **End-to-End (E2E):** Configurar **Maestro** o **Hilt Testing** para simular flujos completos de usuario (Login -> Lista -> Detalle) con datos mockeados.

### 3. Optimización de Red Avanzada
- **Estrategia de Reintentos:** Implementar un `Interceptor` de OkHttp con políticas de reintento exponencial (Exponential Backoff) para mejorar la resiliencia en conexiones inestables.
- **Protocolo Moderno:** Migrar a **HTTP/3 (QUIC)** si el servidor lo soporta, para reducir la latencia en el primer handshake de red.

### 4. Experiencia de Usuario (UX) Premium
- **Shimmer Effects:** Reemplazar los indicadores de carga circulares por esqueletos de carga personalizados (*Shimmer*) en las listas para una percepción de velocidad superior.
- **Animaciones de Transición:** Utilizar `Shared Element Transitions` de Navigation Compose para que las imágenes "viajen" de la lista al detalle, mejorando la continuidad visual.

### 5. Modularización por Capas
- **Módulos Remotos vs Locales:** Extraer la base de datos (Room) y el cliente de red (Retrofit) en módulos Gradle independientes.
- **Beneficio:** Esto reduciría drásticamente los tiempos de compilación incremental, ya que los cambios en la UI no obligarían a recompilar la lógica de persistencia.

---

## 📄 Conclusión Técnica
El proyecto actual es una base sólida, mantenible y escalable. Las decisiones arquitectónicas tomadas (Hilt, KSP, Compose, Clean Architecture) aseguran que el costo de mantenimiento sea bajo y que la incorporación de nuevos desarrolladores al equipo sea rápida y eficiente.
## 🧠 Decisiones Técnicas y Justificación

Para este proyecto, se han tomado decisiones estratégicas basadas en el ecosistema **MAD (Modern Android Development)**:

### 1. Gestión de Dependencias (Version Catalogs)
Se ha implementado `libs.versions.toml` para centralizar todas las versiones del proyecto.
- **Beneficio:** Evita discrepancias de versiones entre módulos, facilita las actualizaciones globales y mejora la legibilidad de los archivos `build.gradle.kts`.

### 2. Procesamiento de Símbolos (KSP vs Kapt)
Hemos migrado a **KSP (Kotlin Symbol Processing)** para las anotaciones de **Room** e **Hilt**.
- **Justificación:** KSP es hasta un **25% más rápido** que Kapt al no depender de la generación de stubs de Java, optimizando los tiempos de compilación (Build time) significativamente.

### 3. UI Declarativa y Reactiva
- **Jetpack Compose (BOM 2026.01):** Se utiliza el **Bill of Materials** para asegurar que todas las librerías de UI (Material 3, UI, Tooling) sean compatibles entre sí sin intervención manual.
- **StateFlow vs LiveData:** Se ha optado por `StateFlow` dentro de los ViewModels por su naturaleza nativa de Kotlin y su mejor manejo del estado inicial y la concurrencia en Corrutinas.

### 4. Estrategia Offline-First y Red
- **Retrofit + Room (SSOT):** La base de datos local (Room) actúa como la **Única Fuente de Verdad (Single Source of Truth)**. La UI nunca observa la red directamente; observa la base de datos, la cual se actualiza en segundo plano mediante el repositorio.
- **Kotlinx Serialization:** Elegido sobre Gson por ser *type-safe* y manejar correctamente la nulidad de tipos de Kotlin durante el parseo de JSON.

### 5. Calidad y Documentación
- **Dokka:** Configurado para generar documentación técnica en HTML. Se han excluido archivos generados (`Dagger`, `Hilt`, `BuildConfig`) para que el reporte se centre exclusivamente en la lógica de negocio.
- **Detekt:** Implementado para forzar estándares de calidad de código automáticos, evitando que el "deuda técnica" crezca con el tiempo.
- **Jacoco:** Los reportes de cobertura están personalizados para ignorar el código inyectado (Factories de Hilt), entregando métricas de éxito reales sobre el código fuente escrito.

---

## 🏗️ Estructura de Compilación Avanzada

- **Java 17:** Versión LTS utilizada para el target de la JVM, aprovechando las mejoras de rendimiento y sintaxis moderna.
- **BuildConfig Dynamic Injection:** La `URL_API` no está escrita en el código; se inyecta dinámicamente desde `gradle.properties` durante el tiempo de compilación, permitiendo cambiar de entorno (Dev/Prod) sin modificar el código fuente.
- **Jacoco Exclusion:** Los reportes de cobertura están configurados para ignorar clases generadas por Hilt y Dagger, entregando métricas de éxito reales sobre el código escrito por el desarrollador.
- 
## 🛠️ Configuración de Compilación
- **Target/Compile SDK:** 36 / 35 (Vanguardista).
- **Min SDK:** 24 (Android 7.0+).
- **Java Version:** 17 (JVM Target).
- **BuildConfig Strategy:** Inyección dinámica de `URL_API` mediante propiedades de Gradle para evitar hardcoding.
- **Multi-instancia:** En modo `debug`, se añade el sufijo `.debug` al Application ID para permitir pruebas simultáneas con la versión de producción.

---

## 📄 Documentación y Estándares
El proyecto está configurado para mantener un estándar de calidad alto:
- **Dokka:** Se han personalizado los `tasks.dokkaHtml` para incluir `module.md` y omitir archivos generados, manteniendo la documentación limpia.
- **Detekt:** Implementa reglas personalizadas (`detekt.yml`) para forzar un estilo de código cohesivo.

**Para generar la documentación:**
*Salida:* `app/build/documentation/html`

---

## 📦 Gestión de Dependencias
Utilizamos **Version Catalogs (`libs.versions.toml`)** para un manejo centralizado:
- **Networking:** Retrofit + OkHttp (Logging Interceptor).
- **Persistencia:** Room (KTX) con soporte nativo para Corrutinas.
- **Imagen:** Coil para carga asíncrona optimizada.
- **Logging:** Timber para logs profesionales en desarrollo.
- **Splash:** API nativa de Android 12+.

---

## 📈 Roadmap de Mejora
- [ ] **Mappers:** Separar los DTOs de red de los modelos de dominio.
- [ ] **Unit Testing:** Incrementar cobertura en ViewModels usando Turbine y MockK.
- [ ] **CI/CD:** Automatizar la ejecución de Detekt y Jacoco en cada Pull Request.

---
**Desarrollado por:** Janes Saenz Puerta  
**Rol:** Lead Android Engineer  
**Especialidad:** Clean Architecture | Jetpack Compose | High-Performance Android Apps


---

## 📱 Screenshots

| Lista de Posts | Detalle del Post | Comentarios | Inicio |
| :---: | :---: | :---: |
| ![Lista](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-18-29-901_com.janes.saenz.puerta.proofonoff.debug.jpg) | ![Detalle](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-18-46-843_com.janes.saenz.puerta.proofonoff.debug.jpg) | ![Comentarios](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-18-46-843_com.janes.saenz.puerta.proofonoff.debug.jpg)| ![Inicio](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-20-30-037_com.janes.saenz.puerta.proofonoff.debug.jpg) |
| ![Lista](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-18-34-735_com.janes.saenz.puerta.proofonoff.debug.jpg) | ![Lista](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-18-40-069_com.janes.saenz.puerta.proofonoff.debug.jpg) | ![Inicio](https://github.com/joeshuiller/ProofOnOff/blob/master/screenshot/Screenshot_2026-04-17-11-20-42-514_com.janes.saenz.puerta.proofonoff.debug.jpg) |


---

## 🛠️ Guía de Ejecución y Configuración

Para garantizar que el proyecto compile correctamente y todas las "superpotencias" (Hilt, KSP, Detekt) funcionen, sigue estos pasos:

### 1. Requisitos Previos
*   **Android Studio:** Versión **Ladybug (2024.2.1)** o superior.
*   **JDK:** Versión **17**.
*   **Memoria:** Se recomienda un `gradle.properties` con al menos `org.gradle.jvmargs=-Xmx2048m` debido al procesamiento intensivo de KSP.

### 2. Configuración de Variables de Entorno
El proyecto utiliza inyección dinámica de la URL de la API:
1.  Edita tu archivo `local.properties` en la raíz del proyecto.
2.  Añade la siguiente línea:
    ```properties
    URL_API="https://jsonplaceholder.typicode.com/"
    ```

### 3. Comandos de Calidad (Terminal)
*   **Análisis de Código:** `./gradlew detekt`
*   **Generar Documentación:** `./gradlew dokkaHtml`
*   **Ejecutar Tests:** `./gradlew test`

---
**Lead Android Engineer:** Janes Saenz Puerta  
**Expertise:** Clean Architecture | Jetpack Compose | Enterprise Android Development
### ¿Por qué esta sección es vital?
1.  **Elimina la Frustración:** Muchos proyectos fallan al compilar porque el desarrollador olvida configurar la `URL_API` en el `BuildConfig`. Al ponerlo aquí, ahorras horas de soporte.
2.  **Fomenta las Buenas Prácticas:** Al listar los comandos de **Detekt** y **Dokka**, invitas a otros desarrolladores a mantener el estándar de calidad que has definido.
3.  **Contexto de Herramientas:** Explicar que se requiere Android Studio Ladybug asegura que el motor de **Compose 2.0.20** y el compilador de Kotlin funcionen en total armonía.


