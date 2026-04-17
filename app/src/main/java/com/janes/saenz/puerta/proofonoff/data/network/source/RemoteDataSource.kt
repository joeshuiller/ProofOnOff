package com.janes.saenz.puerta.proofonoff.data.network.source

import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import retrofit2.Response

/**
 * RemoteDataSource - Contrato para la recuperación de datos externos.
 * * Define las operaciones de red de bajo nivel. Las implementaciones de esta
 * interfaz son responsables de la comunicación directa con los endpoints de la API.
 */
interface RemoteDataSource {

    /**
     * Realiza una petición asíncrona para obtener todas las publicaciones.
     * * Esta operación es de suspensión (suspend), diseñada para ejecutarse
     * fuera del hilo principal (UI Thread).
     *
     * @return Un objeto [Response] de Retrofit que encapsula la lista de [PostsResponse].
     * Provee acceso tanto al cuerpo de la respuesta como a los metadatos de red.
     */
    suspend fun getPosts(): Response<List<PostsResponse>>
}
