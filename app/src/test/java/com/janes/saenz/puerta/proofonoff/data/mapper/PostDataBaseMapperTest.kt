package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.PostEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Posts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PostDataBaseMapperTest {

    private lateinit var mapper: PostDataBaseMapper

    @Before
    fun setUp() {
        mapper = PostDataBaseMapper()
    }

    @Test
    fun `fromDomainToEntity should map core fields correctly`() {
        // GIVEN
        val domain = Posts(
            id = 1,
            userId = 10,
            title = "Architectural Review",
            body = "Clean Architecture is a software design philosophy...",
            createdAt = 123456789,
            updatedAt = 123456789
        )

        // WHEN
        val result = mapper.fromDomainToEntity(domain)

        // THEN
        assertEquals(domain.id, result.id)
        assertEquals(domain.userId, result.userId)
        assertEquals(domain.title, result.title)
        assertEquals(domain.body, result.body)
        // Nota: createdAt/updatedAt no se mapean a Entity en tu código,
        // asumiendo que Room gestiona estos metadatos automáticamente.
    }

    @Test
    fun `fromEntityToDomain should map all fields including timestamps`() {
        // GIVEN
        val entity = PostEntity(
            id = 1,
            userId = 10,
            title = "Persisted Title",
            body = "Persisted Body",
            createdAt = 123456789,
            updatedAt = 123456789
        )

        // WHEN
        val result = mapper.fromEntityToDomain(entity)

        // THEN
        assertEquals(entity.id, result.id)
        assertEquals(entity.userId, result.userId)
        assertEquals(entity.title, result.title)
        assertEquals(entity.body, result.body)
        assertEquals(entity.createdAt, result.createdAt)
        assertEquals(entity.updatedAt, result.updatedAt)
    }

    @Test
    fun `fromEntityListToDomain should filter null elements and return domain list`() {
        // GIVEN
        val entity1 = PostEntity(1, 10, "T1", "B1", 123456789, 123456789)
        val entity2 = null
        val entity3 = PostEntity(2, 20, "T2", "B2", 123456789, 123456789)
        val entities = listOf(entity1, entity2, entity3)

        // WHEN
        val result = mapper.fromEntityListToDomain(entities)

        // THEN
        assertEquals(2, result.size)
        assertEquals(entity1.id, result[0].id)
        assertEquals(entity3.id, result[1].id)
    }

    @Test
    fun `fromEntityListToDomain with empty list should return empty list`() {
        // GIVEN
        val entities = emptyList<PostEntity?>()

        // WHEN
        val result = mapper.fromEntityListToDomain(entities)

        // THEN
        assertTrue(result.isEmpty())
    }
}
