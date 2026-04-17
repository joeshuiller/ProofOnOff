# Documentación Técnica: App La Hinchada 2026

Bienvenido a la documentación oficial de la aplicación móvil para la campaña **"Proof OnOff"**. Este proyecto está construido bajo los principios de **Clean Architecture** y **Domain Driven Design (DDD)**.

## ⚠️ Seguridad y Privacidad (PII)
De acuerdo con la vulnerabilidad **10062**, toda la información de identificación personal (nombres, documentos, teléfonos) debe ser tratada con extrema precaución:
- **Cifrado Local:** Los datos en `Room` deben estar protegidos mediante SQLCipher.
- **Logs:** Queda prohibido el uso de `Log.d` en producción para objetos que contengan `AuthResponse`.

## 🏗️ Arquitectura del Sistema
El proyecto se divide en tres capas principales para garantizar la testeabilidad y escalabilidad:

1. **Domain (Dominio):** Contiene las reglas de negocio, entidades puras y las interfaces de los repositorios. No tiene dependencias de Android.
2. **Data (Infraestructura):** Implementación de la persistencia local con [Room] y consumo de API REST mediante [Retrofit]. Aquí reside la lógica del `Resource<T>`.
3. **Presentation (Presentación):** Interfaz de usuario construida con **Jetpack Compose** y manejo de estados mediante [ViewModels].



## 🌐 Consumo de API https://jsonplaceholder.typicode.com/posts
La comunicación con el backend de Laravel se realiza de forma reactiva.
- **Base URL:** Definida en `BuildConfig.URL_API`.
- **Manejo de Estados:** Se utiliza la clase `Resource<T>` para representar el ciclo de vida de una petición (Loading, Success, Error).

### Ejemplo de flujo de Autenticación:
```kotlin
// El repositorio retorna un flujo de estados
val result = userRepository.login(credentials) 

// La UI reacciona al Resource<AuthResponse?>
when(result) {
    is Resource.Success -> // Navegar a Home
    is Resource.Error -> // Mostrar error de seguridad
}