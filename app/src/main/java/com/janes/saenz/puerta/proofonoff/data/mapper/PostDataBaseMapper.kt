package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import javax.inject.Inject

/**
 * PostDataBaseMapper - Componente de mapeo para la capa de persistencia.
 * * Gestiona la conversión bidireccional entre los modelos de la base de datos local
 * y las entidades de la capa de dominio.
 */
class PostDataBaseMapper @Inject constructor() {

    /**
     * Transforma una entidad de Dominio a una entidad de Persistencia (Room).
     * * Uso: Preparar datos para ser insertados o actualizados en la base de datos.
     * * @param domain Objeto de negocio de tipo [Posts].
     * @return Objeto [PostEntity] listo para ser procesado por el DAO.
     */
    fun fromDomainToEntity(domain: Posts): PostEntity {
        return PostEntity(
            id = domain.id,
            userId = domain.userId,
            title = domain.title.toString(),
            body = domain.body.toString()
        )
    }

    /**
     * Transforma una entidad de Room a un modelo de Dominio.
     * * Uso: Recuperar datos desde la base de datos para ser consumidos por la lógica de negocio.
     * * @param entity Registro recuperado desde SQLite.
     * @return Entidad [Posts] con su estado y metadatos preservados.
     */
    fun fromEntityToDomain(entity: PostEntity): Posts {
        return Posts(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            body = entity.body,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    /**
     * Procesa colecciones de entidades de base de datos.
     * * Implementa un filtrado de seguridad para ignorar posibles elementos nulos.
     * * @param entities Lista de registros opcionalmente nulos de la base de datos.
     * @return Lista inmutable de objetos de dominio [Posts].
     */
    fun fromEntityListToDomain(entities: List<PostEntity?>) =
        entities.filterNotNull().map { fromEntityToDomain(it) }
}
