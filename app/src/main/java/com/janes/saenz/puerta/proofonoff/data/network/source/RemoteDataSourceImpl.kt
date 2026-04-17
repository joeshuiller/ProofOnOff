package com.janes.saenz.puerta.proofonoff.data.network.source

import com.janes.saenz.puerta.proofonoff.data.network.api.ApiUrl
import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import retrofit2.Response
import javax.inject.Inject

/**
 * RemoteDataSourceImpl - Implementación de bajo nivel para el acceso a red.
 * * Provee los datos crudos provenientes de los endpoints de la API.
 * * No aplica lógica de negocio; solo actúa como un wrapper para [apiService].
 *
 * @property apiService Instancia de Retrofit inyectada que define los endpoints HTTP.
 */
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiUrl
) : RemoteDataSource {

    /**
     * Ejecuta la petición GET para recuperar el listado de publicaciones.
     * * Esta función es de suspensión, lo que permite realizar la operación
     * de I/O sin bloquear el hilo principal.
     *
     * @return Una envoltura [Response] que contiene la lista de [PostsResponse].
     */
    override suspend fun getPosts(): Response<List<PostsResponse>> {
        return apiService.getPosts()
    }
}