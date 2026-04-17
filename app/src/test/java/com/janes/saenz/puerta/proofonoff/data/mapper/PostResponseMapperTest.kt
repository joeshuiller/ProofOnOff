package com.janes.saenz.puerta.proofonoff.data.mapper

import com.janes.saenz.puerta.proofonoff.data.network.dtos.response.PostsResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class PostResponseMapperTest {

    private lateinit var mapper: PostResponseMapper

    @Before
    fun setUp() {
        mapper = PostResponseMapper()
    }

    @Test
    fun `fromResponseToDomain should map all fields and sanitize null strings`() {
        // GIVEN
        val response = PostsResponse(
            userId = 1,
            id = 101,
            title = "Hello World",
            body = null // Testeamos el orEmpty()
        )

        // WHEN
        val result = mapper.fromResponseToDomain(response)

        // THEN
        assertEquals(response.id, result.id)
        assertEquals(response.userId, result.userId)
        assertEquals("Hello World", result.title)
        assertEquals("", result.body) // Verificamos saneamiento
        assertNotNull(result.createdAt)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `fromResponseListToDomain should map list and filter null elements`() {
        // GIVEN
        val response1 = PostsResponse(1, 101, "T1", "B1")
        val response2 = null
        val response3 = PostsResponse(2, 102, "T2", "B2")
        val responses = listOf(response1, response2, response3)

        // WHEN
        val result = mapper.fromResponseListToDomain(responses)

        // THEN
        assertEquals(2, result.size)
        assertEquals(response1.id, result[0].id)
        assertEquals(response3.id, result[1].id)
    }

    @Test
    fun `fromResponseListToDomain with null input should return empty list`() {
        // GIVEN
        val responses: List<PostsResponse?>? = null

        // WHEN
        val result = mapper.fromResponseListToDomain(responses)

        // THEN
        assertTrue(result.isEmpty())
    }

    @Test
    fun `fromResponseListToDomain with empty list should return empty list`() {
        // GIVEN
        val responses = emptyList<PostsResponse>()

        // WHEN
        val result = mapper.fromResponseListToDomain(responses)

        // THEN
        assertTrue(result.isEmpty())
    }
}