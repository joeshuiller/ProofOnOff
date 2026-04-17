package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import androidx.room.Transaction
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import kotlinx.coroutines.flow.Flow

/**
 * PostDataSource - Contrato para la gestión de persistencia local de publicaciones.
 *
 * Define las operaciones fundamentales para el almacenamiento, recuperación y
 * filtrado de posts en la base de datos local.
 */
interface PostDataSource {

    /**
     * Realiza una sincronización completa de datos de forma atómica.
     * * Acción: Elimina todos los registros existentes e inserta la nueva colección.
     * * Seguridad: Si la inserción falla, la base de datos mantiene su estado previo.
     * * @param posts Colección de entidades [PostEntity] a persistir.
     */
    @Transaction
    suspend fun clearAndInsertPosts(posts: List<PostEntity>)

    /**
     * Provee un flujo observable de todos los registros de la tabla.
     * * @return [Flow] que emite la lista completa cada vez que hay cambios en la DB.
     */
    fun observeAllPosts(): Flow<List<PostEntity?>>

    /**
     * Observa registros que cumplan con criterios de búsqueda específicos.
     * * @param id Filtro opcional por ID único.
     * @param title Filtro opcional por coincidencia de texto (normalmente búsqueda LIKE).
     * @return [Flow] reactivo con los resultados filtrados.
     */
    fun observeFilteredPosts(id: Int?, title: String?): Flow<List<PostEntity?>>

    /**
     * Recupera y observa de forma continua un registro único.
     * * @param id Identificador de la publicación.
     * @return [Flow] que emite la entidad [PostEntity] correspondiente.
     */
    fun getPostById(id: Int): Flow<PostEntity>
}