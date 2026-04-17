package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetCommentsUseCase - Interactor de consulta reactiva de comentarios.
 *
 * Facilita el acceso al flujo de datos de comentarios filtrados por el identificador
 * de su publicación padre. Es la pieza central para la reactividad en la vista de detalles.
 *
 * @property repository Repositorio encargado de coordinar el acceso a los datos de comentarios.
 */
class GetCommentsUseCase @Inject constructor(
    private val repository: PostCommentRepository,
) {
    /**
     * Inicia una suscripción al flujo de comentarios de un Post específico.
     * * Al devolver un [Flow], este interactor permite que la capa de presentación
     * mantenga la consistencia visual con la base de datos local (SSOT) de forma asíncrona.
     *
     * @param postId Identificador único de la publicación (Foreign Key).
     * @return Un flujo observable [Flow] que emite estados [Resource] con la lista de [Comments].
     */
    operator fun invoke(postId: Int): Flow<Resource<List<Comments>>> {
        return repository.getPostWithComments(postId)
    }
}
