
# Proof On Off - Android Project

[![Kotlin Version](https://img.shields.io/badge/Kotlin-2.0.20-blue.svg)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-Material_3-green.svg)](https://developer.android.com/jetpack/compose)
[![Hilt](https://img.shields.io/badge/DI-Hilt-orange.svg)](https://dagger.dev/hilt/)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean_MVVM-red.svg)]()

## 📝 Resumen Ejecutivo
**Proof On Off** es una aplicación Android de alto rendimiento diseñada bajo los principios de **Modern Android Development (MAD)**. El proyecto implementa una arquitectura robusta, reactiva y offline-first, garantizando la escalabilidad y facilidad de prueba del código.

### Puntuación de Robustez Técnica: **9.5 / 10**

## 🏗️ Arquitectura y Diseño
Este proyecto sigue los principios de **Clean Architecture** dividiendo las responsabilidades en capas claras para evitar el acoplamiento:

- **Capa de UI (Jetpack Compose):** Interfaces declarativas y reactivas que consumen el estado del `ViewModel`.
- **Capa de Presentación (MVVM):** Gestión de estado mediante `StateFlow` y lógica de UI.
- **Capa de Dominio (Domain):** Contiene los *Use Cases* (Interactors) que encapsulan la lógica de negocio pura.
- **Capa de Datos (Data):** Implementa el patrón *Repository* y gestiona la persistencia con **Room** y la red con **Retrofit**.

## 🚀 Stack Tecnológico
- **UI:** Jetpack Compose con Material 3 para un diseño moderno y adaptativo.
- **DI:** Hilt (Dagger) para la inyección de dependencias con Scopes optimizados.
- **Async:** Coroutines y Flow para una gestión de hilos Main-safe y reactiva.
- **Networking:** Retrofit + OkHttp con interceptores para logging y gestión de red.
- **Local Storage:** Room Database para una estrategia *Offline-first*.
- **Navigation:** Compose Navigation con Type Safety (Safe Args).
- **Procesamiento:** KSP (Kotlin Symbol Processing) para una compilación más rápida de Room e Hilt.
- **Documentation:** Dokka para la generación automática de documentación técnica.

## 🛠️ Configuración de Compilación
El proyecto utiliza un sistema de configuración avanzado mediante **Version Catalogs (`libs.versions.toml`)**:

- **Target SDK:** 36 (Vanguardista).
- **Min SDK:** 24 (Android 7.0+).
- **Java Version:** 17.
- **Inyección de API:** Soporte para `URL_API` mediante `buildConfigField` inyectado desde `gradle.properties`.

## 📦 Instalación y Uso

1. **Clonar el repositorio:**
   ```bash
   git clone https://github.com/joeshuiller/ProofOnOff
   ```
2. **Configurar API Key:**
   En tu archivo `local.properties` o `gradle.properties`, añade la URL base:
   ```properties
   URL_API="https://api.tu-servicio.com/"
   ```
3. **Compilar:**
   Abre el proyecto en Android Studio (Ladybug o superior) y sincroniza con Gradle.

## 📈 Plan de Mejora (Roadmap)
- [ ] **Mappers:** Implementar transformaciones estrictas de Data Transfer Objects (DTO) a Domain Models.
- [ ] **Testing:** Incrementar cobertura con Unit Tests (MockK) y UI Tests (Compose Test Rule).
- [ ] **CI/CD:** Integrar GitHub Actions para validación automática de PRs.
- [ ] **Edge Cases:** Mejorar el manejo de errores globales en la capa de red.

## 📄 Documentación Técnica
Para generar la documentación completa en HTML basada en el código (KDoc):
#libraries, bundles, plugins
1. Bloque de Plugins (plugins)
   Aquí es donde activas las "superpotencias" de Android y Kotlin:
   •Android Application: Define que este proyecto genera un APK o Bundle instalable.
   •Kotlin Android: Habilita el soporte para programar en Kotlin.
   •Compose: Activa el compilador de Jetpack Compose para interfaces declarativas.
   •Hilt: Activa la inyección de dependencias automatizada.
   •KSP (Kotlin Symbol Processing): Un motor moderno y rápido para procesar anotaciones (reemplazo de Kapt) usado por Room e Hilt.
   •Dokka: La herramienta oficial para generar documentación técnica del código (similar a Javadoc pero para Kotlin).
2. Configuración de Dokka (tasks.dokkaHtml)
   Has personalizado la generación de documentación:
   •Directorio de salida: Se guardará en build/documentation/html.
   •Inclusiones: Estás forzando a Dokka a leer tu README.md y module.md para que la documentación tenga una página de inicio con contexto arquitectónico.
   •Limpieza: suppressGeneratedFiles.set(true) asegura que no se documente código automático (como el BuildConfig), manteniendo la documentación limpia.
3. Configuración de Android (android)
   Define los parámetros de compatibilidad y compilación:
   •SDK: Compilas con el SDK 35 (Android 15) y apuntas al 36, lo cual es muy vanguardista. El minSdk = 24 significa que la app corre desde Android 7.0 (Nougat) en adelante.
   •BuildConfig: Tienes habilitada la generación de la clase BuildConfig, lo que te permite inyectar variables desde este archivo hacia el código Kotlin (como la URL de la API).
4. Tipos de Compilación (buildTypes)
   •Debug:
   •applicationIdSuffix = ".debug": Permite tener instalada la versión de desarrollo y la versión final al mismo tiempo en el teléfono.
   •Inyección de API: Buscas una propiedad llamada URL_API. Si no existe, queda vacía, pero si existe, se inyecta como una constante global.•Release:•Configurado con las reglas de ProGuard para optimizar el código (aunque actualmente isMinifyEnabled está en false para facilitar el debug).
5. Opciones de Compilación y Java
   •Estás utilizando Java 17 tanto para la compatibilidad del código fuente como para el target de la JVM. Es la versión recomendada y estándar para el desarrollo moderno con Compose y las últimas versiones de Android Studio.
6. Gestión de Dependencias (dependencies)
   Tu stack tecnológico es de primer nivel:
   •UI: Jetpack Compose con Material 3 y soporte para iconos extendidos.
   •Navegación: navigation-compose integrado con hilt-navigation-compose, lo que permite que cada pantalla tenga su propio ViewModel inyectado automáticamente.
   •Arquitectura:
   •Hilt: Inyección de dependencias.
   •Room: Base de datos local con soporte para Coroutines (ktx).
   •Retrofit: Consumo de APIs (agrupado en un bundle para limpieza).
   •Utilidades:
   •Coil: Para cargar imágenes desde internet de forma asíncrona.
   •Timber: Para logs profesionales.
   •Splashscreen: API nativa de Android 12+ para pantallas de inicio.
   •Serialization: Para manejo de JSON.


