package com.janes.saenz.puerta.proofonoff.domain.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import kotlinx.coroutines.flow.Flow

/**
 * PostRepository - Contrato de abstracción para el dominio de Publicaciones.
 * * Implementa el patrón Repository para centralizar el acceso a datos.
 * Las implementaciones concretas deben gestionar la coordinación entre la
 * caché local y el servicio remoto.
 */
interface PostRepository {

    /**
     * Realiza una operación atómica de limpieza y actualización.
     * * Acción: Elimina todos los registros existentes e inserta la nueva colección.
     * * Caso de uso: Sincronización inicial o "Pull to Refresh".
     * * @param posts Colección de entidades [Posts] a persistir.
     */
    suspend fun clearAndInsertPosts(posts: List<Posts>)

    /**
     * Provee un flujo reactivo con la totalidad de las publicaciones.
     * * Comportamiento: Emite una nueva lista cada vez que la fuente de datos cambie.
     * * @return [Flow] que emite estados [Resource] con la lista global de [Posts].
     */
    fun observeAllPosts(): Flow<Resource<List<Posts>>>

    /**
     * Filtra las publicaciones basándose en criterios dinámicos.
     * * Implementación sugerida: Si ambos parámetros son null, debe comportarse como [observeAllPosts].
     * * @param id (Opcional) Identificador exacto del post.
     * @param title (Opcional) Segmento de texto para búsqueda parcial (LIKE).
     * @return [Flow] reactivo con los resultados que cumplen los criterios.
     */
    fun observeFilteredPosts(id: Int?, title: String?): Flow<Resource<List<Posts>>>

    /**
     * Recupera un recurso único identificado por su ID.
     * * Es vital para la vista de detalle. Debe emitir un error si el ID no existe.
     * * @param id Identificador único del recurso.
     * @return [Flow] que observa los cambios de un solo objeto [Posts].
     */
    fun getPostById(id: Int): Flow<Resource<Posts>>
}