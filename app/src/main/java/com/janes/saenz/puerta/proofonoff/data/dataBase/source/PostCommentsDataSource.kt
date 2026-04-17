package com.janes.saenz.puerta.proofonoff.data.dataBase.source

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.relations.PostWithComments
import kotlinx.coroutines.flow.Flow

/**
 * PostCommentsDataSource - Contrato de persistencia para el sistema de comentarios.
 * * Define el comportamiento esperado del motor de base de datos local para la
 * gestión de hilos de conversación vinculados a publicaciones.
 */
interface PostCommentsDataSource {

    /**
     * Recupera de forma reactiva un post y su colección completa de comentarios.
     * * Implementación sugerida: Uso de @Relation en Room para el objeto relacional.
     * * @param postId Identificador único de la publicación padre.
     * @return [Flow] que emite el objeto [PostWithComments] cada vez que cambian los datos.
     */
    fun getPostWithComments(postId: Int): Flow<PostWithComments>

    /**
     * Inserta una colección de comentarios en el almacenamiento local.
     * * Caso de uso: Sincronización masiva de datos desde el servidor remoto.
     * * @param comments Lista de entidades [CommentEntity] a persistir.
     */
    suspend fun insertComments(comments: List<CommentEntity>)

    /**
     * Registra un comentario individual de forma persistente.
     * * Caso de uso: Envío de un nuevo comentario por parte del usuario desde la UI.
     * * @param comment Entidad única [CommentEntity] a guardar.
     */
    suspend fun insertComment(comment: CommentEntity)

    /**
     * Elimina todos los comentarios asociados a una publicación específica.
     * * Útil para procesos de limpieza de caché o invalidación de hilos específicos.
     * * @param postId ID del post cuyos comentarios deben ser removidos.
     */
    suspend fun deleteCommentsByPostId(postId: Int)
}