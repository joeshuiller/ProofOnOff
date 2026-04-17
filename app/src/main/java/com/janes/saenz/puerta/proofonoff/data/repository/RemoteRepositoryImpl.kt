package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.mapper.PostResponseMapper
import com.janes.saenz.puerta.proofonoff.data.network.source.RemoteDataSource
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.RemoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * RemoteRepositoryImpl - Implementación de la fuente de datos externa.
 * * Orquesta la recuperación de datos desde el servidor, asegurando que la
 * lógica de negocio reciba modelos de dominio listos para usar.
 *
 * @property remoteData Servicio de red (normalmente una interfaz de Retrofit).
 * @property mapper Componente encargado de la conversión DTO <-> Domain.
 */
class RemoteRepositoryImpl @Inject constructor(
    private val remoteData: RemoteDataSource,
    private val mapper: PostResponseMapper
) : BaseRepository(), RemoteRepository {

    /**
     * Recupera el listado de publicaciones desde la API.
     * * Implementa un pipeline de datos que:
     * 1. Ejecuta la llamada en un entorno seguro ([safeApiCallFlow]).
     * 2. Intercepta el resultado exitoso para realizar el mapeo de datos.
     * 3. Propaga los estados de Carga y Error sin alteraciones.
     *
     * @return [Flow] que emite el estado del recurso con la lista de modelos de dominio [Posts].
     */
    override suspend fun getPosts(): Flow<Resource<List<Posts>>> {
        return safeApiCallFlow { remoteData.getPosts() }.map { resource ->
            when (resource) {
                is Resource.Success -> {
                    // Conversión de capa de red a capa de dominio
                    val domainList = mapper.fromResponseListToDomain(resource.data)
                    Resource.Success(domainList)
                }
                // Propagación transparente de estados que no requieren mapeo
                is Resource.Error -> Resource.Error(resource.message, resource.code)
                is Resource.Loading -> Resource.Loading
            }
        }
    }
}