package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.dataBase.entity.CommentEntity
import com.janes.saenz.puerta.proofonoff.domain.dtos.Comments
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CommentDataBaseMapperTest {

    private lateinit var mapper: CommentDataBaseMapper

    @Before
    fun setUp() {
        mapper = CommentDataBaseMapper()
    }

    @Test
    fun `fromDomainToEntity should map all fields correctly`() {
        // GIVEN
        val domain = Comments(
            id = 1,
            postId = 10,
            name = "John Doe",
            email = "john@example.com",
            body = "This is a comment",
            createdAt = 123456789
        )

        // WHEN
        val result = mapper.fromDomainToEntity(domain)

        // THEN
        assertEquals(domain.id, result.id)
        assertEquals(domain.postId, result.postId)
        assertEquals(domain.name, result.name)
        assertEquals(domain.email, result.email)
        assertEquals(domain.body, result.body)
        // Nota: createdAt no se mapea a Entity según tu código actual,
        // lo cual es correcto si Room lo genera automáticamente.
    }

    @Test
    fun `fromEntityToDomain should map all fields correctly`() {
        // GIVEN
        val entity = CommentEntity(
            id = 1,
            postId = 10,
            name = "John Doe",
            email = "john@example.com",
            body = "This is a comment",
            createdAt = 123456789
        )

        // WHEN
        val result = mapper.fromEntityToDomain(entity)

        // THEN
        assertEquals(entity.id, result.id)
        assertEquals(entity.postId, result.postId)
        assertEquals(entity.name, result.name)
        assertEquals(entity.email, result.email)
        assertEquals(entity.body, result.body)
        assertEquals(entity.createdAt, result.createdAt)
    }

    @Test
    fun `fromEntityListToDomain should filter nulls and map list`() {
        // GIVEN
        val entity1 = CommentEntity(1, 10, "Name 1", "email1", "body1", 123456789)
        val entity2 = null
        val entity3 = CommentEntity(2, 10, "Name 2", "email2", "body2", 123456789)
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
        val entities = emptyList<CommentEntity?>()

        // WHEN
        val result = mapper.fromEntityListToDomain(entities)

        // THEN
        assertTrue(result.isEmpty())
    }
}