package com.janes.saenz.puerta.proofonoff.data.dataBase.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import kotlinx.coroutines.flow.Flow

/**
 * PostCommentsDao - Interfaz de acceso a datos para la gestión de comentarios.
 *
 * Define las operaciones SQL para interactuar con la tabla de comentarios
 * y su relación con las publicaciones. Implementa reactividad mediante [Flow].
 */
@Dao
interface PostCommentsDao {

    /**
     * Recupera una publicación y todos sus comentarios asociados de forma reactiva.
     * * Nota: Utiliza [@Transaction] para garantizar la integridad de la unión (Join)
     * que Room realiza internamente para construir el objeto [PostWithComments].
     *
     * @param postId Identificador de la publicación padre.
     * @return [Flow] observable con los datos del post y su lista de comentarios.
     */
    @Transaction
    @Query("SELECT * FROM posts_table WHERE id = :postId")
    fun getPostWithComments(postId: Int): Flow<PostWithComments>

    /**
     * Inserta una lista de comentarios (Sincronización masiva).
     * * Reemplaza los registros existentes en caso de conflicto de ID.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    /**
     * Registra un comentario individual.
     * * Caso de uso: Cuando el usuario crea un comentario desde la aplicación.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    /**
     * Elimina físicamente todos los comentarios vinculados a un post específico.
     * @param postId Identificador del post cuyos comentarios deben borrarse.
     */
    @Query("DELETE FROM comments WHERE postId = :postId")
    suspend fun deleteCommentsByPostId(postId: Int)
}