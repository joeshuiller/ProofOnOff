package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithFavorite
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostFavoriteDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * PostFavoriteDataSourceImpl - Implementación de bajo nivel para la persistencia de favoritos.
 * * Interactúa directamente con el DAO de Room para gestionar la tabla de favoritos
 * y sus relaciones con la tabla de publicaciones.
 *
 * @property apiDao Interfaz de acceso a datos (DAO) autogenerada por Room.
 */
class PostFavoriteDataSourceImpl @Inject constructor(
    private val apiDao: PostFavoriteDao
) : PostFavoriteDataSource {

    /**
     * Obtiene un post específico junto con su información de favorito.
     * @param postId Identificador único de la publicación.
     * @return [Flow] que emite el objeto relacional [PostWithFavorite].
     */
    override fun getPostsWithFavorites(postId: Int): Flow<PostWithFavorite> {
        return apiDao.getPostWithFavorites(postId)
    }

    /**
     * Inserta un nuevo registro de favorito en la base de datos.
     * @param favorite Entidad de persistencia [FavoriteEntity].
     */
    override suspend fun insertFavorite(favorite: FavoriteEntity) {
        apiDao.insertFavorite(favorite)
    }

    /**
     * Elimina el registro de favorito asociado a un post.
     * @param postId ID de la publicación a desmarcar.
     */
    override suspend fun deleteFavoriteByPostId(postId: Int) {
        apiDao.deleteFavoriteByPostId(postId)
    }

    /**
     * Recupera todas las publicaciones que tienen un favorito asociado.
     * * Ideal para alimentar la pantalla de "Mis Favoritos".
     * @return [Flow] con la lista de objetos relacionales.
     */
    override fun getAllPostsWithFavorites(): Flow<List<PostWithFavorite>> {
        return apiDao.getAllPostsWithFavorites()
    }

    /**
     * Verifica de forma reactiva si un post está marcado como favorito.
     * * Emite un booleano cada vez que el estado cambia en la DB.
     * @param postId ID del post a consultar.
     */
    override fun isFavorite(postId: Int): Flow<Boolean> {
        return apiDao.isFavorite(postId)
    }
}
