package com.janes.saenz.puerta.proofonoff.data.utils
/**
 * Resource - Envoltorio semántico para el flujo de datos asíncronos.
 *
 * Implementa el patrón de "Estado de Resultado" para facilitar la comunicación
 * entre la capa de Datos/Dominio y la Capa de Presentación.
 *
 * @param T El tipo de entidad o DTO que se espera recibir en caso de éxito.
 */
sealed class Resource<out T> {

    /**
     * Representa una operación exitosa.
     * @param data Los datos recuperados de la fuente (Local o Remota).
     */
    data class Success<out T>(val data: T) : Resource<T>()

    /**
     * Representa un fallo en la operación.
     * @param message Descripción amigable o técnica del problema.
     * @param code Código de error opcional (ej. Código HTTP 404 o código interno de DB).
     */
    data class Error(
        val message: String,
        val code: Int? = null
    ) : Resource<Nothing>()

    /**
     * Representa el estado de transición o espera.
     * Se utiliza para activar Shimmers, Skeletons o Progress Indicators en la UI.
     */
    object Loading : Resource<Nothing>()
}