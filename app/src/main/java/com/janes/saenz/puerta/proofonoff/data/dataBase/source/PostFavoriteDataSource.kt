package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithFavorite
import kotlinx.coroutines.flow.Flow

/**
 * PostFavoriteDataSource - Contrato de acceso a datos locales para Favoritos.
 * * Define el comportamiento esperado de la fuente de datos de persistencia.
 * Las implementaciones deben garantizar la integridad de las relaciones entre
 * las publicaciones y los marcadores del usuario.
 */
interface PostFavoriteDataSource {

    /**
     * Recupera una publicación específica vinculada a su estado de favorito.
     * * @param postId Identificador único de la publicación.
     * @return [Flow] que emite el objeto relacional [PostWithFavorite].
     */
    fun getPostsWithFavorites(postId: Int): Flow<PostWithFavorite>

    /**
     * Registra un nuevo favorito en el almacenamiento persistente.
     * * Operación de escritura suspendida para evitar bloqueos en el hilo principal.
     * * @param favorite Entidad de base de datos [FavoriteEntity].
     */
    suspend fun insertFavorite(favorite: FavoriteEntity)

    /**
     * Elimina el marcador de favorito asociado a una publicación.
     * * @param postId ID de la publicación que se desea desmarcar.
     */
    suspend fun deleteFavoriteByPostId(postId: Int)

    /**
     * Obtiene el listado completo de publicaciones marcadas como favoritas.
     * * Ideal para alimentar vistas de colecciones o bibliotecas personales.
     * @return [Flow] con la lista de todos los objetos relacionales encontrados.
     */
    fun getAllPostsWithFavorites(): Flow<List<PostWithFavorite>>

    /**
     * Consulta reactiva para verificar si un post es favorito.
     * * Emite un booleano cada vez que la tabla de favoritos sufre una modificación.
     * * @param postId ID de la publicación a verificar.
     * @return [Flow] que emite true si existe la relación, false en caso contrario.
     */
    fun isFavorite(postId: Int): Flow<Boolean>
}
