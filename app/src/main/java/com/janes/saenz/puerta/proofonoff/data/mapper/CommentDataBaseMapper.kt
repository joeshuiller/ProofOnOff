package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import javax.inject.Inject

/**
 * CommentDataBaseMapper - Traductor de datos para el módulo de comentarios.
 * * Gestiona la conversión bidireccional entre la capa de persistencia (Room)
 * y la capa de dominio, garantizando la integridad de los hilos de conversación.
 */
class CommentDataBaseMapper @Inject constructor() {

    /**
     * Transforma un modelo de dominio en una entidad de base de datos.
     * * Uso: Preparar un comentario nuevo o sincronizado para su almacenamiento en Room.
     * * @param domain Objeto de negocio [Comments] con la información del autor y contenido.
     * @return [CommentEntity] configurada para la inserción en la tabla de comentarios.
     */
    fun fromDomainToEntity(domain: Comments): CommentEntity {
        return CommentEntity(
            id = domain.id,
            postId = domain.postId,
            name = domain.name,
            email = domain.email,
            body = domain.body
        )
    }

    /**
     * Transforma una entidad de Room en un modelo de dominio.
     * * Uso: Recuperar comentarios de la base de datos para mostrarlos en la UI.
     * * @param entity Registro extraído de la base de datos con metadatos de creación.
     * @return Entidad de dominio [Comments] lista para la lógica de presentación.
     */
    fun fromEntityToDomain(entity: CommentEntity): Comments {
        return Comments(
            id = entity.id,
            postId = entity.postId,
            name = entity.name,
            email = entity.email,
            body = entity.body,
            createdAt = entity.createdAt
        )
    }

    /**
     * Mapea de forma segura una lista de entidades de base de datos a modelos de dominio.
     * * Ejecuta un filtrado de nulos para prevenir errores en tiempo de ejecución.
     * * @param entities Colección de registros de comentarios (pueden ser nulos).
     * @return Lista inmutable de objetos de dominio [Comments].
     */
    fun fromEntityListToDomain(entities: List<CommentEntity?>): List<Comments> =
        entities.filterNotNull().map { fromEntityToDomain(it) }
}
