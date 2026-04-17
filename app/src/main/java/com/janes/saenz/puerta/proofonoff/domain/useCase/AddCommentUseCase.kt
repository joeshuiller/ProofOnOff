package com.janes.saenz.puerta.proofonoff.domain.useCase

import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import com.janes.saenz.puerta.proofonoff.domain.repository.PostCommentRepository
import javax.inject.Inject

/**
 * AddCommentUseCase - Interactor para la persistencia de comentarios.
 *
 * Provee una interfaz limpia para insertar un objeto de tipo [Comments]
 * en el almacenamiento persistente del sistema.
 *
 * @property repository Contrato de acceso a datos para operaciones de comentarios.
 */
class AddCommentUseCase @Inject constructor(
    private val repository: PostCommentRepository,
) {
    /**
     * Ejecuta la acción de inserción de un comentario.
     * * Esta es una función de suspensión que garantiza que la operación
     * de escritura se realice sin bloquear el hilo principal, delegando
     * el contexto de ejecución al repositorio.
     *
     * @param comment Entidad de dominio [Comments] que contiene la información del mensaje.
     */
    suspend operator fun invoke(comment: Comments) {
        return repository.insertComment(comment)
    }
}