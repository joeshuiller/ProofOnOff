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