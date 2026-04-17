package com.janes.saenz.puerta.proofonoff.data.network.api

import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import retrofit2.Response
import retrofit2.http.GET

/**
 * ApiUrl - Definición de endpoints para el cliente REST Retrofit.
 * * Esta interfaz actúa como el puente de comunicación con el Backend.
 * Las funciones definidas aquí son mapeadas automáticamente a peticiones HTTP
 * por el framework Retrofit.
 */
interface ApiUrl {

    /**
     * Recupera el listado global de publicaciones (Posts).
     * * Endpoint: [BASE_URL]/posts
     * * Método: GET
     * * Autenticación: (Dependerá de los Interceptors configurados en OkHttp).
     *
     * @return [Response] Un envoltorio que contiene la lista de [PostsResponse]
     * parseada desde el JSON retornado por el servidor.
     */
    @GET("posts")
    suspend fun getPosts(): Response<List<PostsResponse>>

}