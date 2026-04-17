package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.FavoriteEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Favorite
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class FavoriteDataBaseMapperTest {

    private lateinit var mapper: FavoriteDataBaseMapper

    @Before
    fun setUp() {
        mapper = FavoriteDataBaseMapper()
    }

    @Test
    fun `fromDomainToEntity should map postId correctly`() {
        // GIVEN
        val domain = Favorite(
            favoriteId = 0, // ID temporal antes de persistir
            postId = 101,
            createdAt = 123456789
        )

        // WHEN
        val result = mapper.fromDomainToEntity(domain)

        // THEN
        assertEquals(domain.postId, result.postId)
        // Nota: favoriteId no se mapea de dominio a entidad porque suele ser
        // @PrimaryKey(autoGenerate = true) en la base de datos.
    }

    @Test
    fun `fromEntityToDomain should map all fields correctly`() {
        // GIVEN
        val entity = FavoriteEntity(
            postId = 202
        ).apply {
            favoriteId = 5 // Simula ID generado por Room
            createdAt = 123456789
        }

        // WHEN
        val result = mapper.fromEntityToDomain(entity)

        // THEN
        assertEquals(entity.favoriteId, result.favoriteId)
        assertEquals(entity.postId, result.postId)
        assertEquals(entity.createdAt, result.createdAt)
    }

    @Test
    fun `fromEntityListToDomain should filter nulls and map full list`() {
        // GIVEN
        val entity1 = FavoriteEntity(postId = 1).apply { favoriteId = 1 }
        val entity2 = null
        val entity3 = FavoriteEntity(postId = 3).apply { favoriteId = 3 }
        val entities = listOf(entity1, entity2, entity3)

        // WHEN
        val result = mapper.fromEntityListToDomain(entities)

        // THEN
        assertEquals(2, result.size)
        assertEquals(entity1.postId, result[0].postId)
        assertEquals(entity3.postId, result[1].postId)
    }

    @Test
    fun `fromEntityListToDomain with empty list should return empty list`() {
        // GIVEN
        val entities = emptyList<FavoriteEntity?>()

        // WHEN
        val result = mapper.fromEntityListToDomain(entities)

        // THEN
        assertTrue(result.isEmpty())
    }
}