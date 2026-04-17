package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import com.janes.saenz.puerta.proofonoff.data.dataBase.repository.PostCommentsDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * PostCommentsDataSourceImpl - Implementación de bajo nivel para la persistencia de comentarios.
 * * Gestiona la interacción directa con el motor SQLite a través de [PostCommentsDao].
 * * Provee mecanismos reactivos para observar hilos de conversación vinculados a posts.
 *
 * @property apiDao Interfaz de acceso a datos (DAO) autogenerada por Room.
 */
class PostCommentsDataSourceImpl @Inject constructor(
    private val apiDao: PostCommentsDao
) : PostCommentsDataSource {

    /**
     * Obtiene una publicación junto con su colección de comentarios asociados.
     * * @param postId Identificador de la publicación padre.
     * @return [Flow] que emite el objeto relacional [PostWithComments] de forma reactiva.
     */
    override fun getPostWithComments(postId: Int): Flow<PostWithComments> {
        return apiDao.getPostWithComments(postId)
    }

    /**
     * Inserta múltiples comentarios en una sola operación.
     * * Caso de uso: Almacenamiento tras una petición exitosa a la API remota.
     * * @param comments Lista de entidades [CommentEntity].
     */
    override suspend fun insertComments(comments: List<CommentEntity>) {
        apiDao.insertComments(comments)
    }

    /**
     * Registra un comentario individual de forma persistente.
     * * Caso de uso: Cuando un usuario publica un nuevo comentario desde la UI.
     * * @param comment Entidad única [CommentEntity].
     */
    override suspend fun insertComment(comment: CommentEntity) {
        apiDao.insertComment(comment)
    }

    /**
     * Elimina la totalidad de comentarios vinculados a un identificador de post.
     * * Vital para limpieza de caché o procesos de invalidación de datos.
     * * @param postId ID del post cuyos comentarios serán removidos.
     */
    override suspend fun deleteCommentsByPostId(postId: Int) {
        apiDao.deleteCommentsByPostId(postId)
    }
}