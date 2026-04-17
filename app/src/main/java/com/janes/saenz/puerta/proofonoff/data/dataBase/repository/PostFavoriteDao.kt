package com.janes.saenz.puerta.proofonoff.data.dataBase.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithFavorite
import kotlinx.coroutines.flow.Flow

/**
 * PostFavoriteDao - Objeto de Acceso a Datos para la persistencia de Favoritos.
 *
 * Define las operaciones SQL necesarias para gestionar la relación entre los posts
 * y el interés del usuario. Implementa reactividad nativa mediante [Flow].
 */
@Dao
interface PostFavoriteDao {

    /**
     * Inserta o actualiza un registro de favorito.
     * * Estrategia de conflicto: [REPLACE]. Si el favorito ya existe, se sobreescribe.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    /**
     * Elimina el marcador de favorito asociado a una publicación específica.
     * @param postId Identificador del post a desvincular.
     */
    @Query("DELETE FROM favorites WHERE postId = :postId")
    suspend fun deleteFavoriteByPostId(postId: Int)

    /**
     * Recupera todas las publicaciones del sistema junto con su estado de favorito.
     * * Usa [@Transaction] para garantizar consistencia en la carga de la relación.
     * @return [Flow] con una lista de objetos relacionales [PostWithFavorite].
     */
    @Transaction
    @Query("SELECT * FROM posts_table")
    fun getAllPostsWithFavorites(): Flow<List<PostWithFavorite>>

    /**
     * Verifica de forma ultra-eficiente si un post está marcado como favorito.
     * * Retorna un booleano reactivo.
     * @param postId ID del post a consultar.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE postId = :postId)")
    fun isFavorite(postId: Int): Flow<Boolean>

    /**
     * Obtiene una publicación específica y su metadato de favorito.
     * * Es vital para la pantalla de detalle.
     * @param postId Identificador de la publicación.
     */
    @Transaction
    @Query("SELECT * FROM posts_table WHERE id = :postId")
    fun getPostWithFavorites(postId: Int): Flow<PostWithFavorite>
}