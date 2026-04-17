package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostDao
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

/**
 * PostDataSourceImpl - Implementación de la fuente de datos de persistencia para Posts.
 * * Actúa como un mediador entre el Repositorio y el DAO de Room.
 * * Responsable de la persistencia física de las publicaciones descargadas.
 *
 * @property apiDao Interfaz de acceso a datos (DAO) de Room inyectada por Hilt.
 */
class PostDataSourceImpl @Inject constructor(
    private val apiDao: PostDao
) : PostDataSource {

    /**
     * Reemplaza el contenido local por una nueva lista de publicaciones.
     * * Acción: Elimina registros previos e inserta los nuevos de forma atómica.
     * * @param posts Lista de entidades [PostEntity] a persistir.
     */
    override suspend fun clearAndInsertPosts(posts: List<PostEntity>) {
        apiDao.clearAndInsertPosts(posts)
    }

    /**
     * Provee un flujo observable de todas las publicaciones almacenadas.
     * * @return [Flow] que emite la lista completa cada vez que la tabla cambia.
     */
    override fun observeAllPosts(): Flow<List<PostEntity?>> {
        return apiDao.observeAllPosts()
    }

    /**
     * Observa publicaciones aplicando criterios de búsqueda opcionales.
     * * @param id Filtro por identificador único (opcional).
     * @param title Filtro por coincidencia de texto en el título (opcional).
     * @return [Flow] con los resultados filtrados reactivos.
     */
    override fun observeFilteredPosts(
        id: Int?,
        title: String?
    ): Flow<List<PostEntity?>> {
        return apiDao.observeFilteredPosts(id, title)
    }

    /**
     * Recupera y observa una publicación única por su ID.
     * * @param id Identificador de la publicación.
     * @return [Flow] que emite la entidad específica.
     */
    override fun getPostById(id: Int): Flow<PostEntity> {
        return apiDao.getPostById(id)
    }
}