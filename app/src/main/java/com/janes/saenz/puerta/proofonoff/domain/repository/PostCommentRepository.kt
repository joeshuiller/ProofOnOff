package com.janes.saenz.puerta.proofonoff.domain.repository

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import kotlinx.coroutines.flow.Flow

/**
 * PostCommentRepository - Contrato de persistencia para la gestión de comentarios.
 * * Actúa como la única fuente de verdad para los hilos de conversación asociados
 * a las publicaciones. Implementa un modelo de datos reactivo y asíncrono.
 */
interface PostCommentRepository {

    /**
     * Obtiene un flujo observable de comentarios vinculados a un Post específico.
     * * Comportamiento: Emite un [Resource.Loading] inicial seguido de la lista de
     * comentarios cada vez que ocurra un cambio en la tabla persistente.
     * * @param postId Identificador único de la publicación padre.
     * @return [Flow] reactivo que encapsula el estado de la consulta en un [Resource].
     */
    fun getPostWithComments(postId: Int): Flow<Resource<List<Comments>>>

    /**
     * Realiza una inserción masiva de comentarios en el almacenamiento local.
     * * Caso de uso: Sincronización de datos tras una respuesta exitosa del servidor.
     * * @param comments Colección de entidades [Comments] a persistir.
     */
    suspend fun insertComments(comments: List<Comments>)

    /**
     * Registra un comentario individual de forma persistente.
     * * Caso de uso: Acción de usuario "Publicar comentario" desde la UI.
     * * @param comment Entidad única de comentario a insertar.
     */
    suspend fun insertComment(comment: Comments)

    /**
     * Elimina todos los comentarios asociados a una publicación determinada.
     * * Vital para procesos de limpieza antes de re-sincronización o eliminación de posts.
     * * @param postId Identificador de la publicación cuyos comentarios serán removidos.
     */
    suspend fun deleteCommentsByPostId(postId: Int)
}
