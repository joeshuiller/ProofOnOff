package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Favorite
import javax.inject.Inject

/**
 * FavoriteDataBaseMapper - Componente de mapeo para la gestión de favoritos en persistencia.
 * * Facilita la transición de datos entre el dominio de la aplicación y el motor SQLite.
 */
class FavoriteDataBaseMapper @Inject constructor() {

    /**
     * Convierte un modelo de dominio en una entidad de persistencia.
     * * Uso principal: Guardar un nuevo favorito o actualizar uno existente.
     * * @param domain Objeto [Favorite] proveniente de la lógica de negocio.
     * @return [FavoriteEntity] lista para ser insertada mediante el DAO.
     */
    fun fromDomainToEntity(domain: Favorite): FavoriteEntity {
        return FavoriteEntity(
            postId = domain.postId
        )
    }

    /**
     * Convierte una entidad de base de datos en un modelo de dominio.
     * * Uso principal: Recuperar la lista de favoritos para mostrar en la interfaz.
     * * @param entity Registro extraído directamente de Room.
     * @return Modelo [Favorite] con identificadores y marcas de tiempo recuperadas.
     */
    fun fromEntityToDomain(entity: FavoriteEntity): Favorite {
        return Favorite(
            favoriteId = entity.favoriteId,
            postId = entity.postId,
            createdAt = entity.createdAt
        )
    }

    /**
     * Mapea de forma segura una lista de entidades de base de datos a modelos de dominio.
     * * Garantiza que la lista resultante no contenga elementos nulos.
     * * @param entities Colección de registros (pueden ser nulos).
     * @return Lista inmutable de objetos [Favorite].
     */
    fun fromEntityListToDomain(entities: List<FavoriteEntity?>): List<Favorite> =
        entities.filterNotNull().map { fromEntityToDomain(it) }
}