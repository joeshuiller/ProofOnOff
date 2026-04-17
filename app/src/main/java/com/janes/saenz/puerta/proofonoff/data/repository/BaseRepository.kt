package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.ui.utlis.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import retrofit2.Response
import java.io.IOException

/**
 * BaseRepository - Proveedor de utilidades para la gestión segura de datos.
 *
 * Esta clase base encapsula la complejidad de las llamadas asíncronas,
 * estandarizando la respuesta en el contenedor [Resource].
 */
abstract class BaseRepository {

    /**
     * Ejecuta una llamada suspendida de red y la envuelve en un [Resource].
     * * Ideal para operaciones One-Shot (ej. Post, Delete, Login).
     */
    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Resource<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                Resource.Success(body)
            } else {
                val errorMsg = parseError(response.code())
                Resource.Error(errorMsg, response.code())
            }
        } catch (e: IOException) {
            Resource.Error("${e.localizedMessage} No hay conexión a internet.")
        }
    }

    /**
     * Convierte un [Flow] de datos locales (Room) en un flujo de [Resource].
     * * Maneja automáticamente el estado inicial de carga y captura errores de DB.
     *
     * @receiver Flow de datos puros.
     * @return Flow envuelto en estados Success, Loading o Error.
     */
    fun <T> Flow<T>.asResource(): Flow<Resource<T>> {
        return this
            .map { data -> Resource.Success(data) as Resource<T> }
            .onStart { emit(Resource.Loading) }
            .catch { e ->
                emit(Resource.Error("${e.localizedMessage}"))
            }
    }

    /**
     * Versión reactiva de safeApiCall para flujos de red.
     * * Emite: [Resource.Loading] -> [Resource.Success] o [Resource.Error].
     * * Forzado a ejecutarse en [Dispatchers.IO].
     */
    fun <T> safeApiCallFlow(apiCall: suspend () -> Response<T>): Flow<Resource<T>> = flow {
        emit(Resource.Loading)
        try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                emit(Resource.Success(body))
            } else {
                val errorMsg = parseError(response.code())
                emit(Resource.Error(errorMsg, response.code()))
            }
        } catch (e: IOException) {
            emit(Resource.Error("${e.localizedMessage} Sin conexión a internet"))
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Traductor de códigos de estado HTTP a mensajes amigables.
     */
    private fun parseError(code: Int): String {
        return when (code) {
            Constants.UNAUTHORIZED -> "Sesión expirada"
            Constants.NOT_FOUND -> "Recurso no encontrado"
            Constants.SERVER_ERROR -> "Error en el servidor"
            else -> "Ha ocurrido un error inesperado $code"
        }
    }
}
