package com.janes.saenz.puerta.proofonoff.data.repository

import com.janes.saenz.puerta.proofonoff.data.dataBase.source.PostCommentsDataSource
import com.janes.saenz.puerta.proofonoff.data.mapper.CommentDataBaseMapper
import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * PostCommentRepositoryImpl - Implementación de persistencia para el módulo de comentarios.
 *
 * Esta clase orquesta la lectura reactiva y la escritura de comentarios en la base de datos local,
 * asegurando que la capa de dominio permanezca agnóstica a los detalles de implementación de Room.
 *
 * @property remoteData Fuente de datos local (DAO) que gestiona la tabla de comentarios.
 * @property mapper Utilidad para convertir entre el modelo de persistencia y el de dominio.
 */
class PostCommentRepositoryImpl @Inject constructor(
    private val remoteData: PostCommentsDataSource,
    private val mapper: CommentDataBaseMapper
): BaseRepository(), PostCommentRepository {

    /**
     * Obtiene y observa los comentarios de una publicación específica.
     * * Transforma la relación de base de datos en una lista de dominio [Comments].
     */
    override fun getPostWithComments(postId: Int): Flow<Resource<List<Comments>>> {
        return remoteData.getPostWithComments(postId)
            .map { entities ->
                // Mapeo de la colección de comentarios contenida en el objeto relacional
                mapper.fromEntityListToDomain(entities.comments)
            }
            .asResource()
            .flowOn(Dispatchers.IO)
    }

    /**
     * Inserta una lista de comentarios (útil para sincronización masiva).
     */
    override suspend fun insertComments(comments: List<Comments>) {
        withContext(Dispatchers.IO) {
            val entities = comments.map { mapper.fromDomainToEntity(it) }
            remoteData.insertComments(entities)
        }
    }

    /**
     * Inserta un comentario único (procedente de la UI).
     */
    override suspend fun insertComment(comment: Comments) {
        withContext(Dispatchers.IO) {
            val entity = mapper.fromDomainToEntity(comment)
            remoteData.insertComment(entity)
        }
    }

    /**
     * Elimina todos los comentarios asociados a un Post para limpieza de caché.
     */
    override suspend fun deleteCommentsByPostId(postId: Int) {
        withContext(Dispatchers.IO) {
            remoteData.deleteCommentsByPostId(postId)
        }
    }
}