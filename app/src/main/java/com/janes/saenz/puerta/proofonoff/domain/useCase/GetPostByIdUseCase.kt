package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.data.utils.Resource
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import com.janes.saenz.puerta.proofonoff.domain.repository.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * GetPostByIdUseCase - Interactor para la recuperación de una entidad única.
 *
 * Provee acceso reactivo a los datos de un [Posts] específico. Este componente
 * es fundamental para mantener la integridad visual cuando se navega hacia
 * la vista detallada de un recurso.
 *
 * @property repository Contrato de acceso a datos que gestiona la persistencia de posts.
 */
class GetPostByIdUseCase @Inject constructor(
    private val repository: PostRepository
) {
    /**
     * Inicia la observación de un Post identificado por su [id].
     * * La naturaleza reactiva de este flujo garantiza que cualquier cambio en la
     * base de datos local (SSOT) se propague inmediatamente a la UI de detalle.
     *
     * @param id Identificador único de la publicación a recuperar.
     * @return Un [Flow] observable que emite el estado [Resource] con la entidad [Posts].
     */
    operator fun invoke(id: Int): Flow<Resource<Posts>> {
        return repository.getPostById(id)
    }
}