package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.PostDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * PostRepositoryImpl - Implementación del repositorio de persistencia local.
 *
 * Coordina el flujo de datos entre la fuente de datos (DataSource/DAO) y la capa de dominio.
 *
 * @property remoteData Fuente de datos local (comúnmente una abstracción de Room).
 * @property mapper Utilidad para convertir entre modelos de base de datos y modelos de dominio.
 */
class PostRepositoryImpl @Inject constructor(
    private val remoteData: PostDataSource,
    private val mapper: PostDataBaseMapper
) : BaseRepository(), PostRepository {

    /**
     * Limpia la caché actual e inserta una nueva lista de publicaciones.
     * Operación atómica ejecutada en el hilo de IO.
     */
    override suspend fun clearAndInsertPosts(posts: List<Posts>) {
        withContext(Dispatchers.IO) {
            val entities = posts.map { mapper.fromDomainToEntity(it) }
            remoteData.clearAndInsertPosts(entities)
        }
    }

    /**
     * Observa el flujo de todas las publicaciones almacenadas.
     * Realiza un mapeo reactivo de Entity a Domain y envuelve el resultado en [Resource].
     */
    override fun observeAllPosts(): Flow<Resource<List<Posts>>> {
        return remoteData.observeAllPosts()
            .map { entities -> mapper.fromEntityListToDomain(entities) }
            .asResource()
            .flowOn(Dispatchers.IO)
    }

    /**
     * Observa publicaciones filtradas por ID o título.
     * Permite búsquedas dinámicas manteniendo la reactividad de la base de datos.
     */
    override fun observeFilteredPosts(
        id: Int?,
        title: String?
    ): Flow<Resource<List<Posts>>> {
        return remoteData.observeFilteredPosts(id, title)
            .map { entities -> mapper.fromEntityListToDomain(entities) }
            .asResource()
            .flowOn(Dispatchers.IO)
    }

    /**
     * Recupera y observa un post específico por su identificador.
     * Transforma la entidad única recuperada al modelo de dominio.
     */
    override fun getPostById(id: Int): Flow<Resource<Posts>> {
        return remoteData.getPostById(id)
            .map { entity -> mapper.fromEntityToDomain(entity) }
            .asResource()
            .flowOn(Dispatchers.IO)
    }
}
